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

public class MapExpTable {
    private static final Map<Integer, Double> _exp = new HashMap();
    private static MapExpTable _instance;
    private static final Map<Integer, int[]> _level = new HashMap();
    private static final Log _log = LogFactory.getLog(MapExpTable.class);

    public static MapExpTable get() {
        if (_instance == null) {
            _instance = new MapExpTable();
        }
        return _instance;
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `mapids_exp`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int mapid = rs.getInt("mapid");
                _exp.put(new Integer(mapid), new Double((double) rs.getInt("exp")));
                _level.put(new Integer(mapid), new int[]{rs.getInt("min"), rs.getInt("max")});
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
        _log.info("載入地圖經驗值倍率資料數量: " + _exp.size() + "(" + timer.get() + "ms)");
    }

    public boolean get_level(int mapid, int level) {
        if (_exp.get(new Integer(mapid)) == null) {
            return false;
        }
        int[] levelX = _level.get(new Integer(mapid));
        return level >= levelX[0] && level <= levelX[1];
    }

    public Double get_exp(int mapid) {
        return _exp.get(new Integer(mapid));
    }
}
