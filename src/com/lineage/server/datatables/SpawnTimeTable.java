package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1SpawnTime;
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

public class SpawnTimeTable {
    private static SpawnTimeTable _instance;
    private static final Log _log = LogFactory.getLog(SpawnTimeTable.class);
    private static final Map<Integer, L1SpawnTime> _times = new HashMap();

    public static SpawnTimeTable getInstance() {
        if (_instance == null) {
            _instance = new SpawnTimeTable();
        }
        return _instance;
    }

    private SpawnTimeTable() {
        PerformanceTimer timer = new PerformanceTimer();
        load();
        _log.info("載入召喚時間資料數量: " + _times.size() + "(" + timer.get() + "ms)");
    }

    public L1SpawnTime get(int id) {
        return _times.get(Integer.valueOf(id));
    }

    private void load() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `spawnlist_time`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int npc_id = rs.getInt("npc_id");
                if (NpcTable.get().getTemplate(npc_id) == null) {
                    _log.error("召喚NPC編號: " + npc_id + " 不存在資料庫中!(spawnlist_time)");
                    delete(npc_id);
                } else {
                    L1SpawnTime.L1SpawnTimeBuilder builder = new L1SpawnTime.L1SpawnTimeBuilder(npc_id);
                    builder.setTimeStart(rs.getTime("time_start"));
                    builder.setTimeEnd(rs.getTime("time_end"));
                    builder.setDeleteAtEndTime(rs.getBoolean("delete_at_endtime"));
                    _times.put(Integer.valueOf(npc_id), builder.build());
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public static void delete(int npc_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `spawnlist_time` WHERE `npc_id`=?");
            ps.setInt(1, npc_id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }
}
