package william;

import com.lineage.DatabaseFactory;
import com.lineage.Server;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Reward {
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

    private Reward() {
    }

    public static void getItem(L1PcInstance pc) {
        if (!GET_ITEM) {
            GET_ITEM = true;
            getItemData();
        }
        for (int i = 0; i < array.size(); i++) {
            ArrayList data = array.get(i);
            if (!(pc.getLevel() < ((Integer) data.get(0)).intValue() || ((int[]) data.get(8)) == null || ((int[]) data.get(9)) == null || ((int[]) data.get(10)) == null || pc.getQuest().get_step(((Integer) data.get(11)).intValue()) == ((Integer) data.get(12)).intValue())) {
                if (((Integer) data.get(1)).intValue() != 0 && pc.isCrown()) {
                    boolean isGet = false;
                    int[] materials = (int[]) data.get(8);
                    int[] counts = (int[]) data.get(9);
                    int[] enchantLevel = (int[]) data.get(10);
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
                            if (((String) data.get(13)) != null && !isGet) {
                                pc.sendPackets(new S_SystemMessage((String) data.get(13)));
                                isGet = true;
                            }
                            if (pc.getInventory().checkAddItem(item, (long) counts[j]) == 0) {
                                pc.getInventory().storeItem(item);
                            } else {
                                World.get().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
                            }
                            pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
                            pc.getQuest().set_step(((Integer) data.get(11)).intValue(), ((Integer) data.get(12)).intValue());
                        }
                    }
                }
                if (((Integer) data.get(2)).intValue() != 0 && pc.isKnight()) {
                    boolean isGet2 = false;
                    int[] materials2 = (int[]) data.get(8);
                    int[] counts2 = (int[]) data.get(9);
                    int[] enchantLevel2 = (int[]) data.get(10);
                    for (int j2 = 0; j2 < materials2.length; j2++) {
                        L1ItemInstance item2 = ItemTable.get().createItem(materials2[j2]);
                        item2.setIdentified(true);
                        if (item2.isStackable()) {
                            item2.setCount((long) counts2[j2]);
                        } else {
                            item2.setCount(1);
                        }
                        if (item2.getItem().getType2() == 1 || item2.getItem().getType2() == 2) {
                            item2.setEnchantLevel(enchantLevel2[j2]);
                        } else {
                            item2.setEnchantLevel(0);
                        }
                        if (item2 != null) {
                            if (((String) data.get(13)) != null && !isGet2) {
                                pc.sendPackets(new S_SystemMessage((String) data.get(13)));
                                isGet2 = true;
                            }
                            if (pc.getInventory().checkAddItem(item2, (long) counts2[j2]) == 0) {
                                pc.getInventory().storeItem(item2);
                            } else {
                                World.get().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item2);
                            }
                            pc.sendPackets(new S_ServerMessage(403, item2.getLogName()));
                            pc.getQuest().set_step(((Integer) data.get(11)).intValue(), ((Integer) data.get(12)).intValue());
                        }
                    }
                }
                if (((Integer) data.get(3)).intValue() != 0 && pc.isWizard()) {
                    boolean isGet3 = false;
                    int[] materials3 = (int[]) data.get(8);
                    int[] counts3 = (int[]) data.get(9);
                    int[] enchantLevel3 = (int[]) data.get(10);
                    for (int j3 = 0; j3 < materials3.length; j3++) {
                        L1ItemInstance item3 = ItemTable.get().createItem(materials3[j3]);
                        item3.setIdentified(true);
                        if (item3.isStackable()) {
                            item3.setCount((long) counts3[j3]);
                        } else {
                            item3.setCount(1);
                        }
                        if (item3.getItem().getType2() == 1 || item3.getItem().getType2() == 2) {
                            item3.setEnchantLevel(enchantLevel3[j3]);
                        } else {
                            item3.setEnchantLevel(0);
                        }
                        if (item3 != null) {
                            if (((String) data.get(13)) != null && !isGet3) {
                                pc.sendPackets(new S_SystemMessage((String) data.get(13)));
                                isGet3 = true;
                            }
                            if (pc.getInventory().checkAddItem(item3, (long) counts3[j3]) == 0) {
                                pc.getInventory().storeItem(item3);
                            } else {
                                World.get().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item3);
                            }
                            pc.sendPackets(new S_ServerMessage(403, item3.getLogName()));
                            pc.getQuest().set_step(((Integer) data.get(11)).intValue(), ((Integer) data.get(12)).intValue());
                        }
                    }
                }
                if (((Integer) data.get(4)).intValue() != 0 && pc.isElf()) {
                    boolean isGet4 = false;
                    int[] materials4 = (int[]) data.get(8);
                    int[] counts4 = (int[]) data.get(9);
                    int[] enchantLevel4 = (int[]) data.get(10);
                    for (int j4 = 0; j4 < materials4.length; j4++) {
                        L1ItemInstance item4 = ItemTable.get().createItem(materials4[j4]);
                        item4.setIdentified(true);
                        if (item4.isStackable()) {
                            item4.setCount((long) counts4[j4]);
                        } else {
                            item4.setCount(1);
                        }
                        if (item4.getItem().getType2() == 1 || item4.getItem().getType2() == 2) {
                            item4.setEnchantLevel(enchantLevel4[j4]);
                        } else {
                            item4.setEnchantLevel(0);
                        }
                        if (item4 != null) {
                            if (((String) data.get(13)) != null && !isGet4) {
                                pc.sendPackets(new S_SystemMessage((String) data.get(13)));
                                isGet4 = true;
                            }
                            if (pc.getInventory().checkAddItem(item4, (long) counts4[j4]) == 0) {
                                pc.getInventory().storeItem(item4);
                            } else {
                                World.get().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item4);
                            }
                            pc.sendPackets(new S_ServerMessage(403, item4.getLogName()));
                            pc.getQuest().set_step(((Integer) data.get(11)).intValue(), ((Integer) data.get(12)).intValue());
                        }
                    }
                }
                if (((Integer) data.get(5)).intValue() != 0 && pc.isDarkelf()) {
                    boolean isGet5 = false;
                    int[] materials5 = (int[]) data.get(8);
                    int[] counts5 = (int[]) data.get(9);
                    int[] enchantLevel5 = (int[]) data.get(10);
                    for (int j5 = 0; j5 < materials5.length; j5++) {
                        L1ItemInstance item5 = ItemTable.get().createItem(materials5[j5]);
                        item5.setIdentified(true);
                        if (item5.isStackable()) {
                            item5.setCount((long) counts5[j5]);
                        } else {
                            item5.setCount(1);
                        }
                        if (item5.getItem().getType2() == 1 || item5.getItem().getType2() == 2) {
                            item5.setEnchantLevel(enchantLevel5[j5]);
                        } else {
                            item5.setEnchantLevel(0);
                        }
                        if (item5 != null) {
                            if (((String) data.get(13)) != null && !isGet5) {
                                pc.sendPackets(new S_SystemMessage((String) data.get(13)));
                                isGet5 = true;
                            }
                            if (pc.getInventory().checkAddItem(item5, (long) counts5[j5]) == 0) {
                                pc.getInventory().storeItem(item5);
                            } else {
                                World.get().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item5);
                            }
                            pc.sendPackets(new S_ServerMessage(403, item5.getLogName()));
                            pc.getQuest().set_step(((Integer) data.get(11)).intValue(), ((Integer) data.get(12)).intValue());
                        }
                    }
                }
                if (((Integer) data.get(6)).intValue() != 0 && pc.isDragonKnight()) {
                    boolean isGet6 = false;
                    int[] materials6 = (int[]) data.get(8);
                    int[] counts6 = (int[]) data.get(9);
                    int[] enchantLevel6 = (int[]) data.get(10);
                    for (int j6 = 0; j6 < materials6.length; j6++) {
                        L1ItemInstance item6 = ItemTable.get().createItem(materials6[j6]);
                        item6.setIdentified(true);
                        if (item6.isStackable()) {
                            item6.setCount((long) counts6[j6]);
                        } else {
                            item6.setCount(1);
                        }
                        if (item6.getItem().getType2() == 1 || item6.getItem().getType2() == 2) {
                            item6.setEnchantLevel(enchantLevel6[j6]);
                        } else {
                            item6.setEnchantLevel(0);
                        }
                        if (item6 != null) {
                            if (((String) data.get(13)) != null && !isGet6) {
                                pc.sendPackets(new S_SystemMessage((String) data.get(13)));
                                isGet6 = true;
                            }
                            if (pc.getInventory().checkAddItem(item6, (long) counts6[j6]) == 0) {
                                pc.getInventory().storeItem(item6);
                            } else {
                                World.get().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item6);
                            }
                            pc.sendPackets(new S_ServerMessage(403, item6.getLogName()));
                            pc.getQuest().set_step(((Integer) data.get(11)).intValue(), ((Integer) data.get(12)).intValue());
                        }
                    }
                }
                if (((Integer) data.get(7)).intValue() != 0 && pc.isIllusionist()) {
                    boolean isGet7 = false;
                    int[] materials7 = (int[]) data.get(8);
                    int[] counts7 = (int[]) data.get(9);
                    int[] enchantLevel7 = (int[]) data.get(10);
                    for (int j7 = 0; j7 < materials7.length; j7++) {
                        L1ItemInstance item7 = ItemTable.get().createItem(materials7[j7]);
                        item7.setIdentified(true);
                        if (item7.isStackable()) {
                            item7.setCount((long) counts7[j7]);
                        } else {
                            item7.setCount(1);
                        }
                        if (item7.getItem().getType2() == 1 || item7.getItem().getType2() == 2) {
                            item7.setEnchantLevel(enchantLevel7[j7]);
                        } else {
                            item7.setEnchantLevel(0);
                        }
                        if (item7 != null) {
                            if (((String) data.get(13)) != null && !isGet7) {
                                pc.sendPackets(new S_SystemMessage((String) data.get(13)));
                                isGet7 = true;
                            }
                            if (pc.getInventory().checkAddItem(item7, (long) counts7[j7]) == 0) {
                                pc.getInventory().storeItem(item7);
                            } else {
                                World.get().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item7);
                            }
                            pc.sendPackets(new S_ServerMessage(403, item7.getLogName()));
                            pc.getQuest().set_step(((Integer) data.get(11)).intValue(), ((Integer) data.get(12)).intValue());
                        }
                    }
                }
            }
        }
    }

    private static void getItemData() {
        try {
            Connection con = DatabaseFactory.get().getConnection();
            ResultSet rset = con.createStatement().executeQuery("SELECT * FROM 等級獎勵系統");
            if (rset != null) {
                while (rset.next()) {
                    ArrayList<Object> arraylist = new ArrayList<>();
                    arraylist.add(0, new Integer(rset.getInt("等級限制")));
                    arraylist.add(1, new Integer(rset.getInt("王族")));
                    arraylist.add(2, new Integer(rset.getInt("騎士")));
                    arraylist.add(3, new Integer(rset.getInt("法師")));
                    arraylist.add(4, new Integer(rset.getInt("妖精")));
                    arraylist.add(5, new Integer(rset.getInt("黑妖")));
                    arraylist.add(6, new Integer(rset.getInt("龍騎士")));
                    arraylist.add(7, new Integer(rset.getInt("幻術師")));
                    arraylist.add(8, getArray(rset.getString("給於物品可逗號"), ",", 1));
                    arraylist.add(9, getArray(rset.getString("給於物品數量"), ",", 1));
                    arraylist.add(10, getArray(rset.getString("給於物品強化值"), ",", 1));
                    arraylist.add(11, new Integer(rset.getInt("任務編號")));
                    arraylist.add(12, new Integer(rset.getInt("任務進度255就可")));
                    arraylist.add(13, rset.getString("顯示對話欄內容"));
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
