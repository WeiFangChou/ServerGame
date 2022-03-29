package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.L1Spawn;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1QuestMobSpawn;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QuesttSpawnTable {
    private static QuesttSpawnTable _instance;
    private static final Log _log = LogFactory.getLog(QuesttSpawnTable.class);
    private static final Map<Integer, L1Spawn> _spawntable = new HashMap();
    private static final Map<Integer, L1QuestMobSpawn> _spawntable_X1 = new HashMap();

    public static QuesttSpawnTable get() {
        if (_instance == null) {
            _instance = new QuesttSpawnTable();
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
            pstm = con.prepareStatement("SELECT * FROM `server_quest_spawn`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int questid = rs.getInt("questid");
                boolean is_spawn = rs.getBoolean("is_spawn");
                int npc_templateid = rs.getInt("npc_templateid");
                L1Npc template1 = NpcTable.get().getTemplate(npc_templateid);
                if (template1 == null) {
                    _log.error("召喚NPC編號: " + npc_templateid + " 不存在資料庫中!(server_quest_spawn)");
                    delete(npc_templateid);
                } else if (!is_spawn) {
                    int count2 = rs.getInt("count");
                    int group_id = rs.getInt("group_id");
                    int locx1 = rs.getInt("locx1");
                    int locx2 = rs.getInt("locx2");
                    int locy1 = rs.getInt("locy1");
                    int locy2 = rs.getInt("locy2");
                    int mapid = rs.getInt("mapid");
                    int heading = rs.getInt("heading");
                    L1QuestMobSpawn mobSpawn = new L1QuestMobSpawn();
                    mobSpawn.set_questid(questid);
                    mobSpawn.set_count(count2);
                    mobSpawn.set_npc_templateid(npc_templateid);
                    mobSpawn.set_group_id(group_id);
                    mobSpawn.set_locx1(locx1);
                    mobSpawn.set_locy1(locy1);
                    mobSpawn.set_locx2(locx2);
                    mobSpawn.set_locy2(locy2);
                    mobSpawn.set_heading(heading);
                    mobSpawn.set_mapid(mapid);
                    _spawntable_X1.put(Integer.valueOf(id), mobSpawn);
                } else if (!(QuestTable.get().getTemplate(questid) == null || (count = rs.getInt("count")) == 0)) {
                    int group_id2 = rs.getInt("group_id");
                    int locx12 = rs.getInt("locx1");
                    int locx22 = rs.getInt("locx2");
                    int locy12 = rs.getInt("locy1");
                    int locy22 = rs.getInt("locy2");
                    int mapid2 = rs.getInt("mapid");
                    int heading2 = rs.getInt("heading");
                    int respawn_delay = rs.getInt("respawn_delay");
                    int movement_distance = rs.getInt("movement_distance");
                    int near_spawn = rs.getInt("near_spawn");
                    if (locx12 != 0 || locx22 != 0 || locy12 != 0 || locy22 != 0) {
                        L1Spawn spawnDat = new L1Spawn(template1);
                        spawnDat.setId(id);
                        spawnDat.setAmount(count);
                        spawnDat.setGroupId(group_id2);
                        if (locx22 == 0 && locy22 == 0) {
                            spawnDat.setLocX(locx12);
                            spawnDat.setLocY(locy12);
                            spawnDat.setLocX1(0);
                            spawnDat.setLocY1(0);
                            spawnDat.setLocX2(0);
                            spawnDat.setLocY2(0);
                        } else {
                            spawnDat.setLocX(locx12);
                            spawnDat.setLocY(locy12);
                            spawnDat.setLocX1(locx12);
                            spawnDat.setLocY1(locy12);
                            spawnDat.setLocX2(locx22);
                            spawnDat.setLocY2(locy22);
                        }
                        if (count > 1 && spawnDat.getLocX1() == 0) {
                            int range = Math.min(count * 6, 20);
                            spawnDat.setLocX1(spawnDat.getLocX() - range);
                            spawnDat.setLocY1(spawnDat.getLocY() - range);
                            spawnDat.setLocX2(spawnDat.getLocX() + range);
                            spawnDat.setLocY2(spawnDat.getLocY() + range);
                        }
                        if (locx22 < locx12 && locx22 != 0) {
                            _log.error("server_quest_spawn : locx2 < locx1: " + id);
                        } else if (locy22 >= locy12 || locy22 == 0) {
                            spawnDat.setRandomx(0);
                            spawnDat.setRandomy(0);
                            spawnDat.setMinRespawnDelay(respawn_delay);
                            spawnDat.setMovementDistance(movement_distance);
                            spawnDat.setHeading(heading2);
                            spawnDat.setMapId( mapid2);
                            spawnDat.setSpawnType(near_spawn);
                            spawnDat.setTime(SpawnTimeTable.getInstance().get(spawnDat.getId()));
                            spawnDat.setName(template1.get_name());
                            spawnDat.init();
                            spawnCount += spawnDat.getAmount();
                            _spawntable.put(new Integer(spawnDat.getId()), spawnDat);
                        } else {
                            _log.error("server_quest_spawn : locy2 < locy1: " + id);
                        }
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
        _log.info("載入召喚QUEST NPC資料數量: " + spawnCount + "(" + timer.get() + "ms)");
    }

    public static void delete(int npc_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `server_quest_spawn` WHERE `npc_templateid`=?");
            ps.setInt(1, npc_id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public L1Spawn getTemplate(int id) {
        return _spawntable.get(new Integer(id));
    }

    public ArrayList<L1QuestMobSpawn> getMobSpawn(int questid) {
        ArrayList<L1QuestMobSpawn> spawnList = new ArrayList<>();
        for (L1QuestMobSpawn mobSpawn : _spawntable_X1.values()) {
            if (mobSpawn.get_questid() == questid) {
                spawnList.add(mobSpawn);
            }
        }
        return spawnList;
    }
}
