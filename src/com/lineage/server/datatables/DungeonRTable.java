package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Teleport2;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DungeonRTable {
    private static Map<String, ArrayList<int[]>> _dungeonMap = new HashMap();
    private static Map<String, Integer> _dungeonMapID = new HashMap();
    private static DungeonRTable _instance = null;
    private static final Log _log = LogFactory.getLog(DungeonRTable.class);
    private static Random _random = new Random();

    public static DungeonRTable get() {
        if (_instance == null) {
            _instance = new DungeonRTable();
        }
        return _instance;
    }

    public void load() throws Throwable {
        Throwable th;
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `dungeon_random`");
            rs = ps.executeQuery();
            int teltportId = 1000;
            while (rs.next()) {
                try {
                    int srcMapId = rs.getInt("src_mapid");
                    String key = new StringBuilder().append(srcMapId).append(rs.getInt("src_x")).append(rs.getInt("src_y")).toString();
                    if (_dungeonMap.containsKey(key)) {
                        _log.error("相同SRC(多點)傳送座標(" + key + ")");
                    } else {
                        int heading = rs.getInt("new_heading");
                        ArrayList<int[]> value = new ArrayList<>();
                        if (rs.getInt("new_x1") != 0) {
                            value.add(new int[]{rs.getInt("new_x1"), rs.getInt("new_y1"), rs.getShort("new_mapid1"), heading});
                        }
                        if (rs.getInt("new_x2") != 0) {
                            value.add(new int[]{rs.getInt("new_x2"), rs.getInt("new_y2"), rs.getShort("new_mapid2"), heading});
                        }
                        if (rs.getInt("new_x3") != 0) {
                            value.add(new int[]{rs.getInt("new_x3"), rs.getInt("new_y3"), rs.getShort("new_mapid3"), heading});
                        }
                        if (rs.getInt("new_x4") != 0) {
                            value.add(new int[]{rs.getInt("new_x4"), rs.getInt("new_y4"), rs.getShort("new_mapid4"), heading});
                        }
                        if (rs.getInt("new_x5") != 0) {
                            value.add(new int[]{rs.getInt("new_x5"), rs.getInt("new_y5"), rs.getShort("new_mapid5"), heading});
                        }
                        _dungeonMap.put(key, value);
                        _dungeonMapID.put(key, Integer.valueOf(teltportId));
                        teltportId++;
                    }
                } catch (SQLException e) {
                    SQLException e1 = e;
                    try {
                        _log.error(e1.getLocalizedMessage(), e1);
                        SQLUtil.close(rs);
                        SQLUtil.close(ps);
                        SQLUtil.close(cn);
                        _log.info("載入地圖切換點設置(多點)數量: " + _dungeonMapID.size() + "(" + timer.get() + "ms)");
                    } catch (Throwable th2) {
                        th = th2;
                        SQLUtil.close(rs);
                        SQLUtil.close(ps);
                        SQLUtil.close(cn);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    SQLUtil.close(rs);
                    SQLUtil.close(ps);
                    SQLUtil.close(cn);
                    throw th;
                }
            }
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        } catch (SQLException e2) {
            SQLException e = e2;
            _log.error(e.getLocalizedMessage(), e);
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
            _log.info("載入地圖切換點設置(多點)數量: " + _dungeonMapID.size() + "(" + timer.get() + "ms)");
        }
        _log.info("載入地圖切換點設置(多點)數量: " + _dungeonMapID.size() + "(" + timer.get() + "ms)");
    }

    public boolean dg(int locX, int locY, int mapId, L1PcInstance pc) {
        String key = new StringBuilder().append(mapId).append(locX).append(locY).toString();
        if (!_dungeonMap.containsKey(key)) {
            return false;
        }
        ArrayList<int[]> newLocs = _dungeonMap.get(key);
        int[] loc = newLocs.get(_random.nextInt(newLocs.size()));
        int newX = loc[0];
        int newY = loc[1];
        short newMap = (short) loc[2];
        int heading = loc[3];
        pc.setSkillEffect(78, 2000);
        pc.stopHpRegeneration();
        pc.stopMpRegeneration();
        teleport(pc, _dungeonMapID.get(key).intValue(), newX, newY, newMap, heading, false);
        return true;
    }

    private void teleport(L1PcInstance pc, int id, int newX, int newY, short newMap, int heading, boolean b) {
        pc.setTeleportX(newX);
        pc.setTeleportY(newY);
        pc.setTeleportMapId(newMap);
        pc.setTeleportHeading(heading);
        pc.sendPackets(new S_Teleport2(newMap, id));
    }
}
