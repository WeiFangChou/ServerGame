package william;

import com.lineage.DatabaseFactory;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.sql.Connection;
import java.sql.ResultSet;

public class Pay_Point {
    public static void PayPoint(L1PcInstance pc, long count) {
        try {
            Connection conn = DatabaseFactory.get().getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM ezpay_累積儲值禮品");
            int nowb = pc.get_point();
            if (rs != null) {
                while (rs.next()) {
                    int money = rs.getInt("累積金額");
                    int itemid = rs.getInt("物品編號");
                    int itemcount = rs.getInt("物品數量");
                    int quest_id = rs.getInt("任務編號");
                    if (((int) count) + nowb >= money && nowb < money && pc.getEzpay_Quest().get_step(quest_id) < 1) {
                        L1ItemInstance items = pc.getInventory().storeItem(itemid, (long) itemcount);
                        items.setIdentified(true);
                        pc.getEzpay_Quest().set_step(quest_id, 1);
                        pc.sendPackets(new S_ServerMessage("累積額度:【" + money + "】禮品:【" + items.getName() + "(" + itemcount + ")】。"));
                        WriteLogTxt.Recording("累積儲值好禮紀錄", "帳號:" + pc.getAccountName().toLowerCase() + " 人物:" + pc.getName() + " 領取好禮:【累積:" + money + "金額】物品:【" + items.getName() + "(" + itemcount + ")數量】 ");
                    }
                }
            }
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception ignored) {
        }
    }
}
