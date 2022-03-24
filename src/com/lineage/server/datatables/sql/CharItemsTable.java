package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.InnKeyTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.storage.CharItemsStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CharItemsTable implements CharItemsStorage {
    private static final Map<Integer, CopyOnWriteArrayList<L1ItemInstance>> _itemList = new ConcurrentHashMap();
    private static final Log _log = LogFactory.getLog(BuddyTable.class);

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        int i = 0;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `character_items`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int objid = rs.getInt("id");
                int item_id = rs.getInt("item_id");
                int char_id = rs.getInt("char_id");
                if (CharObjidTable.get().isChar(char_id) != null) {
                    L1Item itemTemplate = ItemTable.get().getTemplate(item_id);
                    if (itemTemplate == null) {
                        errorItem(objid);
                    } else {
                        long count = rs.getLong("count");
                        int is_equipped = rs.getInt("is_equipped");
                        int enchantlvl = rs.getInt("enchantlvl");
                        int is_id = rs.getInt("is_id");
                        int durability = rs.getInt("durability");
                        int charge_count = rs.getInt("charge_count");
                        int remaining_time = rs.getInt("remaining_time");
                        Timestamp last_used = rs.getTimestamp("last_used");
                        int bless = rs.getInt("bless");
                        int attr_enchant_kind = rs.getInt("attr_enchant_kind");
                        int attr_enchant_level = rs.getInt("attr_enchant_level");
                        String gamno = rs.getString("gamno");
                        L1ItemInstance item = new L1ItemInstance();
                        item.setId(objid);
                        item.setItem(itemTemplate);
                        item.setCount(count);
                        item.setEquipped(is_equipped != 0);
                        item.setEnchantLevel(enchantlvl);
                        item.setIdentified(is_id != 0);
                        item.set_durability(durability);
                        item.setChargeCount(charge_count);
                        item.setRemainingTime(remaining_time);
                        item.setLastUsed(last_used);
                        item.setBless(bless);
                        item.setAttrEnchantKind(attr_enchant_kind);
                        item.setAttrEnchantLevel(attr_enchant_level);
                        item.setGamNo(gamno);
                        item.set_char_objid(char_id);
                        item.getLastStatus().updateAll();
                        if (item.getItem().getItemId() == 40312) {
                            InnKeyTable.checkey(item);
                        }
                        addItem(Integer.valueOf(char_id), item);
                        i++;
                    }
                } else {
                    deleteItem(Integer.valueOf(char_id));
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入人物背包物件清單資料數量: " + _itemList.size() + "/" + i + "(" + timer.get() + "ms)");
    }

    private static void errorItem(int objid) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("DELETE FROM `character_items` WHERE `id`=?");
            pstm.setInt(1, objid);
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    private static void addItem(Integer objid, L1ItemInstance item) {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(objid);
        if (list == null) {
            list = new CopyOnWriteArrayList<>();
            if (!list.contains(item)) {
                list.add(item);
            }
        } else if (!list.contains(item)) {
            list.add(item);
        }
        if (World.get().findObject(item.getId()) == null) {
            World.get().storeObject(item);
        }
        _itemList.put(objid, list);
    }

    private static void deleteItem(Integer objid) {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.remove(objid);
        if (list != null) {
            Iterator<L1ItemInstance> it = list.iterator();
            while (it.hasNext()) {
                World.get().removeObject(it.next());
            }
        }
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `character_items` WHERE `char_id`=?");
            ps.setInt(1, objid.intValue());
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public CopyOnWriteArrayList<L1ItemInstance> loadItems(Integer objid) {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(objid);
        if (list != null) {
            return list;
        }
        return null;
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void delUserItems(Integer objid) {
        deleteItem(objid);
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public boolean getUserItems(Integer pcObjid, int objid, long count) {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(pcObjid);
        if (list != null) {
            Iterator<L1ItemInstance> it = list.iterator();
            while (it.hasNext()) {
                L1ItemInstance item = it.next();
                if (item.getId() == objid && item.getCount() >= count) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public boolean getUserItem(int objid) {
        for (CopyOnWriteArrayList<L1ItemInstance> list : _itemList.values()) {
            Iterator<L1ItemInstance> it = list.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (it.next().getId() == objid) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public Map<Integer, L1ItemInstance> getUserItems(int itemid) {
        Map<Integer, L1ItemInstance> outList = new ConcurrentHashMap<>();
        try {
            for (Integer key : _itemList.keySet()) {
                Iterator<L1ItemInstance> it = _itemList.get(key).iterator();
                while (it.hasNext()) {
                    L1ItemInstance item = it.next();
                    if (item.getItemId() == itemid) {
                        outList.put(key, item);
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return outList;
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void del_item(int itemid) {
        try {
            for (Integer key : _itemList.keySet()) {
                Iterator<L1ItemInstance> it = _itemList.get(key).iterator();
                while (it.hasNext()) {
                    L1ItemInstance item = it.next();
                    if (item.getItemId() == itemid) {
                        deleteItem(key.intValue(), item);
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void storeItem(int objId, L1ItemInstance item) throws Exception {
        int i;
        int i2 = 1;
        addItem(Integer.valueOf(objId), item);
        item.getLastStatus().updateAll();
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("INSERT INTO `character_items` SET `id`=?,`item_id`=?,`char_id`=?,`item_name`=?,`count`=?,`is_equipped`=?,`enchantlvl`=?,`is_id`=?,`durability`=?,`charge_count`=?,`remaining_time`=?,`last_used`=?,`bless`=?,`attr_enchant_kind`=?,`attr_enchant_level`=?,`gamno`=?");
            int i3 = 0 + 1;
            pstm.setInt(i3, item.getId());
            int i4 = i3 + 1;
            pstm.setInt(i4, item.getItem().getItemId());
            int i5 = i4 + 1;
            pstm.setInt(i5, objId);
            int i6 = i5 + 1;
            pstm.setString(i6, item.getItem().getName());
            int i7 = i6 + 1;
            pstm.setLong(i7, item.getCount());
            int i8 = i7 + 1;
            if (item.isEquipped()) {
                i = 1;
            } else {
                i = 0;
            }
            pstm.setInt(i8, i);
            int i9 = i8 + 1;
            pstm.setInt(i9, item.getEnchantLevel());
            int i10 = i9 + 1;
            if (!item.isIdentified()) {
                i2 = 0;
            }
            pstm.setInt(i10, i2);
            int i11 = i10 + 1;
            pstm.setInt(i11, item.get_durability());
            int i12 = i11 + 1;
            pstm.setInt(i12, item.getChargeCount());
            int i13 = i12 + 1;
            pstm.setInt(i13, item.getRemainingTime());
            int i14 = i13 + 1;
            pstm.setTimestamp(i14, item.getLastUsed());
            int i15 = i14 + 1;
            pstm.setInt(i15, item.getBless());
            int i16 = i15 + 1;
            pstm.setInt(i16, item.getAttrEnchantKind());
            int i17 = i16 + 1;
            pstm.setInt(i17, item.getAttrEnchantLevel());
            pstm.setString(i17 + 1, item.getGamNo());
            pstm.execute();
        } catch (SQLException e) {
            _log.error("背包物品增加時發生異常 人物OBJID:" + objId, e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void deleteItem(int objid, L1ItemInstance item) throws Exception {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(Integer.valueOf(objid));
        if (list != null) {
            list.remove(item);
            Connection cn = null;
            PreparedStatement ps = null;
            try {
                cn = DatabaseFactory.get().getConnection();
                ps = cn.prepareStatement("DELETE FROM `character_items` WHERE `id`=?");
                ps.setInt(1, item.getId());
                ps.execute();
            } catch (SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(ps);
                SQLUtil.close(cn);
            }
        }
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void updateItemId_Name(L1ItemInstance item) throws Exception {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("UPDATE `character_items` SET `item_id`=?,`item_name`=?,`bless`=? WHERE `id`=?");
            pstm.setInt(1, item.getItemId());
            pstm.setString(2, item.getItem().getName());
            pstm.setInt(3, item.getItem().getBless());
            pstm.setInt(4, item.getId());
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void updateItemId(L1ItemInstance item) throws Exception {
        executeUpdate(item.getId(), "UPDATE `character_items` SET `item_id`=? WHERE `id`=?", (long) item.getItemId());
        item.getLastStatus().updateItemId();
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void updateItemCount(L1ItemInstance item) throws Exception {
        executeUpdate(item.getId(), "UPDATE `character_items` SET `count`=? WHERE `id`=?", item.getCount());
        item.getLastStatus().updateCount();
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void updateItemDurability(L1ItemInstance item) throws Exception {
        executeUpdate(item.getId(), "UPDATE `character_items` SET `durability`=? WHERE `id`=?", (long) item.get_durability());
        item.getLastStatus().updateDuraility();
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void updateItemChargeCount(L1ItemInstance item) throws Exception {
        executeUpdate(item.getId(), "UPDATE `character_items` SET `charge_count`=? WHERE `id`=?", (long) item.getChargeCount());
        item.getLastStatus().updateChargeCount();
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void updateItemRemainingTime(L1ItemInstance item) throws Exception {
        executeUpdate(item.getId(), "UPDATE `character_items` SET `remaining_time`=? WHERE `id`=?", (long) item.getRemainingTime());
        item.getLastStatus().updateRemainingTime();
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void updateItemEnchantLevel(L1ItemInstance item) throws Exception {
        executeUpdate(item.getId(), "UPDATE `character_items` SET `enchantlvl`=? WHERE `id`=?", (long) item.getEnchantLevel());
        item.getLastStatus().updateEnchantLevel();
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void updateItemEquipped(L1ItemInstance item) throws Exception {
        executeUpdate(item.getId(), "UPDATE `character_items` SET `is_equipped`=? WHERE `id`=?", (long) (item.isEquipped() ? 1 : 0));
        item.getLastStatus().updateEquipped();
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void updateItemIdentified(L1ItemInstance item) throws Exception {
        executeUpdate(item.getId(), "UPDATE `character_items` SET `is_id`=? WHERE `id`=?", (long) (item.isIdentified() ? 1 : 0));
        item.getLastStatus().updateIdentified();
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void updateItemBless(L1ItemInstance item) throws Exception {
        executeUpdate(item.getId(), "UPDATE `character_items` SET `bless`=? WHERE `id`=?", (long) item.getBless());
        item.getLastStatus().updateBless();
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void updateItemAttrEnchantKind(L1ItemInstance item) throws Exception {
        executeUpdate(item.getId(), "UPDATE `character_items` SET `attr_enchant_kind`=? WHERE `id`=?", (long) item.getAttrEnchantKind());
        item.getLastStatus().updateAttrEnchantKind();
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void updateItemAttrEnchantLevel(L1ItemInstance item) throws Exception {
        executeUpdate(item.getId(), "UPDATE `character_items` SET `attr_enchant_level`=? WHERE `id`=?", (long) item.getAttrEnchantLevel());
        item.getLastStatus().updateAttrEnchantLevel();
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void updateItemDelayEffect(L1ItemInstance item) throws Exception {
        executeUpdate(item.getId(), "UPDATE `character_items` SET `last_used`=? WHERE `id`=?", item.getLastUsed());
        item.getLastStatus().updateLastUsed();
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public int getItemCount(int objId) throws Exception {
        int count = 0;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `character_items` WHERE `char_id`=?");
            pstm.setInt(1, objId);
            rs = pstm.executeQuery();
            while (rs.next()) {
                count++;
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return count;
    }

    @Override // com.lineage.server.datatables.storage.CharItemsStorage
    public void getAdenaCount(int objid, long count) throws Exception {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(Integer.valueOf(objid));
        if (list != null) {
            boolean isAdena = false;
            Iterator<L1ItemInstance> it = list.iterator();
            while (it.hasNext()) {
                L1ItemInstance item = it.next();
                if (item.getItemId() == 40308) {
                    item.setCount(item.getCount() + count);
                    updateItemCount(item);
                    isAdena = true;
                }
            }
            if (!isAdena) {
                L1ItemInstance item2 = ItemTable.get().createItem(L1ItemId.ADENA);
                item2.setCount(count);
                storeItem(objid, item2);
            }
        }
    }

    private void executeUpdate(int objId, String sql, long updateNum) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement(sql.toString());
            pstm.setLong(1, updateNum);
            pstm.setInt(2, objId);
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    private static void executeUpdate(int objId, String sql, Timestamp ts) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement(sql.toString());
            pstm.setTimestamp(1, ts);
            pstm.setInt(2, objId);
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
