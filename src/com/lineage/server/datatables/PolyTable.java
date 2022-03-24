package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.L1PolyMorph;
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

public class PolyTable {
    private static PolyTable _instance;
    private static final Log _log = LogFactory.getLog(PolyTable.class);
    private static final Map<Integer, L1PolyMorph> _polyIdIndex = new HashMap();
    private static final Map<String, L1PolyMorph> _polymorphs = new HashMap();

    public static PolyTable get() {
        if (_instance == null) {
            _instance = new PolyTable();
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
            ps = cn.prepareStatement("SELECT * FROM polymorphs");
            rs = ps.executeQuery();
            fillPolyTable(rs);
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入人物變身資料數量: " + _polymorphs.size() + "(" + timer.get() + "ms)");
    }

    private void fillPolyTable(ResultSet rs) throws SQLException {
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int polyId = rs.getInt("polyid");
            L1PolyMorph poly = new L1PolyMorph(id, name, polyId, rs.getInt("minlevel"), rs.getInt("weaponequip"), rs.getInt("armorequip"), rs.getBoolean("isSkillUse"), rs.getInt("cause"));
            _polymorphs.put(name, poly);
            _polyIdIndex.put(Integer.valueOf(polyId), poly);
        }
    }

    public L1PolyMorph getTemplate(String name) {
        return _polymorphs.get(name);
    }

    public L1PolyMorph getTemplate(int polyId) {
        return _polyIdIndex.get(Integer.valueOf(polyId));
    }
}
