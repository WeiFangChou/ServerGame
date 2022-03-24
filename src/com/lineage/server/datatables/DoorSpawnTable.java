package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DoorSpawnTable {
    private static final ArrayList<L1DoorInstance> _doorList = new ArrayList<>();
    private static DoorSpawnTable _instance;
    private static final Log _log = LogFactory.getLog(DoorSpawnTable.class);

    public static DoorSpawnTable get() {
        if (_instance == null) {
            _instance = new DoorSpawnTable();
        }
        return _instance;
    }

    public void load() {
        int id;
        PerformanceTimer timer = new PerformanceTimer();
        int i = 0;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `spawnlist_door`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                i++;
                L1Npc l1npc = NpcTable.get().getTemplate(81158);
                if (l1npc != null && ((id = rs.getInt("id")) < 808 || id > 812)) {
                    L1DoorInstance door = (L1DoorInstance) NpcTable.get().newNpcInstance(l1npc);
                    door.setId(IdFactoryNpc.get().nextId());
                    door.setDoorId(id);
                    door.setGfxId(rs.getInt("gfxid"));
                    int x = rs.getInt("locx");
                    int y = rs.getInt("locy");
                    door.setX(x);
                    door.setY(y);
                    door.setMap(rs.getShort("mapid"));
                    door.setHomeX(x);
                    door.setHomeY(y);
                    door.setDirection(rs.getInt("direction"));
                    door.setLeftEdgeLocation(rs.getInt("left_edge_location"));
                    door.setRightEdgeLocation(rs.getInt("right_edge_location"));
                    int hp = rs.getInt("hp");
                    door.setMaxHp(hp);
                    door.setCurrentHp(hp);
                    door.setKeeperId(rs.getInt("keeper"));
                    World.get().storeObject(door);
                    World.get().addVisibleObject(door);
                    _doorList.add(door);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } catch (SecurityException e2) {
            _log.error(e2.getLocalizedMessage(), e2);
        } catch (IllegalArgumentException e3) {
            _log.error(e3.getLocalizedMessage(), e3);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入門資料數量: " + i + "(" + timer.get() + "ms)");
    }

    public L1DoorInstance[] getDoorList() {
        return (L1DoorInstance[]) _doorList.toArray(new L1DoorInstance[_doorList.size()]);
    }
}
