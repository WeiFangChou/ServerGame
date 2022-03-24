package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Spawn;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.utils.NumberUtil;
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

public class SpawnTable {
    private static SpawnTable _instance;
    private static final Log _log = LogFactory.getLog(SpawnTable.class);
    private static final Map<Integer, L1Spawn> _spawntable = new HashMap();

    public static SpawnTable get() {
        if (_instance == null) {
            _instance = new SpawnTable();
        }
        return _instance;
    }

    public void load() {
        int count;
        PerformanceTimer timer = new PerformanceTimer();
        int spawnCount = 0;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `spawnlist`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int npcTemplateId = rs.getInt("npc_templateid");
                L1Npc template1 = NpcTable.get().getTemplate(npcTemplateId);
                if (template1 == null) {
                    _log.error("召喚MOB編號: " + npcTemplateId + " 不存在資料庫中!");
                    delete(npcTemplateId);
                } else {
                    int count2 = rs.getInt("count");
                    if (!(count2 == 0 || (count = calcCount(template1, count2, MapsTable.get().getMonsterAmount(rs.getShort("mapid")))) == 0)) {
                        L1Spawn spawnDat = new L1Spawn(template1);
                        spawnDat.setId(rs.getInt("id"));
                        spawnDat.setAmount(count);
                        spawnDat.setGroupId(rs.getInt("group_id"));
                        spawnDat.setLocX(rs.getInt("locx"));
                        spawnDat.setLocY(rs.getInt("locy"));
                        spawnDat.setRandomx(rs.getInt("randomx"));
                        spawnDat.setRandomy(rs.getInt("randomy"));
                        spawnDat.setLocX1(rs.getInt("locx1"));
                        spawnDat.setLocY1(rs.getInt("locy1"));
                        spawnDat.setLocX2(rs.getInt("locx2"));
                        spawnDat.setLocY2(rs.getInt("locy2"));
                        spawnDat.setHeading(rs.getInt("heading"));
                        spawnDat.setMinRespawnDelay(rs.getInt("min_respawn_delay"));
                        spawnDat.setMaxRespawnDelay(rs.getInt("max_respawn_delay"));
                        spawnDat.setMapId(rs.getShort("mapid"));
                        spawnDat.setRespawnScreen(rs.getBoolean("respawn_screen"));
                        spawnDat.setMovementDistance(rs.getInt("movement_distance"));
                        spawnDat.setRest(rs.getBoolean("rest"));
                        spawnDat.setSpawnType(rs.getInt("near_spawn"));
                        spawnDat.setTime(SpawnTimeTable.getInstance().get(npcTemplateId));
                        spawnDat.setName(template1.get_name());
                        if (count > 1 && spawnDat.getLocX1() == 0) {
                            int range = Math.min(count * 6, 30);
                            spawnDat.setLocX1(spawnDat.getLocX() - range);
                            spawnDat.setLocY1(spawnDat.getLocY() - range);
                            spawnDat.setLocX2(spawnDat.getLocX() + range);
                            spawnDat.setLocY2(spawnDat.getLocY() + range);
                        }
                        spawnDat.init();
                        spawnCount += spawnDat.getAmount();
                        _spawntable.put(new Integer(spawnDat.getId()), spawnDat);
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
        _log.info("載入召喚MOB資料數量: " + spawnCount + "(" + timer.get() + "ms)");
    }

    public L1Spawn getTemplate(int Id) {
        return _spawntable.get(new Integer(Id));
    }

    public static void delete(int npc_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `spawnlist` WHERE `npc_templateid`=?");
            ps.setInt(1, npc_id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public static void storeSpawn(L1PcInstance pc, L1Npc npc) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            String note = npc.get_name();
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("INSERT INTO `spawnlist` SET `location`=?,`count`=?,`npc_templateid`=?,`group_id`=?,`locx`=?,`locy`=?,`randomx`=?,`randomy`=?,`heading`=?,`min_respawn_delay`=?,`max_respawn_delay`=?,`mapid`=?");
            pstm.setString(1, note);
            pstm.setInt(2, 1);
            pstm.setInt(3, npc.get_npcId());
            pstm.setInt(4, 0);
            pstm.setInt(5, pc.getX());
            pstm.setInt(6, pc.getY());
            pstm.setInt(7, 12);
            pstm.setInt(8, 12);
            pstm.setInt(9, pc.getHeading());
            pstm.setInt(10, 60);
            pstm.setInt(11, OpcodesServer.S_OPCODE_STRUP);
            pstm.setInt(12, pc.getMapId());
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    private static int calcCount(L1Npc npc, int count, double rate) {
        if (rate == 0.0d) {
            return 0;
        }
        return (rate == 1.0d || npc.isAmountFixed()) ? count : NumberUtil.randomRound(((double) count) * rate);
    }
}
