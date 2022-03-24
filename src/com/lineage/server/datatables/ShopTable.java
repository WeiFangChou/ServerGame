package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.shop.L1Shop;
import com.lineage.server.templates.L1Item;
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
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ShopTable {
    public static final Map<Integer, Integer> _DailyItem = new HashMap();
    private static final Map<Integer, Integer> _allShopItem = new HashMap();
    private static final Map<Integer, L1Shop> _allShops = new HashMap();
    private static ShopTable _instance;
    private static final Log _log = LogFactory.getLog(ShopTable.class);
    private static final Map<Integer, Integer> _noBuyList = new HashMap();

    public static ShopTable get() {
        if (_instance == null) {
            _instance = new ShopTable();
        }
        return _instance;
    }

    public void restshop() {
        _allShops.clear();
        _allShopItem.clear();
        _noBuyList.clear();
        _DailyItem.clear();
        load();
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `shop` WHERE `npc_id`=? ORDER BY `item_id`,`order_id`");
            Iterator<Integer> it = enumNpcIds().iterator();
            while (it.hasNext()) {
                int npcId = it.next().intValue();
                ps.setInt(1, npcId);
                rs = ps.executeQuery();
                _allShops.put(Integer.valueOf(npcId), loadShop(npcId, rs));
                rs.close();
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs, ps, cn);
        }
        _log.info("載入商店販賣資料數量: " + _allShops.size() + "(" + timer.get() + "ms)");
        checkA();
    }

    private void checkA() {
        int old_all_size = _allShopItem.size();
        int old_nobuy_size = _noBuyList.size();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `shop_rates`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                if (ItemTable.get().getTemplate(itemId) == null) {
                    _log.error("回收物品資料錯誤: 沒有這個編號的道具:" + itemId);
                    delete1(itemId);
                } else {
                    int purchasingPrice = rs.getInt("purchasing_price");
                    if (!rs.getString("note").contains("→")) {
                        updata_name1(itemId, purchasingPrice);
                    }
                    if (purchasingPrice > 0) {
                        if (!_allShopItem.containsKey(Integer.valueOf(itemId))) {
                            _allShopItem.put(new Integer(itemId), new Integer(purchasingPrice));
                        }
                    } else if (!_noBuyList.containsKey(Integer.valueOf(itemId))) {
                        _noBuyList.put(new Integer(itemId), new Integer(purchasingPrice));
                    }
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        SQLUtil.close(rs, ps, cn);
        _log.info("載入額外販賣資料數量: 回收物補:" + (_allShopItem.size() - old_all_size) + "筆 /不可回補:" + (_noBuyList.size() - old_nobuy_size) + "筆");
    }

    private static ArrayList<Integer> enumNpcIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT DISTINCT `npc_id` FROM `shop`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                ids.add(Integer.valueOf(rs.getInt("npc_id")));
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        return ids;
    }

    private static L1Shop loadShop(int npcId, ResultSet rs) throws SQLException {
        List<L1ShopItem> sellingList = new ArrayList<>();
        List<L1ShopItem> purchasingList = new ArrayList<>();
        while (rs.next()) {
            int itemId = rs.getInt("item_id");
            if (ItemTable.get().getTemplate(itemId) == null) {
                _log.error("商店販賣資料錯誤: 沒有這個編號的道具:" + itemId + " 對應NPC編號:" + npcId);
                delete(npcId, itemId);
            } else {
                int sellingPrice = rs.getInt("selling_price");
                int purchasingPrice = rs.getInt("purchasing_price");
                int packCount = rs.getInt("pack_count");
                String note = rs.getString("note");
                int enchantlevel = rs.getInt("強化值");
                int level = rs.getInt("每日限制等級");
                int dailybuyingCount = rs.getInt("每日限制數量");
                int remain_time = rs.getInt("時間限制(天)");
                if (dailybuyingCount > 0) {
                    _DailyItem.put(Integer.valueOf(itemId), Integer.valueOf(dailybuyingCount));
                }
                Connection conI = null;
                PreparedStatement pstmI = null;
                ResultSet rsI = null;
                try {
                    conI = DatabaseFactory.get().getConnection();
                    pstmI = conI.prepareStatement("SELECT * FROM shop WHERE item_id='" + itemId + "' order by order_id ");
                    rsI = pstmI.executeQuery();
                    while (rsI.next()) {
                        if (rsI.getInt("selling_price") >= 1 && purchasingPrice > rsI.getInt("selling_price")) {
                            System.out.println("偵測到買低賣高錯誤!!! NpcId=" + rsI.getInt("npc_id") + ", ItemID=" + itemId + ", 價格錯誤!!!");
                            purchasingPrice = -1;
                        }
                    }
                    rsI.close();
                } catch (SQLException e) {
                    _log.error(e.getLocalizedMessage(), e);
                } finally {
                    SQLUtil.close(rsI, pstmI, conI);
                }
                if (!note.contains("→")) {
                    updata_name(npcId, itemId, sellingPrice);
                }
                if (packCount == 0) {
                    packCount = 1;
                }
                if (sellingPrice >= 0) {
                    sellingList.add(new L1ShopItem(itemId, sellingPrice, packCount, enchantlevel, dailybuyingCount, level, remain_time));
                }
                if (purchasingPrice >= 0) {
                    purchasingList.add(new L1ShopItem(itemId, purchasingPrice, packCount, enchantlevel, dailybuyingCount, level, remain_time));
                    addSellList(itemId, sellingPrice, purchasingPrice, packCount);
                }
            }
        }
        if (!sellingList.isEmpty() && !purchasingList.isEmpty()) {
            for (int i = 0; i < purchasingList.size(); i++) {
                L1ShopItem purchasing_item = purchasingList.get(i);
                if (purchasing_item.getPrice() > 0) {
                    int purchasing_itemid = purchasing_item.getItemId();
                    for (int s = 0; s < sellingList.size(); s++) {
                        L1ShopItem selling_item = sellingList.get(i);
                        if (selling_item.getPrice() > 0 && purchasing_itemid == selling_item.getItemId() && purchasing_item.getPrice() > selling_item.getPrice()) {
                            _log.error("商店出現洗幣物品->道具:" + purchasing_itemid);
                        }
                    }
                }
            }
        }
        return new L1Shop(npcId, sellingList, purchasingList);
    }

    private static void updata_name(int npcId, int itemId, int sellingPrice) {
        String blessed;
        Connection cn = null;
        PreparedStatement ps = null;
        String npcname = NpcTable.get().getNpcName(npcId);
        String itemname = ItemTable.get().getTemplate(itemId).getName();
        L1Item dropitem = ItemTable.get().getTemplate(itemId);
        if (dropitem.getBless() == 1) {
            blessed = "";
        } else if (dropitem.getBless() == 0) {
            blessed = "祝福→";
        } else {
            blessed = "詛咒→";
        }
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE `shop` SET `note`=? WHERE `npc_id`=? AND `item_id`=?");
            int i = 0 + 1;
            ps.setString(i, String.valueOf(npcname) + "→：" + blessed + itemname + "→賣出：" + sellingPrice + "金額");
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

    private static void updata_name1(int itemId, int purchasingPrice) {
        String blessed;
        Connection cn = null;
        PreparedStatement ps = null;
        String itemname = ItemTable.get().getTemplate(itemId).getName();
        L1Item dropitem = ItemTable.get().getTemplate(itemId);
        if (dropitem.getBless() == 1) {
            blessed = "";
        } else if (dropitem.getBless() == 0) {
            blessed = "祝福→";
        } else {
            blessed = "詛咒→";
        }
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE `shop_rates` SET `note`=? WHERE `item_id`=?");
            int i = 0 + 1;
            ps.setString(i, String.valueOf(blessed) + itemname + "→回收：" + purchasingPrice + "金額");
            ps.setInt(i + 1, itemId);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private static void delete1(int item_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `shop_rates` WHERE `item_id`=?");
            ps.setInt(1, item_id);
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
            ps = cn.prepareStatement("DELETE FROM `shop` WHERE `npc_id`=? AND `item_id`=?");
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

    private static void addSellList(int key, int value1, int value2, int packCount) {
        if (_noBuyList.get(Integer.valueOf(key)) == null) {
            Integer price = _allShopItem.get(new Integer(key));
            double value3 = 0.0d;
            if (value2 > 0) {
                value3 = (double) value2;
            }
            if (value3 < 1.0d) {
                _noBuyList.put(new Integer(key), new Integer((int) value3));
                if (price != null) {
                    _allShopItem.remove(new Integer(key));
                }
            } else if (price == null) {
                _allShopItem.put(new Integer(key), new Integer((int) value3));
            } else if (value3 < ((double) price.intValue())) {
                _allShopItem.put(new Integer(key), new Integer((int) value3));
            }
        }
    }

    public int getPrice(int itemid) {
        int tgprice = 0;
        Integer price = _allShopItem.get(new Integer(itemid));
        if (price != null) {
            tgprice = price.intValue();
        }
        if (_noBuyList.get(Integer.valueOf(itemid)) != null) {
            return 0;
        }
        return tgprice;
    }

    public L1Shop get(int npcId) {
        return _allShops.get(Integer.valueOf(npcId));
    }
}
