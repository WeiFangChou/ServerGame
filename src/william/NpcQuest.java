package william;

import com.lineage.DatabaseFactory;
import com.lineage.Server;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxGree1;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.world.World;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class NpcQuest {
    private static boolean NO_GET_DATA = false;
    public static final String TOKEN = ",";
    private static ArrayList<Object> aData = new ArrayList<>();

    public static void main(String[] a) {
        while (true) {
            try {
                Server.main(null);
            } catch (Exception ignored) {
            }
        }
    }

    public static boolean forNpcQuest(String s, L1PcInstance pc, L1NpcInstance npc, int npcid, int oid) throws Exception {
        if (!NO_GET_DATA) {
            NO_GET_DATA = true;
            getData();
        }
        for (int i = 0; i < aData.size(); i++) {
            ArrayList<?> aTempData = (ArrayList) aData.get(i);
            if (aTempData.get(0) != null && ((Integer) aTempData.get(0)).intValue() == npcid && ((String) aTempData.get(1)).equals(s)) {
                if (((Integer) aTempData.get(2)).intValue() != 0 && pc.getLevel() < ((Integer) aTempData.get(2)).intValue()) {
                    pc.sendPackets(new S_SystemMessage("等級最低需求" + ((Integer) aTempData.get(2)).intValue() + "才可使用。"));
                    return false;
                } else if (pc.getMap_Quest().get_step(((Integer) aTempData.get(7)).intValue()) == 255) {
                    pc.sendPackets(new S_SystemMessage("您已經完成了。"));
                    return false;
                } else {
                    boolean isCreate = true;
                    if (!(((int[]) aTempData.get(3)) == null || ((int[]) aTempData.get(4)) == null)) {
                        int[] materials = (int[]) aTempData.get(3);
                        int[] counts = (int[]) aTempData.get(4);
                        for (int j = 0; j < materials.length; j++) {
                            if (!pc.getInventory().checkItem(materials[j], (long) counts[j])) {
                                L1Item temp = ItemTable.get().getTemplate(materials[j]);
                                pc.sendPackets(new S_ServerMessage(337, String.valueOf(temp.getName()) + "(" + (((long) counts[j]) - pc.getInventory().countItems(temp.getItemId())) + ")"));
                                isCreate = false;
                            }
                        }
                        if (isCreate) {
                            for (int k = 0; k < materials.length; k++) {
                                pc.getInventory().consumeItem(materials[k], (long) counts[k]);
                            }
                        }
                    }
                    if (!(((int[]) aTempData.get(5)) == null || ((int[]) aTempData.get(6)) == null || !isCreate)) {
                        int[] giveMaterials = (int[]) aTempData.get(5);
                        int[] giveCounts = (int[]) aTempData.get(6);
                        for (int l = 0; l < giveMaterials.length; l++) {
                            L1ItemInstance item = pc.getInventory().storeItem(giveMaterials[l], (long) giveCounts[l]);
                            item.setIdentified(true);
                            pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), String.valueOf(ItemTable.get().getTemplate(item.getName()).getName()) + "(" + giveCounts[l] + ")"));
                            pc.getMap_Quest().set_step(((Integer) aTempData.get(7)).intValue(), ((Integer) aTempData.get(8)).intValue());
                            switch (((Integer) aTempData.get(9)).intValue()) {
                                case 1:
                                    World.get().broadcastPacketToAll(new S_PacketBoxGree1(2, "恭喜【" + pc.getName() + "】完成了，領取到【" + ItemTable.get().getTemplate(item.getName()).getName() + "(" + giveCounts[l] + ")】。"));
                                    World.get().broadcastPacketToAll(new S_ServerMessage("恭喜【" + pc.getName() + "】完成了，領取到【" + ItemTable.get().getTemplate(item.getName()).getName() + "(" + giveCounts[l] + ")】。"));
                                    break;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static void getData() {
        try {
            Connection con = DatabaseFactory.get().getConnection();
            ResultSet rset = con.createStatement().executeQuery("SELECT * FROM 每日地區證明系統");
            if (rset != null) {
                while (rset.next()) {
                    ArrayList<Object> aReturn = new ArrayList<>();
                    aReturn.add(0, new Integer(rset.getInt("NPC編號")));
                    aReturn.add(1, rset.getString("對話(action)"));
                    aReturn.add(2, new Integer(rset.getInt("限制等級")));
                    if (rset.getString("扣道具編號(可逗號)") == null || rset.getString("扣道具編號(可逗號)").equals("") || rset.getString("扣道具編號(可逗號)").equals("0")) {
                        aReturn.add(3, null);
                    } else {
                        aReturn.add(3, getArray(rset.getString("扣道具編號(可逗號)"), ",", 1));
                    }
                    if (rset.getString("扣道具數量(可逗號)") == null || rset.getString("扣道具數量(可逗號)").equals("") || rset.getString("扣道具數量(可逗號)").equals("0")) {
                        aReturn.add(4, null);
                    } else {
                        aReturn.add(4, getArray(rset.getString("扣道具數量(可逗號)"), ",", 1));
                    }
                    if (rset.getString("給予道具編號(可逗號)") == null || rset.getString("給予道具編號(可逗號)").equals("") || rset.getString("給予道具編號(可逗號)").equals("0")) {
                        aReturn.add(5, null);
                    } else {
                        aReturn.add(5, getArray(rset.getString("給予道具編號(可逗號)"), ",", 1));
                    }
                    if (rset.getString("給予道具數量(可逗號)") == null || rset.getString("給予道具數量(可逗號)").equals("") || rset.getString("給予道具數量(可逗號)").equals("0")) {
                        aReturn.add(6, null);
                    } else {
                        aReturn.add(6, getArray(rset.getString("給予道具數量(可逗號)"), ",", 1));
                    }
                    aReturn.add(7, new Integer(rset.getInt("任務編號")));
                    aReturn.add(8, new Integer(rset.getInt("任務進度255就可")));
                    aReturn.add(9, new Integer(rset.getInt("公告(1就可)")));
                    aData.add(aReturn);
                }
            }
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (Exception ignored) {
        }
    }

    private static Object getArray(String s, String sToken, int iType) {
        StringTokenizer st = new StringTokenizer(s, sToken);
        int iSize = st.countTokens();
        if (iType == 1) {
            int[] iReturn = new int[iSize];
            for (int i = 0; i < iSize; i++) {
                iReturn[i] = Integer.parseInt(st.nextToken());
            }
            return iReturn;
        } else if (iType == 2) {
            String[] sReturn = new String[iSize];
            for (int i2 = 0; i2 < iSize; i2++) {
                sReturn[i2] = st.nextToken();
            }
            return sReturn;
        } else if (iType != 3) {
            return null;
        } else {
            String sReturn2 = null;
            for (int i3 = 0; i3 < iSize; i3++) {
                sReturn2 = st.nextToken();
            }
            return sReturn2;
        }
    }
}
