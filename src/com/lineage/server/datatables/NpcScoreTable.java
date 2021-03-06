package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NpcScoreTable {
    private static NpcScoreTable _instance;
    private static final Log _log = LogFactory.getLog(NpcScoreTable.class);
    private static final Map<Integer, Integer> _scoreList = new TreeMap();

    public static NpcScoreTable get() {
        if (_instance == null) {
            _instance = new NpcScoreTable();
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
            ps = cn.prepareStatement("SELECT * FROM `npcscore`");
            rs = ps.executeQuery();
            while (rs.next()) {
                _scoreList.put(new Integer(rs.getInt("npcid")), new Integer(rs.getInt("score")));
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入NPC積分設置資料數量: " + _scoreList.size() + "(" + timer.get() + "ms)");
    }

    public Map<Integer, Integer> get_scoreList() {
        return _scoreList;
    }

    public int get_score(int npcid) {
        if (_scoreList.get(Integer.valueOf(npcid)) != null) {
            return _scoreList.get(Integer.valueOf(npcid)).intValue();
        }
        return 0;
    }
}
