package com.lineage.server.plugin;

import com.lineage.DatabaseFactory;
import com.lineage.config.ConfigOther;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Account;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import william.PayBonus;
import william.PayBonusFirst;
import william.Pay_Point;

public class P_Pay {
    private static final Log _log = LogFactory.getLog(P_Pay.class);

    public static synchronized void checkSponsor(L1PcInstance pc) {
        synchronized (P_Pay.class) {
            Connection con = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            PreparedStatement pstm2 = null;
            try {
                String AccountName = pc.getAccountName();
                con = DatabaseFactory.get().getConnection();
                pstm = con.prepareStatement("select id,amount,payname,state from ezpay_自動贊助 where state = 1 and payname ='" + AccountName + "'");
                rs = pstm.executeQuery();
                boolean isfind = false;
                while (rs.next() && rs != null) {
                    int serial = rs.getInt("id");
                    if (pc.getAccountName().equalsIgnoreCase(rs.getString("payname"))) {
                        isfind = true;
                        pstm2 = con.prepareStatement("update ezpay_自動贊助 set state = 2 where id = ?");
                        pstm2.setInt(1, serial);
                        pstm2.execute();
                        int count = rs.getInt("amount");
                        Ratio(pc, (long) count, ConfigOther.GASH_SHOP_ITEM_ID);
                        PayBonus.Pay_Bonus(pc, (long) count);
                        int b = AccountReading.get().getPoint(pc.getAccountName()) + count;
                        AccountReading.get().setPoint(pc.getAccountName(), b);
                        Pay_Point.PayPoint(pc, (long) b);
                        L1Account account = pc.getNetConnection().getAccount();
                        if (account.get_pay_first() == 0) {
                            account.set_pay_first(1);
                            AccountReading.get().updateFirstPay(pc.getAccountName(), 1);
                            PayBonusFirst.PayBonus_First(pc, (long) count);
                        }
                    }
                }
                if (!isfind) {
                    pc.sendPackets(new S_SystemMessage("沒有您的贊助資料。"));
                }
            } catch (SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(rs);
                SQLUtil.close(pstm);
                SQLUtil.close(pstm2);
                SQLUtil.close(con);
            }
        }
    }

    private static void Ratio(L1PcInstance pc, long count, int itemid) {
        L1ItemInstance item = ItemTable.get().createItem(itemid);
        item.setIdentified(true);
        if (itemid == ConfigOther.GASH_SHOP_ITEM_ID) {
            Connection conn = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            try {
                conn = DatabaseFactory.get().getConnection();
                pstm = conn.prepareStatement("SELECT * FROM ezpay_金流比值器");
                rs = pstm.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                        int min = rs.getInt("最小贊助比值獎勵範圍");
                        int max = rs.getInt("最大贊助比值獎勵範圍");
                        int ratio = rs.getInt("倍率");
                        if (count >= ((long) min) && count <= ((long) max)) {
                            int TM = (int) (((double) ((int) (((long) ratio) * count))) * 0.01d);
                            GiveItem(pc, itemid, TM);
                            WriteLogTxt.Recording("贊助領取紀錄", "帳號:(" + pc.getAccountName() + ")IP:(" + ((Object) pc.getNetConnection().getIp()) + ")玩家:【 " + pc.getName() + " 】 儲值:經過比值-原本『" + item.getName() + "(" + count + ")』各變為 :『" + item.getName() + "(" + TM + ")』 總共領取: 【" + item.getName() + "(" + TM + ")】個 了。");
                        }
                    }
                }
            } catch (SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(rs);
                SQLUtil.close(pstm);
                SQLUtil.close(conn);
            }
        }
    }

    public static void GiveItem(L1PcInstance pc, int itemId, int count) {
        L1ItemInstance item = ItemTable.get().createItem(itemId);
        item.setIdentified(true);
        item.setCount((long) count);
        if (pc.getInventory().checkAddItem(item, (long) count) == 0) {
            pc.getInventory().storeItem(item);
            pc.sendPackets(new S_ServerMessage("自動贊助專員 給你 " + item.getLogName() + "。"));
        }
    }
}
