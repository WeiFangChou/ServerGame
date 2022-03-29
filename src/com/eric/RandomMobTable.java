package com.eric;

import com.lineage.DatabaseFactory;
import com.lineage.server.IdFactory;
import com.lineage.server.datatables.MapsTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.utils.collections.Maps;
import com.lineage.server.world.World;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class RandomMobTable {
    private static RandomMobTable _instance;
    private static Logger _log = Logger.getLogger(RandomMobTable.class.getName());
    private static Random _random = new Random();
    private final Map<Integer, Data> _mobs = Maps.newHashMap();

    /* access modifiers changed from: private */
    public class Data {
        public int cont;

        /* renamed from: id */
        public int f0id;
        public boolean isActive;
        public int[] mapId;
        public int mobId;
        public String note;
        public int timeSecondToDelete;

        private Data() {
            this.f0id = 0;
            this.note = "";
            this.mobId = 0;
            this.cont = 0;
            this.mapId = new int[0];
            this.timeSecondToDelete = -1;
            this.isActive = false;
        }

        /* synthetic */ Data(RandomMobTable randomMobTable, Data data) {
            this();
        }
    }

    private RandomMobTable() {
        loadRandomMobFromDatabase();
    }

    private void loadRandomMobFromDatabase() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM eric_random_mob");
            rs = pstm.executeQuery();
            while (rs.next()) {
                Data data = new Data(this, null);
                int id = rs.getInt("id");
                data.f0id = id;
                data.note = rs.getString("note");
                String[] temp = rs.getString("mapId").split(",");
                int[] i = new int[temp.length];
                int loop = 0;
                for (String s : temp) {
                    i[loop] =  Integer.parseInt(s);
                    loop++;
                }
                data.mapId = i;
                data.mobId = rs.getInt("mobId");
                data.cont = rs.getInt("cont");
                data.timeSecondToDelete = rs.getInt("timeSecondToKill");
                data.isActive = rs.getBoolean("isActive");
                this._mobs.put(new Integer(id), data);
            }
            _log.config("RandomMob " + this._mobs.size());
        } catch (SQLException e) {
            e.printStackTrace();
            _log.log(Level.SEVERE, e.getLocalizedMessage(), (Throwable) e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void startRandomMob() {
        for (Data data : this._mobs.values()) {
            if (data.isActive) {
                spawn(data.f0id);
            }
        }
    }

    public static RandomMobTable getInstance() {
        if (_instance == null) {
            _instance = new RandomMobTable();
        }
        return _instance;
    }

    public int getRandomMapId(int RandomMobId) {
        if (this._mobs.get(Integer.valueOf(RandomMobId)) == null) {
            return 0;
        }
        return this._mobs.get(Integer.valueOf(RandomMobId)).mapId[new Random().nextInt(this._mobs.get(Integer.valueOf(RandomMobId)).mapId.length)];
    }

    public int getRandomMapX(int mapId) {
        int startX = MapsTable.get().getStartX(mapId);
        return startX + new Random().nextInt(MapsTable.get().getEndX(mapId) - startX);
    }

    public int getRandomMapY(int mapId) {
        int startY = MapsTable.get().getStartY(mapId);
        return startY + new Random().nextInt(MapsTable.get().getEndY(mapId) - startY);
    }

    public String getName(int RandomMobId) {
        if (this._mobs.get(Integer.valueOf(RandomMobId)) == null) {
            return "";
        }
        return this._mobs.get(Integer.valueOf(RandomMobId)).note;
    }

    public int getMobId(int RandomMobId) {
        if (this._mobs.get(Integer.valueOf(RandomMobId)) == null) {
            return 0;
        }
        return this._mobs.get(Integer.valueOf(RandomMobId)).mobId;
    }

    public int getCont(int RandomMobId) {
        if (this._mobs.get(Integer.valueOf(RandomMobId)) == null) {
            return 0;
        }
        return this._mobs.get(Integer.valueOf(RandomMobId)).cont;
    }

    public int getTimeSecondToDelete(int RandomMobId) {
        if (this._mobs.get(Integer.valueOf(RandomMobId)) == null) {
            return 0;
        }
        return this._mobs.get(Integer.valueOf(RandomMobId)).timeSecondToDelete;
    }

    public static void spawn(int randomMobId) {
        try {
            RandomMobTable mobTable = getInstance();
            int mobCont = mobTable.getCont(randomMobId);
            int mobId = mobTable.getMobId(randomMobId);
            L1NpcInstance[] npc = new L1NpcInstance[mobCont];
            for (int i = 0; i < mobCont; i++) {
                int mapId = mobTable.getRandomMapId(randomMobId);
                int x = mobTable.getRandomMapX(mapId);
                int y = mobTable.getRandomMapY(mapId);
                npc[i] = NpcTable.get().newNpcInstance(mobId);
                npc[i].setId(IdFactory.get().nextId());
                npc[i].setMap(mapId);
                int tryCount = 0;
                while (true) {
                    tryCount++;
                    npc[i].setX((_random.nextInt(200) + x) - _random.nextInt(200));
                    npc[i].setY((_random.nextInt(200) + y) - _random.nextInt(200));
                    L1Map map = npc[i].getMap();
                    if (map.isInMap(npc[i].getLocation()) && map.isPassable(npc[i].getLocation(), null)) {
                        break;
                    }
                    Thread.sleep(1);
                    if (tryCount >= 50) {
                        break;
                    }
                }
                if (tryCount >= 50) {
                    boolean find = false;
                    Iterator<L1Object> it = World.get().getVisibleObjects(mapId).values().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        Object obj = it.next();
                        if (obj instanceof L1PcInstance) {
                            L1PcInstance findPc = (L1PcInstance) obj;
                            npc[i].getLocation().set(findPc.getLocation());
                            npc[i].getLocation().forward(findPc.getHeading());
                            find = true;
                            break;
                        }
                    }
                    if (!find) {
                    }
                }
                npc[i].setHomeX(npc[i].getX());
                npc[i].setHomeY(npc[i].getY());
                npc[i].setHeading(0);
                World.get().storeObject(npc[i]);
                World.get().addVisibleObject(npc[i]);
                npc[i].turnOnOffLight();
                npc[i].startChat(0);
            }
            if (mobTable.getTimeSecondToDelete(randomMobId) > 0) {
                new RandomMobDeleteTimer(randomMobId, npc).begin();
            }
        } catch (Exception ignored) {
        }
    }
}
