package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.model.Instance.L1TrapInstance;
import com.lineage.server.model.L1Location;
import com.lineage.server.templates.L1Trap;
import com.lineage.server.types.Point;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldTrap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TrapsSpawn {
    private static TrapsSpawn _instance;
    private static final Log _log = LogFactory.getLog(TrapsSpawn.class);

    public static TrapsSpawn get() {
        if (_instance == null) {
            _instance = new TrapsSpawn();
        }
        return _instance;
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `spawnlist_trap`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                L1Trap trapTemp = TrapTable.get().getTemplate(rs.getInt("trapId"));
                L1Location loc = new L1Location();
                loc.setMap(rs.getInt("mapId"));
                loc.setX(rs.getInt("locX"));
                loc.setY(rs.getInt("locY"));
                Point rndPt = new Point();
                rndPt.setX(rs.getInt("locRndX"));
                rndPt.setY(rs.getInt("locRndY"));
                int count = rs.getInt("count");
                int span = rs.getInt("span");
                for (int i = 0; i < count; i++) {
                    L1TrapInstance trap = new L1TrapInstance(IdFactoryNpc.get().nextId(), trapTemp, loc, rndPt, span);
                    World.get().storeObject(trap);
                    World.get().addVisibleObject(trap);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入陷阱召喚資料數量: " + WorldTrap.get().map().size() + "(" + timer.get() + "ms)");
    }

    public void reloadTraps() {
        for (Object iter : WorldTrap.get().map().values().toArray()) {
            L1TrapInstance trap = (L1TrapInstance) iter;
            World.get().removeObject(trap);
            World.get().removeVisibleObject(trap);
        }
        load();
    }
}
