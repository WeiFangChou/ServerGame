package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
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

public class MapLevelTable {
    private static MapLevelTable _instance;
    private static final Map<Integer, int[]> _level = new HashMap();
    private static final Log _log = LogFactory.getLog(MapLevelTable.class);

    public static MapLevelTable get() {
        if (_instance == null) {
            _instance = new MapLevelTable();
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
            ps = co.prepareStatement("SELECT * FROM `mapids_level`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int mapid = rs.getInt("mapid");
                _level.put(new Integer(mapid), new int[]{rs.getInt("min"), rs.getInt("max"), rs.getInt("locx"), rs.getInt("locy"), rs.getInt("tomapid")});
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
        _log.info("載入地圖等極限制資料數量: " + _level.size() + "(" + timer.get() + "ms)");
    }

    public void get_level(int mapid, L1PcInstance pc) {
        int[] levelX = _level.get(new Integer(mapid));
        if (levelX != null) {
            if ((pc.getLevel() < levelX[0] || pc.getLevel() >= levelX[1]) && !pc.isGm()) {
                L1Teleport.teleport(pc, levelX[2], levelX[3],  levelX[4], 5, true);
            }
        }
    }
}
