package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.InnKeyTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.datatables.storage.DwarfForElfStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
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

public class DwarfForElfTable implements DwarfForElfStorage {
    private static final Map<String, CopyOnWriteArrayList<L1ItemInstance>> _itemList = new ConcurrentHashMap();
    private static final Log _log = LogFactory.getLog(DwarfForElfTable.class);

    @Override // com.lineage.server.datatables.storage.DwarfForElfStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        int i = 0;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `character_elf_warehouse` order by account_name, item_id, id");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int objid = rs.getInt("id");
                String account_name = rs.getString("account_name").toLowerCase();
                if (AccountReading.get().isAccountUT(account_name)) {
                    int item_id = rs.getInt("item_id");
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
                    L1Item itemTemplate = ItemTable.get().getTemplate(item_id);
                    if (itemTemplate == null) {
                        errorItem(objid);
                    } else {
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
                        if (item.getItem().getItemId() == 40312) {
                            InnKeyTable.checkey(item);
                        }
                        addItem(account_name, item);
                        i++;
                    }
                } else {
                    deleteItem(account_name);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入精靈倉庫物件清單資料數量: " + _itemList.size() + "/" + i + "(" + timer.get() + "ms)");
    }

    private static void errorItem(int objid) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("DELETE FROM `character_elf_warehouse` WHERE `id`=?");
            pstm.setInt(1, objid);
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    private static void addItem(String account_name, L1ItemInstance item) {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(account_name);
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
        _itemList.put(account_name, list);
    }

    private static void deleteItem(String account_name) {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.remove(account_name);
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
            ps = cn.prepareStatement("DELETE FROM `character_elf_warehouse` WHERE `account_name`=?");
            ps.setString(1, account_name);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    @Override // com.lineage.server.datatables.storage.DwarfForElfStorage
    public CopyOnWriteArrayList<L1ItemInstance> loadItems(String account_name) {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(account_name);
        if (list != null) {
            return list;
        }
        return null;
    }

    @Override // com.lineage.server.datatables.storage.DwarfForElfStorage
    public void delUserItems(String account_name) {
        deleteItem(account_name);
    }

    @Override // com.lineage.server.datatables.storage.DwarfForElfStorage
    public boolean getUserItems(String account_name, int objid, int count) {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(account_name);
        if (list == null || list.size() <= 0) {
            return false;
        }
        Iterator<L1ItemInstance> it = list.iterator();
        while (it.hasNext()) {
            L1ItemInstance item = it.next();
            if (item.getId() == objid && item.getCount() >= ((long) count)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.lineage.server.datatables.storage.DwarfForElfStorage
    public void insertItem(String account_name, L1ItemInstance item) {
        addItem(account_name, item);
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("INSERT INTO `character_elf_warehouse` SET `id`=?,`account_name`=?,`item_id`= ?,`item_name`=?,`count`=?,`is_equipped`=0,`enchantlvl`=?,`is_id`=?,`durability`=?,`charge_count`=?,`remaining_time`=?,`last_used`=?,`bless`=?,`attr_enchant_kind`=?,`attr_enchant_level`=?,`gamno`=?");
            int i = 0 + 1;
            pstm.setInt(i, item.getId());
            int i2 = i + 1;
            pstm.setString(i2, account_name);
            int i3 = i2 + 1;
            pstm.setInt(i3, item.getItemId());
            int i4 = i3 + 1;
            pstm.setString(i4, item.getItem().getName());
            int i5 = i4 + 1;
            pstm.setLong(i5, item.getCount());
            int i6 = i5 + 1;
            pstm.setInt(i6, item.getEnchantLevel());
            int i7 = i6 + 1;
            pstm.setInt(i7, item.isIdentified() ? 1 : 0);
            int i8 = i7 + 1;
            pstm.setInt(i8, item.get_durability());
            int i9 = i8 + 1;
            pstm.setInt(i9, item.getChargeCount());
            int i10 = i9 + 1;
            pstm.setInt(i10, item.getRemainingTime());
            int i11 = i10 + 1;
            pstm.setTimestamp(i11, item.getLastUsed());
            int i12 = i11 + 1;
            pstm.setInt(i12, item.getBless());
            int i13 = i12 + 1;
            pstm.setInt(i13, item.getAttrEnchantKind());
            int i14 = i13 + 1;
            pstm.setInt(i14, item.getAttrEnchantLevel());
            pstm.setString(i14 + 1, item.getGamNo());
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.DwarfForElfStorage
    public void updateItem(L1ItemInstance item) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("UPDATE `character_elf_warehouse` SET `count`=? WHERE `id`=?");
            pstm.setLong(1, item.getCount());
            pstm.setInt(2, item.getId());
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.DwarfForElfStorage
    public void deleteItem(String account_name, L1ItemInstance item) {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(account_name);
        if (list != null) {
            list.remove(item);
            Connection con = null;
            PreparedStatement pstm = null;
            try {
                con = DatabaseFactory.get().getConnection();
                pstm = con.prepareStatement("DELETE FROM `character_elf_warehouse` WHERE `id`=?");
                pstm.setInt(1, item.getId());
                pstm.execute();
            } catch (SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(pstm);
                SQLUtil.close(con);
            }
        }
    }
}
