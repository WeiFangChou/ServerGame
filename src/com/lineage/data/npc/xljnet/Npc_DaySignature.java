package com.lineage.data.npc.xljnet;

import com.lineage.DatabaseFactory;
import com.lineage.config.ConfigOther;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.Day_Signature;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Day_Signature;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_DaySignature extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_DaySignature.class);
    Random random = new Random();

    private Npc_DaySignature() {
    }

    public static NpcExecutor get() {
        return new Npc_DaySignature();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        pc.setCmd(-1);
        if (pc.getLevel() < ConfigOther.Pc_Level) {
            pc.sendPackets(new S_NpcChat(npc, "簽到限制【" + ConfigOther.Pc_Level + "】級。"));
            return;
        }
        int size = Day_Signature.get().DaySize();
        if (size > 0) {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i <= size; i++) {
                L1Day_Signature Day = Day_Signature.get().getDay(i);
                if (Day != null) {
                    if (search(pc.getAccountName(), Day.getDay() + 33000)) {
                        s.append(String.valueOf(Day.getMsg()) + "『已領取』。,");
                    } else {
                        s.append(String.valueOf(Day.getMsg()) + ",");
                    }
                }
            }
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_daySing", s.toString().split(",")));
        } else {
            pc.sendPackets(new S_SystemMessage("尚未開放。"));
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `每日簽到領取` ORDER BY `流水號`");
            rs = ps.executeQuery();
            do {
            } while (rs.next());
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        L1Day_Signature Day;
        try {
            if (cmd.matches("[0-999]+")) {
                int cmd1 = Integer.valueOf(cmd).intValue() + 1;
                L1Day_Signature Day2 = Day_Signature.get().getDay(cmd1);
                if (Day2 != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("【" + Day2.getMsg() + "】,");
                    if (Day2.getItem() != null) {
                        int index = 0;
                        String[] item = Day2.getItem().split(",");
                        int length = item.length;
                        for (int i = 0; i < length; i++) {
                            L1Item items = ItemTable.get().getTemplate(Integer.parseInt(item[i]));
                            String enchant = Day2.getEnchant().split(",")[index];
                            String count = Day2.getCount().split(",")[index];
                            index++;
                            stringBuilder.append("+【" + enchant + "】" + items.getName() + "(" + count + "),");
                        }
                    } else {
                        stringBuilder.append("無道具可領取,");
                    }
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_daySing_1", stringBuilder.toString().split(",")));
                    pc.setCmd(cmd1);
                }
            } else if (cmd.matches("NP1")) {
                L1Day_Signature Day3 = Day_Signature.get().getDay(pc.getCmd());
                if (Day3 != null) {
                    int questid = Day3.getDay() + 33000;
                    String strDate = new SimpleDateFormat("MMdd").format(new Date());
                    if (Integer.valueOf(strDate).intValue() > Day3.getDay()) {
                        pc.sendPackets(new S_SystemMessage("你選擇的簽到獎勵已經過期只能補簽喔。"));
                        pc.sendPackets(new S_CloseList(pc.getId()));
                        pc.setCmd(-1);
                    } else if (Integer.valueOf(strDate).intValue() < Day3.getDay()) {
                        pc.sendPackets(new S_SystemMessage("你選擇的簽到獎勵領取日期還沒到喔。"));
                        pc.sendPackets(new S_CloseList(pc.getId()));
                        pc.setCmd(-1);
                    } else if (search(pc.getAccountName(), questid)) {
                        pc.sendPackets(new S_SystemMessage("今天的簽到獎勵已經領過囉。"));
                        pc.sendPackets(new S_CloseList(pc.getId()));
                        pc.setCmd(-1);
                    } else if (Day3.getItem() != null) {
                        int index2 = 0;
                        String[] item2 = Day3.getItem().split(",");
                        int length2 = item2.length;
                        for (int i2 = 0; i2 < length2; i2++) {
                            int itemid = Integer.parseInt(item2[i2]);
                            String enchant2 = Day3.getEnchant().split(",")[index2];
                            String count2 = Day3.getCount().split(",")[index2];
                            index2++;
                            L1ItemInstance ItemX = ItemTable.get().createItem(itemid);
                            if (ItemX.isStackable()) {
                                pc.getInventory().storeItem(itemid, (long) Integer.parseInt(count2));
                            } else {
                                ItemX.setEnchantLevel(Integer.parseInt(enchant2));
                                ItemX.setIdentified(true);
                                pc.getInventory().storeItem(ItemX);
                            }
                            pc.sendPackets(new S_NpcChat(npc, "簽到:獲得" + ItemX.getName() + "(" + count2 + ")。"));
                            Index(pc, questid);
                            pc.sendPackets(new S_SystemMessage("恭喜您完成本次簽到獲得簽到獎勵。"));
                            WriteLogTxt.Recording("簽到紀錄", "玩家:【 " + pc.getName() + "】獲得 : 【" + ItemX.getName() + "(" + count2 + ")】。");
                        }
                    }
                } else {
                    pc.sendPackets(new S_SystemMessage("簽到資料異常。"));
                    pc.sendPackets(new S_CloseList(pc.getId()));
                    pc.setCmd(-1);
                }
            } else if (cmd.matches("NP2")) {
                L1Day_Signature Day4 = Day_Signature.get().getDay(pc.getCmd());
                if (Day4 != null) {
                    String strDate2 = new SimpleDateFormat("MMdd").format(new Date());
                    if (Integer.valueOf(strDate2).intValue() < Day4.getDay()) {
                        pc.sendPackets(new S_SystemMessage("你選擇的簽到獎勵領取日期還沒到喔。"));
                        pc.sendPackets(new S_CloseList(pc.getId()));
                        pc.setCmd(-1);
                    } else if (Integer.valueOf(strDate2).intValue() == Day4.getDay()) {
                        pc.sendPackets(new S_SystemMessage("你選擇的簽到獎勵可以直接領取不需要補簽。"));
                        pc.sendPackets(new S_CloseList(pc.getId()));
                        pc.setCmd(-1);
                    } else {
                        L1Item MakeItem = ItemTable.get().getTemplate(Day4.getMakeUp());
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("補簽需要消耗的道具【" + MakeItem.getName() + "(" + Day4.getMakeUpC() + ")】,");
                        stringBuilder2.append("【" + Day4.getMsg() + "】,");
                        if (Day4.getItem() != null) {
                            int index3 = 0;
                            String[] item3 = Day4.getItem().split(",");
                            int length3 = item3.length;
                            for (int i3 = 0; i3 < length3; i3++) {
                                L1Item items2 = ItemTable.get().getTemplate(Integer.parseInt(item3[i3]));
                                String enchant3 = Day4.getEnchant().split(",")[index3];
                                String count3 = Day4.getCount().split(",")[index3];
                                index3++;
                                stringBuilder2.append("+【" + enchant3 + "】" + items2.getName() + "(" + count3 + "),");
                            }
                        } else {
                            stringBuilder2.append("無道具可領取,");
                        }
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_daySing_2", stringBuilder2.toString().split(",")));
                    }
                }
            } else if (cmd.matches("NP3") && (Day = Day_Signature.get().getDay(pc.getCmd())) != null) {
                int questid2 = Day.getDay() + 33000;
                if (search(pc.getAccountName(), questid2)) {
                    pc.sendPackets(new S_SystemMessage("你選擇的簽到獎勵已經領過囉。"));
                    pc.sendPackets(new S_CloseList(pc.getId()));
                    pc.setCmd(-1);
                } else if (Day.getItem() != null) {
                    if (pc.getInventory().checkItem(Day.getMakeUp(), (long) Day.getMakeUpC())) {
                        pc.getInventory().consumeItem(Day.getMakeUp(), (long) Day.getMakeUpC());
                        int index4 = 0;
                        String[] item4 = Day.getItem().split(",");
                        int length4 = item4.length;
                        for (int i4 = 0; i4 < length4; i4++) {
                            int itemid2 = Integer.parseInt(item4[i4]);
                            String enchant4 = Day.getEnchant().split(",")[index4];
                            String count4 = Day.getCount().split(",")[index4];
                            index4++;
                            L1ItemInstance ItemX2 = ItemTable.get().createItem(itemid2);
                            if (ItemX2.isStackable()) {
                                pc.getInventory().storeItem(itemid2, (long) Integer.parseInt(count4));
                            } else {
                                ItemX2.setEnchantLevel(Integer.parseInt(enchant4));
                                ItemX2.setIdentified(true);
                                pc.getInventory().storeItem(ItemX2);
                            }
                            Index(pc, questid2);
                            pc.sendPackets(new S_NpcChat(npc, "補簽到:獲得" + ItemX2.getName() + "(" + count4 + ")。"));
                            pc.sendPackets(new S_SystemMessage("恭喜您完成本次簽到獲得簽到獎勵。"));
                            WriteLogTxt.Recording("簽到紀錄", "玩家:【 " + pc.getName() + "】獲得 : 【" + ItemX2.getName() + "(" + count4 + ")】");
                        }
                        return;
                    }
                    pc.sendPackets(new S_SystemMessage("需求道具不足無法進行補簽。"));
                    pc.sendPackets(new S_CloseList(pc.getId()));
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private boolean search(String s, int i) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT `quest` FROM `每日簽到領取紀錄` WHERE `account_name`=?");
            pstm.setString(1, s);
            rs = pstm.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) == i) {
                    return true;
                }
            }
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return false;
    }

    private void Index(L1PcInstance pc, int i) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("INSERT INTO `每日簽到領取紀錄` SET `account_name`=?,`quest`=?");
            int x = 0 + 1;
            pstm.setString(x, pc.getAccountName());
            pstm.setInt(x + 1, i);
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close((ResultSet) null);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
