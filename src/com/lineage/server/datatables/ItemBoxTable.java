package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.lock.CharItemsTimeReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Box;
import com.lineage.server.templates.L1Item;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import william.L1WilliamSystemMessage;

public class ItemBoxTable {
    private static final Map<Integer, ArrayList<L1Box>> _box = new HashMap();
    private static final Map<Integer, HashMap<Integer, ArrayList<L1Box>>> _boxkey = new HashMap();
    private static final Map<Integer, ArrayList<L1Box>> _boxs = new HashMap();
    private static ItemBoxTable _instance;
    private static final Log _log = LogFactory.getLog(ItemBoxTable.class);
    private static final Random _random = new Random();

    public static ItemBoxTable get() {
        if (_instance == null) {
            _instance = new ItemBoxTable();
        }
        return _instance;
    }

    public void load() {
        load_box();
        load_boxs();
        load_box_key();
    }

    public void load_box() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int i = 0;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `etcitem_box`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int key = rs.getInt("box_item_id");
                L1Item temp = ItemTable.get().getTemplate(key);
                if (temp == null) {
                    del_box(key);
                } else if (temp.getType() == 16) {
                    int get_item_id = rs.getInt("get_item_id");
                    String note = rs.getString("name");
                    if (ItemTable.get().getTemplate(key) == null) {
                        del_box2(key);
                    } else {
                        L1Box box = new L1Box();
                        box.set_box_item_id(key);
                        box.set_get_item_id(get_item_id);
                        box.set_random(rs.getInt("random"));
                        box.set_min_count(rs.getInt("min_count"));
                        box.set_max_count(rs.getInt("max_count"));
                        box.set_remain_time(rs.getInt("時間限制(天)"));
                        box.set_out(rs.getInt("out"));
                        ArrayList<L1Box> list = _box.get(Integer.valueOf(key));
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        list.add(box);
                        _box.put(Integer.valueOf(key), list);
                        i++;
                        if (!note.contains("=>")) {
                            updata_name(key, get_item_id);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入箱子開出物設置: " + _box.size() + "/" + i + "(" + timer.get() + "ms)");
    }

    private void del_box(int id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `etcitem_box` WHERE `box_item_id`=?");
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void del_box2(int get_item_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `etcitem_box` WHERE `get_item_id`=?");
            ps.setInt(1, get_item_id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void load_boxs() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int i = 0;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `etcitem_boxs`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int key = rs.getInt("box_item_id");
                L1Item temp = ItemTable.get().getTemplate(key);
                if (temp == null) {
                    del_boxs(key);
                } else if (temp.getType() == 16) {
                    int get_item_id = rs.getInt("get_item_id");
                    String note = rs.getString("name");
                    if (ItemTable.get().getTemplate(key) == null) {
                        del_boxs2(key);
                    } else {
                        L1Box box = new L1Box();
                        box.set_box_item_id(key);
                        box.set_get_item_id(get_item_id);
                        box.set_random(-1);
                        int count = rs.getInt("count");
                        box.set_min_count(count);
                        box.set_max_count(count);
                        box.set_out(rs.getInt("out"));
                        box.set_use_type(rs.getInt("use_type"));
                        box.set_remain_time(rs.getInt("時間限制(天)"));
                        ArrayList<L1Box> list = _boxs.get(Integer.valueOf(key));
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        list.add(box);
                        _boxs.put(Integer.valueOf(key), list);
                        i++;
                        if (!note.contains("=>")) {
                            updata_name2(key, get_item_id);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入箱子開出物設置(多種): " + _boxs.size() + "/" + i + "(" + timer.get() + "ms)");
    }

    private static void updata_name(int key, int itemId) {
        Connection cn = null;
        PreparedStatement ps = null;
        String boxname = ItemTable.get().getTemplate(key).getName();
        String itemname = ItemTable.get().getTemplate(itemId).getName();
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE `etcitem_box` SET `name`=? WHERE `box_item_id`=? AND `get_item_id`=?");
            int i = 0 + 1;
            ps.setString(i, String.valueOf(boxname) + "=>" + itemname);
            int i2 = i + 1;
            ps.setInt(i2, key);
            ps.setInt(i2 + 1, itemId);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private static void updata_name2(int key, int itemId) {
        Connection cn = null;
        PreparedStatement ps = null;
        String boxname = ItemTable.get().getTemplate(key).getName();
        String itemname = ItemTable.get().getTemplate(itemId).getName();
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE `etcitem_boxs` SET `name`=? WHERE `box_item_id`=? AND `get_item_id`=?");
            int i = 0 + 1;
            ps.setString(i, String.valueOf(boxname) + "=>" + itemname);
            int i2 = i + 1;
            ps.setInt(i2, key);
            ps.setInt(i2 + 1, itemId);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private static void updata_name3(int key, int itemId) {
        Connection cn = null;
        PreparedStatement ps = null;
        String boxname = ItemTable.get().getTemplate(key).getName();
        String itemname = ItemTable.get().getTemplate(itemId).getName();
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE `etcitem_box_key` SET `name`=? WHERE `box_item_id`=? AND `get_item_id`=?");
            int i = 0 + 1;
            ps.setString(i, String.valueOf(boxname) + "=>" + itemname);
            int i2 = i + 1;
            ps.setInt(i2, key);
            ps.setInt(i2 + 1, itemId);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void del_boxs(int id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `etcitem_boxs` WHERE `box_item_id`=?");
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void del_boxs2(int get_item_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `etcitem_boxs` WHERE `get_item_id`=?");
            ps.setInt(1, get_item_id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void load_box_key() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int i = 0;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `etcitem_box_key`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int keyId = rs.getInt("key_itemid");
                if (ItemTable.get().getTemplate(keyId) == null) {
                    del_box_key(keyId);
                } else {
                    int key = rs.getInt("box_item_id");
                    L1Item temp = ItemTable.get().getTemplate(key);
                    if (temp == null) {
                        del_box_key2(key);
                    } else if (temp.getType() == 16) {
                        int get_item_id = rs.getInt("get_item_id");
                        String note = rs.getString("name");
                        if (ItemTable.get().getTemplate(get_item_id) == null) {
                            del_box_key3(get_item_id);
                        } else {
                            L1Box box = new L1Box();
                            box.set_box_item_id(key);
                            box.set_get_item_id(get_item_id);
                            box.set_random(rs.getInt("random"));
                            box.set_min_count(rs.getInt("min_count"));
                            box.set_max_count(rs.getInt("max_count"));
                            box.set_remain_time(rs.getInt("時間限制(天)"));
                            box.set_out(rs.getInt("out"));
                            box.set_use_type(127);
                            HashMap<Integer, ArrayList<L1Box>> map = _boxkey.get(Integer.valueOf(key));
                            if (map == null) {
                                map = new HashMap<>();
                            }
                            ArrayList<L1Box> keylist = map.get(Integer.valueOf(keyId));
                            if (keylist == null) {
                                keylist = new ArrayList<>();
                            }
                            keylist.add(box);
                            map.put(Integer.valueOf(keyId), keylist);
                            _boxkey.put(Integer.valueOf(key), map);
                            i++;
                            if (!note.contains("=>")) {
                                updata_name3(key, get_item_id);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入箱子開出物設置(指定使用物品開啟): " + _boxkey.size() + "/" + i + "(" + timer.get() + "ms)");
    }

    private void del_box_key(int key_itemid) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `etcitem_box_key` WHERE `key_itemid`=?");
            ps.setInt(1, key_itemid);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void del_box_key2(int box_item_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `etcitem_box_key` WHERE `box_item_id`=?");
            ps.setInt(1, box_item_id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void del_box_key3(int get_item_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `etcitem_box_key` WHERE `get_item_id`=?");
            ps.setInt(1, get_item_id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public ArrayList<L1Box> get(L1PcInstance pc, L1ItemInstance tgitem) {
        try {
            ArrayList<L1Box> list = _box.get(Integer.valueOf(tgitem.getItemId()));
            if (list != null) {
                new BoxRandom(pc, tgitem, list).getStart();
                return list;
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public void get_all(L1PcInstance pc, L1ItemInstance tgitem) {
        try {
            ArrayList<L1Box> list = _boxs.get(Integer.valueOf(tgitem.getItemId()));
            if (list == null) {
                pc.sendPackets(new S_ServerMessage(79));
            } else if (list.size() > 0 && !list.isEmpty()) {
                String name = tgitem.getName();
                if (pc.getInventory().removeItem(tgitem, 1) == 1) {
                    Iterator<L1Box> it = list.iterator();
                    while (it.hasNext()) {
                        L1Box box = it.next();
                        if (box.is_use(pc)) {
                            outItem(pc, box, name);
                        }
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public boolean is_key(int tgid, int keyid) {
        HashMap<Integer, ArrayList<L1Box>> map = _boxkey.get(Integer.valueOf(tgid));
        if (map == null || map.get(Integer.valueOf(keyid)) == null) {
            return false;
        }
        return true;
    }

    public void get_key(L1PcInstance pc, L1ItemInstance tgitem, int keyid) {
        ArrayList<L1Box> keylist;
        try {
            HashMap<Integer, ArrayList<L1Box>> map = _boxkey.get(Integer.valueOf(tgitem.getItemId()));
            if (map != null && (keylist = map.get(Integer.valueOf(keyid))) != null && keylist.size() > 0 && !keylist.isEmpty()) {
                new BoxRandom(pc, tgitem, keylist).getStart();
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /* access modifiers changed from: private */
    public static void outItem(L1PcInstance pc, L1Box box, String tgitemName) {
        int count;
        if (box != null) {
            L1Item temp = ItemTable.get().getTemplate(box.get_item_id());
            if (box.get_min_count() < box.get_max_count()) {
                count = _random.nextInt(box.get_max_count() - box.get_min_count()) + box.get_min_count();
            } else {
                count = box.get_min_count();
            }
            int deleteDay = box.get_remain_time();
            L1ItemInstance item = null;
            if (temp.isStackable()) {
                item = ItemTable.get().createItem(box.get_item_id());
                item.setIdentified(true);
                item.setCount((long) count);
                if (deleteDay > 0) {
                    Timestamp ts = new Timestamp(System.currentTimeMillis() + ((long) (86400000 * deleteDay)));
                    item.set_time(ts);
                    CharItemsTimeReading.get().addTime(item.getId(), ts);
                }
                createNewItem(pc, item);
            } else {
                for (int i = 0; i < count; i++) {
                    item = ItemTable.get().createItem(box.get_item_id());
                    item.setIdentified(true);
                    if (deleteDay > 0) {
                        Timestamp ts2 = new Timestamp(System.currentTimeMillis() + ((long) (86400000 * deleteDay)));
                        item.set_time(ts2);
                        CharItemsTimeReading.get().addTime(item.getId(), ts2);
                    }
                    createNewItem(pc, item);
                }
            }
            if (item != null) {
                String itemName = item.getName();
                switch (box.is_out()) {
                    case 1:
                        World.get().broadcastPacketToAll(new S_ServerMessage(L1WilliamSystemMessage.ShowMessage(1) + "【" + pc.getName() + "】" + L1WilliamSystemMessage.ShowMessage(3) + "【" + tgitemName + "】" + L1WilliamSystemMessage.ShowMessage(4) + "【" + itemName + "(" + item.getCount() + ")】。"));
                        return;
                    default:
                        return;
                }
            }
        }
    }

    private static void createNewItem(L1PcInstance pc, L1ItemInstance item) {
        try {
            pc.getInventory().storeItem(item);
            pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
            pc.saveInventory();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private class BoxRandom implements Runnable {
        private final ArrayList<L1Box> _list_tmp = new ArrayList<>();
        private final L1PcInstance _pc;
        private final L1ItemInstance _tgitem;

        public BoxRandom(L1PcInstance pc, L1ItemInstance tgitem, ArrayList<L1Box> list) {
            this._pc = pc;
            this._tgitem = tgitem;
            this._list_tmp.addAll(list);
        }

        public void getStart() {
            GeneralThreadPool.get().schedule(this, 0);
        }

        public void run() {
            try {
                if (this._list_tmp.size() > 0 && !this._list_tmp.isEmpty()) {
                    Map<Integer, Integer> tempList = new HashMap<>();
                    L1Box box = null;
                    int i = 0;
                    while (box == null) {
                        box = runItem(tempList);
                        Thread.sleep(1);
                        i++;
                        if (i >= 300) {
                            Object[] obj = this._list_tmp.toArray();
                            box = (L1Box) obj[ItemBoxTable._random.nextInt(obj.length)];
                            if (box != null) {
                                break;
                            }
                        }
                    }
                    if (box != null) {
                        String name = this._tgitem.getName();
                        if (this._pc.getInventory().removeItem(this._tgitem, 1) == 1) {
                            ItemBoxTable.outItem(this._pc, box, name);
                        }
                    }
                }
            } catch (Exception e) {
                ItemBoxTable._log.error("寶盒物品設置或取率可能太低,本次開啟未獲得任何物品(寶盒不會被刪除) 寶盒編號:" + this._tgitem.getItemId());
            }
        }

        private L1Box runItem(Map<Integer, Integer> tempList) {
            try {
                if (this._list_tmp.size() <= 0) {
                    return null;
                }
                if (this._list_tmp.isEmpty()) {
                    return null;
                }
                L1Box score = this._list_tmp.get(ItemBoxTable._random.nextInt(this._list_tmp.size()));
                int random = ItemBoxTable._random.nextInt(1000000);
                int srcrandom = score.get_random();
                if (random <= srcrandom) {
                    return score;
                }
                Integer tmp = tempList.get(Integer.valueOf(score.get_item_id()));
                int x = 1;
                if (tmp != null) {
                    x = tmp.intValue() + 1;
                    tempList.put(Integer.valueOf(score.get_item_id()), Integer.valueOf(tmp.intValue() + x));
                } else {
                    tempList.put(Integer.valueOf(score.get_item_id()), 1);
                }
                boolean isremove = false;
                if (srcrandom < 5000) {
                    isremove = true;
                } else if (srcrandom < 5000 || srcrandom >= 10000) {
                    if (srcrandom < 10000 || srcrandom >= 20000) {
                        if (srcrandom < 20000 || srcrandom >= 40000) {
                            if (srcrandom < 40000 || srcrandom >= 80000) {
                                if (srcrandom < 80000 || srcrandom >= 160000) {
                                    if (srcrandom >= 160000 && srcrandom < 320000 && x > 64) {
                                        isremove = true;
                                    }
                                } else if (x > 32) {
                                    isremove = true;
                                }
                            } else if (x > 16) {
                                isremove = true;
                            }
                        } else if (x > 8) {
                            isremove = true;
                        }
                    } else if (x > 4) {
                        isremove = true;
                    }
                } else if (x > 2) {
                    isremove = true;
                }
                if (isremove) {
                    this._list_tmp.remove(score);
                }
                return null;
            } catch (Exception e) {
                ItemBoxTable._log.error(e.getLocalizedMessage(), e);
                return null;
            }
        }
    }

    public void set_box(int box_item_id, int get_item_id, String name, int randomint, int random, int min_count, int max_count) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `etcitem_box` SET `box_item_id`=?,`get_item_id`=?,`name`=?,`random`=?,`min_count`=?,`max_count`=?,`out`=?");
            int i = 0 + 1;
            ps.setInt(i, box_item_id);
            int i2 = i + 1;
            ps.setInt(i2, get_item_id);
            int i3 = i2 + 1;
            ps.setString(i3, name);
            int i4 = i3 + 1;
            ps.setInt(i4, random);
            int i5 = i4 + 1;
            ps.setInt(i5, min_count);
            int i6 = i5 + 1;
            ps.setInt(i6, max_count);
            ps.setBoolean(i6 + 1, false);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public void set_boxs(int box_item_id, int get_item_id, String name, int count) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `etcitem_boxs` SET `box_item_id`=?,`get_item_id`=?,`name`=?,`count`=?");
            int i = 0 + 1;
            ps.setInt(i, box_item_id);
            int i2 = i + 1;
            ps.setInt(i2, get_item_id);
            int i3 = i2 + 1;
            ps.setString(i3, name);
            ps.setInt(i3 + 1, count);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }
}
