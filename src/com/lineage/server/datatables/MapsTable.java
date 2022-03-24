package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.MapData;
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

public final class MapsTable {
    private static MapsTable _instance;
    private static final Log _log = LogFactory.getLog(MapsTable.class);
    private static final Map<Integer, MapData> _maps = new HashMap();

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `mapids`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                MapData data = new MapData();
                int mapId = rs.getInt("mapid");
                data.mapId = mapId;
                data.startX = rs.getInt("startX");
                data.endX = rs.getInt("endX");
                data.startY = rs.getInt("startY");
                data.endY = rs.getInt("endY");
                data.monster_amount = rs.getDouble("monster_amount");
                data.dropRate = rs.getDouble("drop_rate");
                data.isUnderwater = rs.getBoolean("underwater");
                data.markable = rs.getBoolean("markable");
                data.teleportable = rs.getBoolean("teleportable");
                data.escapable = rs.getBoolean("escapable");
                data.isUseResurrection = rs.getBoolean("resurrection");
                data.isUsePainwand = rs.getBoolean("painwand");
                data.isEnabledDeathPenalty = rs.getBoolean("penalty");
                data.isTakePets = rs.getBoolean("take_pets");
                data.isRecallPets = rs.getBoolean("recall_pets");
                data.isUsableItem = rs.getBoolean("usable_item");
                data.isUsableSkill = rs.getBoolean("usable_skill");
                data.isMapteleport = rs.getInt("穿雲箭地圖限制");
                _maps.put(new Integer(mapId), data);
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入地圖設置資料數量: " + _maps.size() + "(" + timer.get() + "ms)");
    }

    public static MapsTable get() {
        if (_instance == null) {
            _instance = new MapsTable();
        }
        return _instance;
    }

    public Map<Integer, MapData> getMaps() {
        return _maps;
    }

    public int getStartX(int mapId) {
        if (_maps.get(Integer.valueOf(mapId)) == null) {
            return 0;
        }
        return _maps.get(Integer.valueOf(mapId)).startX;
    }

    public int getEndX(int mapId) {
        if (_maps.get(Integer.valueOf(mapId)) == null) {
            return 0;
        }
        return _maps.get(Integer.valueOf(mapId)).endX;
    }

    public int getStartY(int mapId) {
        if (_maps.get(Integer.valueOf(mapId)) == null) {
            return 0;
        }
        return _maps.get(Integer.valueOf(mapId)).startY;
    }

    public int getEndY(int mapId) {
        if (_maps.get(Integer.valueOf(mapId)) == null) {
            return 0;
        }
        return _maps.get(Integer.valueOf(mapId)).endY;
    }

    public double getMonsterAmount(int mapId) {
        MapData map = _maps.get(Integer.valueOf(mapId));
        if (map == null) {
            return 0.0d;
        }
        return map.monster_amount;
    }

    public double getDropRate(int mapId) {
        MapData map = _maps.get(Integer.valueOf(mapId));
        if (map == null) {
            return 0.0d;
        }
        return map.dropRate;
    }

    public boolean isUnderwater(int mapId) {
        if (_maps.get(Integer.valueOf(mapId)) == null) {
            return false;
        }
        return _maps.get(Integer.valueOf(mapId)).isUnderwater;
    }

    public boolean isMarkable(int mapId) {
        if (_maps.get(Integer.valueOf(mapId)) == null) {
            return false;
        }
        return _maps.get(Integer.valueOf(mapId)).markable;
    }

    public boolean isTeleportable(int mapId) {
        if (_maps.get(Integer.valueOf(mapId)) == null) {
            return false;
        }
        return _maps.get(Integer.valueOf(mapId)).teleportable;
    }

    public boolean isEscapable(int mapId) {
        if (_maps.get(Integer.valueOf(mapId)) == null) {
            return false;
        }
        return _maps.get(Integer.valueOf(mapId)).escapable;
    }

    public boolean isUseResurrection(int mapId) {
        if (_maps.get(Integer.valueOf(mapId)) == null) {
            return false;
        }
        return _maps.get(Integer.valueOf(mapId)).isUseResurrection;
    }

    public boolean isUsePainwand(int mapId) {
        if (_maps.get(Integer.valueOf(mapId)) == null) {
            return false;
        }
        return _maps.get(Integer.valueOf(mapId)).isUsePainwand;
    }

    public boolean isEnabledDeathPenalty(int mapId) {
        if (_maps.get(Integer.valueOf(mapId)) == null) {
            return false;
        }
        return _maps.get(Integer.valueOf(mapId)).isEnabledDeathPenalty;
    }

    public boolean isTakePets(int mapId) {
        if (_maps.get(Integer.valueOf(mapId)) == null) {
            return false;
        }
        return _maps.get(Integer.valueOf(mapId)).isTakePets;
    }

    public boolean isRecallPets(int mapId) {
        if (_maps.get(Integer.valueOf(mapId)) == null) {
            return false;
        }
        return _maps.get(Integer.valueOf(mapId)).isRecallPets;
    }

    public boolean isUsableItem(int mapId) {
        if (_maps.get(Integer.valueOf(mapId)) == null) {
            return false;
        }
        return _maps.get(Integer.valueOf(mapId)).isUsableItem;
    }

    public boolean isUsableSkill(int mapId) {
        if (_maps.get(Integer.valueOf(mapId)) == null) {
            return false;
        }
        return _maps.get(Integer.valueOf(mapId)).isUsableSkill;
    }

    public int isMapteleport(int mapId) {
        if (_maps.get(Integer.valueOf(mapId)) == null) {
            return 0;
        }
        return _maps.get(Integer.valueOf(mapId)).isMapteleport;
    }

    public String getMapName(int mapId, int x, int y) {
        return LoadZnoe.getInstance().findMapName(mapId, x, y);
    }
}
