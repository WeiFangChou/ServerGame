package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.lock.CharItemsTimeReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1_Box;
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
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Item_Box_Table {
    private static final Map<Integer, ArrayList<L1_Box>> _box = new HashMap();
    private static Item_Box_Table _instance;
    private static final Log _log = LogFactory.getLog(Item_Box_Table.class);
    private static final Random _random = new Random();

    public static Item_Box_Table get() {
        if (_instance == null) {
            _instance = new Item_Box_Table();
        }
        return _instance;
    }

    public void load() {
        load_box();
    }

    public void load_box() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int i = 0;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `etcitem_box_questitem`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int key = rs.getInt("box_item_id");
                L1Item temp = ItemTable.get().getTemplate(key);
                if (temp == null) {
                    del_box(key);
                } else if (temp.getType() == 9) {
                    int get_item_id = rs.getInt("get_item_id");
                    String note = rs.getString("name");
                    if (ItemTable.get().getTemplate(key) == null) {
                        del_box2(key);
                    } else {
                        L1_Box box = new L1_Box();
                        box.set_box_item_id(key);
                        box.set_get_item_id(get_item_id);
                        box.set_random(rs.getInt("random"));
                        box.set_min_count(rs.getInt("min_count"));
                        box.set_max_count(rs.getInt("max_count"));
                        box.set_remain_time(rs.getInt("時間限制(天)"));
                        box.set_out(rs.getInt("out"));
                        ArrayList<L1_Box> list = _box.get(Integer.valueOf(key));
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
        _log.info("載入袋子開出物設置: " + _box.size() + "/" + i + "(" + timer.get() + "ms)");
    }

    private static void updata_name(int key, int itemId) {
        Connection cn = null;
        PreparedStatement ps = null;
        String boxname = ItemTable.get().getTemplate(key).getName();
        String itemname = ItemTable.get().getTemplate(itemId).getName();
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE `etcitem_box_questitem` SET `name`=? WHERE `box_item_id`=? AND `get_item_id`=?");
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

    private void del_box(int id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `etcitem_box_questitem` WHERE `box_item_id`=?");
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
            ps = cn.prepareStatement("DELETE FROM `etcitem_box_questitem` WHERE `get_item_id`=?");
            ps.setInt(1, get_item_id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public ArrayList<L1_Box> get(L1PcInstance pc, L1ItemInstance tgitem) {
        try {
            ArrayList<L1_Box> list = _box.get(Integer.valueOf(tgitem.getItemId()));
            if (list != null) {
                new BoxRandom(pc, tgitem, list).getStart();
                return list;
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static void outItem(L1PcInstance pc, L1_Box box, String tgitemName) {
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
                        World.get().broadcastPacketToAll(new S_ServerMessage("恭喜玩家：【" + pc.getName() + "】開啟【" + tgitemName + "】稀有【" + itemName + "(" + item.getCount() + ")】。"));
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
        private final ArrayList<L1_Box> _list_tmp = new ArrayList<>();
        private final L1PcInstance _pc;
        private final L1ItemInstance _tgitem;

        public BoxRandom(L1PcInstance pc, L1ItemInstance tgitem, ArrayList<L1_Box> list) {
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
                    L1_Box box = null;
                    int i = 0;
                    while (box == null) {
                        box = runItem(tempList);
                        Thread.sleep(1);
                        i++;
                        if (i >= 300) {
                            Object[] obj = this._list_tmp.toArray();
                            box = (L1_Box) obj[Item_Box_Table._random.nextInt(obj.length)];
                            if (box != null) {
                                break;
                            }
                        }
                    }
                    if (box != null) {
                        String name = this._tgitem.getName();
                        if (this._pc.getInventory().removeItem(this._tgitem, 1) == 1) {
                            Item_Box_Table.outItem(this._pc, box, name);
                        }
                    }
                }
            } catch (Exception e) {
                Item_Box_Table._log.error("袋子物品設置或取率可能太低,本次開啟未獲得任何物品(袋子不會被刪除) 寶盒編號:" + this._tgitem.getItemId());
            }
        }

        private L1_Box runItem(Map<Integer, Integer> tempList) {
            try {
                if (this._list_tmp.size() <= 0) {
                    return null;
                }
                if (this._list_tmp.isEmpty()) {
                    return null;
                }
                L1_Box score = this._list_tmp.get(Item_Box_Table._random.nextInt(this._list_tmp.size()));
                int srcrandom = score.get_random();
                int random = Item_Box_Table._random.nextInt(100) + 1;
                if (random <= srcrandom) {
                    return score;
                }
                Integer tmp = tempList.get(Integer.valueOf(score.get_item_id()));
                if (tmp != null) {
                    tempList.put(Integer.valueOf(score.get_item_id()), tmp);
                } else {
                    tempList.put(Integer.valueOf(score.get_item_id()), 1);
                }
                boolean isremove = false;
                if (random <= srcrandom) {
                    isremove = true;
                }
                if (isremove) {
                    this._list_tmp.remove(score);
                }
                return null;
            } catch (Exception e) {
                Item_Box_Table._log.error(e.getLocalizedMessage(), e);
                return null;
            }
        }
    }

    public void set_box(int box_item_id, int get_item_id, String name, int randomint, int random, int min_count, int max_count) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `etcitem_box_questitem` SET `box_item_id`=?,`get_item_id`=?,`name`=?,`random`=?,`min_count`=?,`max_count`=?,`out`=?");
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
}
