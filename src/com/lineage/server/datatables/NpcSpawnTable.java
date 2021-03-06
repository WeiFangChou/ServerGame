package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Spawn;
import com.lineage.server.templates.L1Npc;
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

public class NpcSpawnTable {
    private static NpcSpawnTable _instance;
    private static final Log _log = LogFactory.getLog(NpcSpawnTable.class);
    private static final Map<Integer, L1Spawn> _spawntable = new HashMap();

    public static NpcSpawnTable get() {
        if (_instance == null) {
            _instance = new NpcSpawnTable();
        }
        return _instance;
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        int spawnCount = 0;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `spawnlist_npc`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int npcTemplateid = rs.getInt("npc_templateid");
                L1Npc l1npc = NpcTable.get().getTemplate(npcTemplateid);
                if (l1npc == null) {
                    _log.error("召喚NPC編號: " + npcTemplateid + " 不存在資料庫中!(spawnlist_npc)");
                    delete(npcTemplateid);
                } else {
                    int count = rs.getInt("count");
                    if (count != 0) {
                        L1Spawn l1spawn = new L1Spawn(l1npc);
                        l1spawn.setId(rs.getInt("id"));
                        l1spawn.setAmount(count);
                        l1spawn.setLocX(rs.getInt("locx"));
                        l1spawn.setLocY(rs.getInt("locy"));
                        l1spawn.setRandomx(rs.getInt("randomx"));
                        l1spawn.setRandomy(rs.getInt("randomy"));
                        l1spawn.setLocX1(0);
                        l1spawn.setLocY1(0);
                        l1spawn.setLocX2(0);
                        l1spawn.setLocY2(0);
                        l1spawn.setHeading(rs.getInt("heading"));
                        l1spawn.setMinRespawnDelay(rs.getInt("respawn_delay"));
                        l1spawn.setMapId(rs.getShort("mapid"));
                        l1spawn.setMovementDistance(rs.getInt("movement_distance"));
                        l1spawn.setName(l1npc.get_name());
                        l1spawn.init();
                        spawnCount += l1spawn.getAmount();
                        _spawntable.put(new Integer(l1spawn.getId()), l1spawn);
                    }
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入召喚NPC資料數量: " + _spawntable.size() + "/" + spawnCount + "(" + timer.get() + "ms)");
    }

    public void storeSpawn(L1PcInstance pc, L1Npc npc) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            String note = npc.get_name();
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("INSERT INTO `spawnlist_npc` SET `location`=?,`count`=?,`npc_templateid`=?,`locx`=?,`locy`=?,`heading`=?,`mapid`=?");
            pstm.setString(1, note);
            pstm.setInt(2, 1);
            pstm.setInt(3, npc.get_npcId());
            pstm.setInt(4, pc.getX());
            pstm.setInt(5, pc.getY());
            pstm.setInt(6, pc.getHeading());
            pstm.setInt(7, pc.getMapId());
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public static void delete(int npc_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `spawnlist_npc` WHERE `npc_templateid`=?");
            ps.setInt(1, npc_id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public L1Spawn getTemplate(int i) {
        return _spawntable.get(Integer.valueOf(i));
    }
}
