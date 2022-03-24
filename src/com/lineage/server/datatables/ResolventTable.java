package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
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

public final class ResolventTable {
    private static ResolventTable _instance;
    private static final Log _log = LogFactory.getLog(ResolventTable.class);
    private static final Map<Integer, Integer> _resolvent = new HashMap();

    public static ResolventTable get() {
        if (_instance == null) {
            _instance = new ResolventTable();
        }
        return _instance;
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM resolvent");
            rs = ps.executeQuery();
            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                _resolvent.put(new Integer(itemId), Integer.valueOf(rs.getInt("crystal_count")));
                if (!rs.getString("note").contains("=>")) {
                    updata_name(itemId);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入溶解物品設置資料數量: " + _resolvent.size() + "(" + timer.get() + "ms)");
    }

    private static void updata_name(int itemId) {
        Connection cn = null;
        PreparedStatement ps = null;
        String itemname = ItemTable.get().getTemplate(itemId).getName();
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE `resolvent` SET `note`=? WHERE `item_id`=?");
            int i = 0 + 1;
            ps.setString(i, "名稱->" + itemname);
            ps.setInt(i + 1, itemId);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public int getCrystalCount(int itemId) {
        if (_resolvent.containsKey(Integer.valueOf(itemId))) {
            return _resolvent.get(Integer.valueOf(itemId)).intValue();
        }
        return 0;
    }
}
