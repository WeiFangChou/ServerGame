package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.storage.DwarfShopStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ShopS;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DwarfShopTable implements DwarfShopStorage {
    private static int _id = 0;
    private static final Map<Integer, L1ItemInstance> _itemList = new ConcurrentHashMap();
    private static final Log _log = LogFactory.getLog(DwarfShopTable.class);
    private static final Map<Integer, L1ShopS> _shopSList = new ConcurrentHashMap();

    @Override // com.lineage.server.datatables.storage.DwarfShopStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `character_shopinfo`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int objid = rs.getInt("id");
                L1Item itemTemplate = ItemTable.get().getTemplate(rs.getInt("item_id"));
                if (itemTemplate == null) {
                    errorItem(objid);
                } else {
                    long count = rs.getLong("count");
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
                    item.setEquipped(false);
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
                    addItem(objid, item);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(co);
            loadShopS();
        }
        _log.info("載入託售道具資料數量: " + _itemList.size() + "/" + _shopSList.size() + "(" + timer.get() + "ms)");
    }

    private static void addItem(int key, L1ItemInstance value) {
        if (_itemList.get(Integer.valueOf(key)) == null) {
            _itemList.put(Integer.valueOf(key), value);
        }
        if (World.get().findObject(key) == null) {
            World.get().storeObject(value);
        }
    }

    @Override // com.lineage.server.datatables.storage.DwarfShopStorage
    public int get_id() {
        return _id;
    }

    @Override // com.lineage.server.datatables.storage.DwarfShopStorage
    public void set_id(int id) {
        _id = id;
    }

    private static void loadShopS() {
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `character_shop`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int item_obj_id = rs.getInt("item_obj_id");
                int user_obj_id = rs.getInt("user_obj_id");
                int adena = rs.getInt("adena");
                Timestamp overtime = rs.getTimestamp("overtime");
                int end = rs.getInt("end");
                String none = rs.getString("none");
                if (_id < id) {
                    _id = id;
                }
                L1ShopS shopS = new L1ShopS();
                shopS.set_id(id);
                shopS.set_item_obj_id(item_obj_id);
                shopS.set_user_obj_id(user_obj_id);
                shopS.set_adena(adena);
                shopS.set_overtime(overtime);
                shopS.set_end(end);
                shopS.set_none(none);
                switch (end) {
                    case 0:
                    case 1:
                    case 3:
                        shopS.set_item(_itemList.get(Integer.valueOf(item_obj_id)));
                        break;
                    case 2:
                    case 4:
                        shopS.set_item(null);
                        break;
                }
                userMap(id, shopS);
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    private static void userMap(int key, L1ShopS value) {
        if (_shopSList.get(Integer.valueOf(key)) == null) {
            _shopSList.put(Integer.valueOf(key), value);
        }
    }

    private static void errorItem(int objid) {
        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("DELETE FROM `character_shopinfo` WHERE `id`=?");
            ps.setInt(1, objid);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    @Override // com.lineage.server.datatables.storage.DwarfShopStorage
    public HashMap<Integer, L1ShopS> allShopS() {
        HashMap<Integer, L1ShopS> shopSList = new HashMap<>();
        for (L1ShopS value : _shopSList.values()) {
            if (value.get_end() == 0) {
                shopSList.put(Integer.valueOf(value.get_id()), value);
            }
        }
        return shopSList;
    }

    @Override // com.lineage.server.datatables.storage.DwarfShopStorage
    public Map<Integer, L1ItemInstance> allItems() {
        return _itemList;
    }

    @Override // com.lineage.server.datatables.storage.DwarfShopStorage
    public L1ShopS getShopS(int objid) {
        L1ShopS out = null;
        int i = 0;
        for (L1ShopS value : _shopSList.values()) {
            if (value.get_end() == 0 && value.get_item_obj_id() == objid) {
                out = value;
                i++;
            }
        }
        if (i > 1) {
            _log.error("取回託售物品資料異常-未售出物品OBJID重複:" + objid);
        }
        return out;
    }

    @Override // com.lineage.server.datatables.storage.DwarfShopStorage
    public HashMap<Integer, L1ShopS> getShopSMap(int pcobjid) {
        HashMap<Integer, L1ShopS> shopSMap = new HashMap<>();
        int index = 0;
        for (int i = _shopSList.size() + 1; i > 0; i--) {
            L1ShopS value = _shopSList.get(Integer.valueOf(i));
            if (value != null && value.get_user_obj_id() == pcobjid) {
                shopSMap.put(Integer.valueOf(index), value);
                index++;
            }
        }
        if (shopSMap.size() > 0) {
            return shopSMap;
        }
        return null;
    }

    @Override // com.lineage.server.datatables.storage.DwarfShopStorage
    public void insertItem(int key, L1ItemInstance item, L1ShopS shopS) {
        addItem(key, item);
        set_userMap(shopS.get_id(), shopS);
        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `character_shopinfo` SET `id`=?,`item_id`= ?,`item_name`=?,`count`=?,`enchantlvl`=?,`is_id`=?,`durability`=?,`charge_count`=?,`remaining_time`=?,`last_used`=?,`bless`=?,`attr_enchant_kind`=?,`attr_enchant_level`=?,`gamno`=?");
            int i = 0 + 1;
            ps.setInt(i, item.getId());
            int i2 = i + 1;
            ps.setInt(i2, item.getItemId());
            int i3 = i2 + 1;
            ps.setString(i3, item.getItem().getName());
            int i4 = i3 + 1;
            ps.setLong(i4, item.getCount());
            int i5 = i4 + 1;
            ps.setInt(i5, item.getEnchantLevel());
            int i6 = i5 + 1;
            ps.setInt(i6, item.isIdentified() ? 1 : 0);
            int i7 = i6 + 1;
            ps.setInt(i7, item.get_durability());
            int i8 = i7 + 1;
            ps.setInt(i8, item.getChargeCount());
            int i9 = i8 + 1;
            ps.setInt(i9, item.getRemainingTime());
            int i10 = i9 + 1;
            ps.setTimestamp(i10, item.getLastUsed());
            int i11 = i10 + 1;
            ps.setInt(i11, item.getBless());
            int i12 = i11 + 1;
            ps.setInt(i12, item.getAttrEnchantKind());
            int i13 = i12 + 1;
            ps.setInt(i13, item.getAttrEnchantLevel());
            ps.setString(i13 + 1, item.getGamNo());
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    private void set_userMap(int getId, L1ShopS shopS) {
        userMap(shopS.get_id(), shopS);
        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `character_shop` SET `id`=?,`item_obj_id`= ?,`user_obj_id`=?,`adena`=?,`overtime`=?,`end`=?,`none`=?");
            int i = 0 + 1;
            ps.setInt(i, shopS.get_id());
            int i2 = i + 1;
            ps.setInt(i2, shopS.get_item_obj_id());
            int i3 = i2 + 1;
            ps.setInt(i3, shopS.get_user_obj_id());
            int i4 = i3 + 1;
            ps.setInt(i4, shopS.get_adena());
            int i5 = i4 + 1;
            ps.setTimestamp(i5, shopS.get_overtime());
            int i6 = i5 + 1;
            ps.setInt(i6, shopS.get_end());
            ps.setString(i6 + 1, shopS.get_none());
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    @Override // com.lineage.server.datatables.storage.DwarfShopStorage
    public void updateShopS(L1ShopS shopS) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("UPDATE `character_shop` SET `end`=? WHERE `id`=?");
            pstm.setLong(1, (long) shopS.get_end());
            pstm.setInt(2, shopS.get_id());
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.DwarfShopStorage
    public void deleteItem(int key) {
        if (_itemList.get(Integer.valueOf(key)) != null) {
            _itemList.remove(Integer.valueOf(key));
            errorItem(key);
        }
    }
}
