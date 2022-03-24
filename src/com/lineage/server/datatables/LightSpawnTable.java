package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.model.Instance.L1FieldObjectInstance;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LightSpawnTable {
    private static LightSpawnTable _instance;
    private static final Log _log = LogFactory.getLog(LightSpawnTable.class);

    public static LightSpawnTable getInstance() {
        if (_instance == null) {
            _instance = new LightSpawnTable();
        }
        return _instance;
    }

    private LightSpawnTable() {
        FillLightSpawnTable();
    }

    private void FillLightSpawnTable() {
        PerformanceTimer timer = new PerformanceTimer();
        int spawnCount = 0;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `spawnlist_light`");
            rs = ps.executeQuery();
            while (rs.next()) {
                Thread.sleep(10);
                int npcid = rs.getInt("npcid");
                spawnCount++;
                L1Npc l1npc = NpcTable.get().getTemplate(npcid);
                if (l1npc != null) {
                    L1FieldObjectInstance field = (L1FieldObjectInstance) NpcTable.get().newNpcInstance(npcid);
                    field.setId(IdFactoryNpc.get().nextId());
                    field.setX(rs.getInt("locx"));
                    field.setY(rs.getInt("locy"));
                    field.setMap((short) rs.getInt("mapid"));
                    field.setHomeX(field.getX());
                    field.setHomeY(field.getY());
                    field.setHeading(0);
                    field.setLightSize(l1npc.getLightSize());
                    World.get().storeObject(field);
                    World.get().addVisibleObject(field);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入召喚照明資料數量: " + spawnCount + "(" + timer.get() + "ms)");
    }
}
