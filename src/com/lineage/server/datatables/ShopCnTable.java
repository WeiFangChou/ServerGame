package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1ShopItem;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ShopCnTable {
    private static final ArrayList<Integer> _CnitemidList = new ArrayList<>();
    public static final Map<Integer, Integer> _DailyCnItem = new HashMap();
    private static final Map<Integer, Integer> _allCnShopItem = new HashMap();
    private static ShopCnTable _instance;
    private static final Log _log = LogFactory.getLog(ShopCnTable.class);
    private static final Map<Integer, ArrayList<L1ShopItem>> _shopList = new HashMap();

    public static ShopCnTable get() {
        if (_instance == null) {
            _instance = new ShopCnTable();
        }
        return _instance;
    }

    public void restshopCn() {
        _shopList.clear();
        _DailyCnItem.clear();
        load();
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `shop_cn` ORDER BY `item_id`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int npcId = rs.getInt("npc_id");
                int itemId = rs.getInt("item_id");
                String note = rs.getString("note");
                if (ItemTable.get().getTemplate(itemId) == null) {
                    _log.error("特殊商店販賣資料錯誤: 沒有這個編號的道具:" + itemId + " 對應NPC編號:" + npcId);
                    delete(npcId, itemId);
                } else {
                    int dailybuyingCount = rs.getInt("dailybuying_count");
                    if (dailybuyingCount > 0) {
                        _DailyCnItem.put(Integer.valueOf(itemId), Integer.valueOf(dailybuyingCount));
                    }
                    addShopItem(npcId, new L1ShopItem(id, itemId, rs.getInt("selling_price"), rs.getInt("pack_count"), rs.getInt("enchant_level"), dailybuyingCount, rs.getInt("level"), rs.getInt("時間限制(天)")));
                    _CnitemidList.add(Integer.valueOf(itemId));
                    if (!note.contains("=>")) {
                        updata_name(npcId, itemId);
                    }
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs, ps, cn);
        }
        _log.info("載入商城販賣物品資料數量: " + _shopList.size() + "(" + timer.get() + "ms)");
    }

    public int getPrice(int itemid) {
        Integer price = _allCnShopItem.get(new Integer(itemid));
        if (price != null) {
            return price.intValue();
        }
        return 0;
    }

    public ArrayList<Integer> get_cnitemidlist() {
        return _CnitemidList;
    }

    private static void updata_name(int npcId, int itemId) {
        Connection cn = null;
        PreparedStatement ps = null;
        String npcname = NpcTable.get().getNpcName(npcId);
        String itemname = ItemTable.get().getTemplate(itemId).getName();
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE `shop_cn` SET `note`=? WHERE `npc_id`=? AND `item_id`=?");
            int i = 0 + 1;
            ps.setString(i, String.valueOf(npcname) + "=>" + itemname);
            int i2 = i + 1;
            ps.setInt(i2, npcId);
            ps.setInt(i2 + 1, itemId);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private static void delete(int npc_id, int item_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `shop_cn` WHERE `npc_id`=? AND `item_id`=?");
            ps.setInt(1, npc_id);
            ps.setInt(2, item_id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void addShopItem(int npcId, L1ShopItem item) {
        ArrayList<L1ShopItem> list = _shopList.get(new Integer(npcId));
        if (list == null) {
            ArrayList<L1ShopItem> list2 = new ArrayList<>();
            list2.add(item);
            _shopList.put(Integer.valueOf(npcId), list2);
            return;
        }
        list.add(item);
    }

    public ArrayList<L1ShopItem> get(int npcId) {
        ArrayList<L1ShopItem> list = _shopList.get(new Integer(npcId));
        if (list != null) {
            return list;
        }
        return null;
    }

    public L1ShopItem getTemp(int npcId, int id) {
        ArrayList<L1ShopItem> list = _shopList.get(new Integer(npcId));
        if (list != null) {
            Iterator<L1ShopItem> it = list.iterator();
            while (it.hasNext()) {
                L1ShopItem shopItem = it.next();
                if (shopItem.getId() == id) {
                    return shopItem;
                }
            }
        }
        return null;
    }

    public boolean isSelling(int npcId, int itemid) {
        ArrayList<L1ShopItem> list = _shopList.get(new Integer(npcId));
        if (list != null) {
            Iterator<L1ShopItem> it = list.iterator();
            while (it.hasNext()) {
                if (it.next().getItemId() == itemid) {
                    return true;
                }
            }
        }
        return false;
    }
}
