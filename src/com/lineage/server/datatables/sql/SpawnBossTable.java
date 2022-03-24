package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.storage.SpawnBossStorage;
import com.lineage.server.model.L1Spawn;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SpawnBossTable implements SpawnBossStorage {
    private static final Map<Integer, L1Spawn> _bossSpawnTable = new HashMap();
    private static final Log _log = LogFactory.getLog(SpawnBossTable.class);
    private List<Integer> _bossId = new ArrayList();

    private Calendar timestampToCalendar(Timestamp ts) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ts.getTime());
        return cal;
    }

    @Override // com.lineage.server.datatables.storage.SpawnBossStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `spawnlist_boss`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int npcTemplateId = rs.getInt("npc_templateid");
                L1Npc temp1 = NpcTable.get().getTemplate(npcTemplateId);
                if (temp1 == null) {
                    _log.error("BOSS召喚MOB編號: " + npcTemplateId + " 不存在資料庫中!");
                } else {
                    this._bossId.add(new Integer(npcTemplateId));
                    temp1.set_boss(true);
                    int tmp_id = temp1.getTransformId();
                    while (tmp_id > 0) {
                        this._bossId.add(new Integer(tmp_id));
                        L1Npc temp2 = NpcTable.get().getTemplate(tmp_id);
                        temp2.set_boss(true);
                        tmp_id = temp2.getTransformId();
                    }
                    int count = rs.getInt("count");
                    if (count > 0) {
                        int group_id = rs.getInt("group_id");
                        int locx1 = rs.getInt("locx1");
                        int locy1 = rs.getInt("locy1");
                        int locx2 = rs.getInt("locx2");
                        int locy2 = rs.getInt("locy2");
                        int heading = rs.getInt("heading");
                        int mapid = rs.getShort("mapid");
                        Calendar next_spawn_time = null;
                        if (rs.getTimestamp("next_spawn_time") != null) {
                            next_spawn_time = timestampToCalendar(rs.getTimestamp("next_spawn_time"));
                        }
                        int spawn_interval = rs.getInt("spawn_interval");
                        int exist_time = rs.getInt("exist_time");
                        int message = rs.getInt("頭目查詢時間");
                        int mes_boss = rs.getInt("頭目出現公告");
                        int message_boss = rs.getInt("頭目死亡公告");
                        L1Spawn spawnDat = new L1Spawn(temp1);
                        spawnDat.setId(id);
                        spawnDat.setAmount(count);
                        spawnDat.setGroupId(group_id);
                        spawnDat.setNpcid(npcTemplateId);
                        if (locx2 == 0 && locy2 == 0) {
                            spawnDat.setLocX(locx1);
                            spawnDat.setLocY(locy1);
                            spawnDat.setLocX1(0);
                            spawnDat.setLocY1(0);
                            spawnDat.setLocX2(0);
                            spawnDat.setLocY2(0);
                        } else {
                            spawnDat.setLocX(locx1);
                            spawnDat.setLocY(locy1);
                            spawnDat.setLocX1(locx1);
                            spawnDat.setLocY1(locy1);
                            spawnDat.setLocX2(locx2);
                            spawnDat.setLocY2(locy2);
                        }
                        if (locx2 < locx1 && locx2 != 0) {
                            _log.error("spawnlist_boss : locx2 < locx1:" + id);
                        } else if (locy2 >= locy1 || locy2 == 0) {
                            spawnDat.setHeading(heading);
                            spawnDat.setMapId((short) mapid);
                            spawnDat.setMinRespawnDelay(10);
                            spawnDat.setMovementDistance(100);
                            spawnDat.setName(temp1.get_name());
                            spawnDat.set_nextSpawnTime(next_spawn_time);
                            spawnDat.set_spawnInterval((long) spawn_interval);
                            spawnDat.set_existTime(exist_time);
                            spawnDat.setMes_boss(mes_boss);
                            spawnDat.setMessage(message);
                            spawnDat.setMessage_boss(message_boss);
                            spawnDat.setSpawnType(0);
                            if (count > 1 && spawnDat.getLocX1() == 0) {
                                int range = Math.min(count * 6, 30);
                                spawnDat.setLocX1(spawnDat.getLocX() - range);
                                spawnDat.setLocY1(spawnDat.getLocY() - range);
                                spawnDat.setLocX2(spawnDat.getLocX() + range);
                                spawnDat.setLocY2(spawnDat.getLocY() + range);
                            }
                            spawnDat.init();
                            _bossSpawnTable.put(new Integer(spawnDat.getId()), spawnDat);
                        } else {
                            _log.error("spawnlist_boss : locy2 < locy1:" + id);
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
        _log.info("載入BOSS召喚資料數量: " + _bossSpawnTable.size() + "(" + timer.get() + "ms)");
    }

    @Override // com.lineage.server.datatables.storage.SpawnBossStorage
    public void upDateNextSpawnTime(int id, Calendar spawnTime) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("UPDATE `spawnlist_boss` SET `next_spawn_time`=? WHERE `id`=?");
            int i = 0 + 1;
            pstm.setString(i, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(spawnTime.getTime()));
            pstm.setInt(i + 1, id);
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public static Map<Integer, L1Spawn> get_bossSpawnTable() {
        return _bossSpawnTable;
    }

    @Override // com.lineage.server.datatables.storage.SpawnBossStorage
    public L1Spawn getTemplate(int key) {
        return _bossSpawnTable.get(Integer.valueOf(key));
    }

    @Override // com.lineage.server.datatables.storage.SpawnBossStorage
    public List<Integer> bossIds() {
        return this._bossId;
    }
}
