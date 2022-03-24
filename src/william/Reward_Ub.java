package william;

import com.lineage.DatabaseFactory;
import com.lineage.Server;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.UBTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1UltimateBattle;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Reward_Ub {
    private static boolean GET_ITEM = false;
    public static final String TOKEN = ",";
    private static ArrayList<ArrayList<Object>> array = new ArrayList<>();

    public static void main(String[] a) {
        while (true) {
            try {
                Server.main(null);
            } catch (Exception ignored) {
            }
        }
    }

    private Reward_Ub() {
    }

    public static void getItem(L1PcInstance pc, int Id, long count) {
        if (!GET_ITEM) {
            GET_ITEM = true;
            getItemData();
        }
        int id = 0;
        L1UltimateBattle ub = UBTable.getInstance().getUb(Id);
        if (ub != null) {
            id = ub.getUbId();
        }
        for (int i = 0; i < array.size(); i++) {
            ArrayList<?> data = array.get(i);
            if (id == ((Integer) data.get(0)).intValue()) {
                int[] materials = (int[]) data.get(1);
                int[] counts = (int[]) data.get(2);
                int[] enchantLevel = (int[]) data.get(3);
                for (int j = 0; j < materials.length; j++) {
                    L1ItemInstance item = ItemTable.get().createItem(materials[j]);
                    item.setIdentified(true);
                    if (item.isStackable()) {
                        item.setCount((long) counts[j]);
                    } else {
                        item.setCount(1);
                    }
                    if (item.getItem().getType2() == 1 || item.getItem().getType2() == 2) {
                        item.setEnchantLevel(enchantLevel[j]);
                    } else {
                        item.setEnchantLevel(0);
                    }
                    if (item != null) {
                        if (pc.getInventory().checkAddItem(item, (long) counts[j]) == 0) {
                            pc.getInventory().storeItem(item);
                        } else {
                            World.get().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
                        }
                        pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
                    }
                }
            }
        }
    }

    private static void getItemData() {
        try {
            Connection con = DatabaseFactory.get().getConnection();
            ResultSet rset = con.createStatement().executeQuery("SELECT * FROM 競技場獎勵系統");
            if (rset != null) {
                while (rset.next()) {
                    ArrayList<Object> arraylist = new ArrayList<>();
                    arraylist.add(0, new Integer(rset.getInt("競技場編號")));
                    arraylist.add(1, getArray(rset.getString("給於物品可逗號"), ",", 1));
                    arraylist.add(2, getArray(rset.getString("給於物品數量"), ",", 1));
                    arraylist.add(3, getArray(rset.getString("給於物品強化值"), ",", 1));
                    array.add(arraylist);
                }
            }
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ignored) {
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
