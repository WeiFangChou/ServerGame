package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1ItemPowerUpdate;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ItemPowerUpdateTable {
    private static ItemPowerUpdateTable _instance;
    private static final Log _log = LogFactory.getLog(ItemPowerUpdateTable.class);
    private static Map<Integer, L1ItemPowerUpdate> _updateMap = new HashMap();

    public static ItemPowerUpdateTable get() {
        if (_instance == null) {
            _instance = new ItemPowerUpdateTable();
        }
        return _instance;
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `server_item_power_update`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int itemid = rs.getInt("itemid");
                if (ItemTable.get().getTemplate(itemid) == null) {
                    _log.error("特殊物品升級資料錯誤: 沒有這個編號的道具:" + itemid);
                    delete(itemid);
                } else {
                    int nedid = rs.getInt("nedid");
                    int type_id = rs.getInt("type_id");
                    int order_id = rs.getInt("order_id");
                    int mode = rs.getInt("mode");
                    int random = rs.getInt("random");
                    int out = rs.getInt("公告");
                    L1ItemPowerUpdate value = _updateMap.get(Integer.valueOf(itemid));
                    if (value == null) {
                        value = new L1ItemPowerUpdate();
                        value.set_itemid(itemid);
                        value.set_nedid(nedid);
                        value.set_type_id(type_id);
                        value.set_order_id(order_id);
                        value.set_mode(mode);
                        value.set_random(random);
                        value.set_out(out);
                    }
                    _updateMap.put(Integer.valueOf(itemid), value);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入物品升級資料數量: " + _updateMap.size() + "(" + timer.get() + "ms)");
    }

    public static void delete(int itemid) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `server_item_power_update` WHERE `itemid`=?");
            ps.setInt(1, itemid);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public Map<Integer, L1ItemPowerUpdate> get_type_id(int itemid) {
        Map<Integer, L1ItemPowerUpdate> updateMap = new HashMap<>();
        L1ItemPowerUpdate tmp = _updateMap.get(Integer.valueOf(itemid));
        if (tmp != null) {
            int type_id = tmp.get_type_id();
            for (L1ItemPowerUpdate value : _updateMap.values()) {
                if (value.get_type_id() == type_id) {
                    updateMap.put(Integer.valueOf(value.get_order_id()), value);
                }
            }
        }
        return updateMap;
    }

    public L1ItemPowerUpdate get(int key) {
        return _updateMap.get(Integer.valueOf(key));
    }

    public Map<Integer, L1ItemPowerUpdate> map() {
        return _updateMap;
    }
}
