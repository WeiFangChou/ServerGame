package com.lineage.server.model;

import com.lineage.config.ConfigAlt;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1BowInstance;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1FieldObjectInstance;
import com.lineage.server.model.Instance.L1FurnitureInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.gametime.L1GameTime;
import com.lineage.server.model.gametime.L1GameTimeAdapter;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_PacketBoxGree1;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1SpawnTime;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.npc.NpcSpawnBossTimer;
import com.lineage.server.types.Point;
import com.lineage.server.world.World;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import william.L1WilliamSystemMessage;

public class L1Spawn extends L1GameTimeAdapter {
    private static final int SPAWN_TYPE_PC_AROUND = 1;
    private static final Log _log = LogFactory.getLog(L1Spawn.class);
    private int _delayInterval;
    private int _existTime = 0;
    private int _groupId;
    private int _heading;
    private Map<Integer, Point> _homePoint = null;
    private int _id;
    private boolean _initSpawn = false;
    private String _location;
    private int _locx;
    private int _locx1;
    private int _locx2;
    private int _locy;
    private int _locy1;
    private int _locy2;
    private int _mapid;
    private int _maxRespawnDelay;
    private int _maximumCount;
    private int _mes_boss;
    private int _message;
    private int _message_boss;
    private int _minRespawnDelay;
    private List<L1NpcInstance> _mobs = new ArrayList();
    private int _movementDistance;
    private String _name;
    private Calendar _nextSpawnTime = null;
    private int _npcid;
    private Random _random = new Random();
    private int _randomx;
    private int _randomy;
    private boolean _respaenScreen;
    private boolean _rest;
    private boolean _spawnHomePoint;
    private long _spawnInterval = 0;
    private int _spawnType;
    private final L1Npc _template;
    private L1SpawnTime _time;
    private int _tmplocx;
    private int _tmplocy;
    private int _tmpmapid;

    /* access modifiers changed from: private */
    public class SpawnTask implements Runnable {
        private long _delay;
        private int _objectId;
        private int _spawnNumber;

        private SpawnTask(int spawnNumber, int objectId, long delay) {
            this._spawnNumber = spawnNumber;
            this._objectId = objectId;
            this._delay = delay;
        }

        /* synthetic */ SpawnTask(L1Spawn l1Spawn, int i, int i2, long j, SpawnTask spawnTask) {
            this(i, i2, j);
        }

        public void getStart() {
            GeneralThreadPool.get().schedule(this, this._delay);
        }

        public void run() {
            L1Spawn.this.doSpawn(this._spawnNumber, this._objectId);
        }
    }

    public L1Spawn(L1Npc mobTemplate) {
        this._template = mobTemplate;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public int getMapId() {
        return this._mapid;
    }

    public void setMapId(int _mapid2) {
        this._mapid = _mapid2;
    }

    public boolean isRespawnScreen() {
        return this._respaenScreen;
    }

    public void setRespawnScreen(boolean flag) {
        this._respaenScreen = flag;
    }

    public int getMovementDistance() {
        return this._movementDistance;
    }

    public void setMovementDistance(int i) {
        this._movementDistance = i;
    }

    public int getAmount() {
        return this._maximumCount;
    }

    public int getGroupId() {
        return this._groupId;
    }

    public int getId() {
        return this._id;
    }

    public String getLocation() {
        return this._location;
    }

    public int getLocX() {
        return this._locx;
    }

    public int getLocY() {
        return this._locy;
    }

    public int getNpcId() {
        return this._npcid;
    }

    public int getHeading() {
        return this._heading;
    }

    public int getRandomx() {
        return this._randomx;
    }

    public int getRandomy() {
        return this._randomy;
    }

    public int getLocX1() {
        return this._locx1;
    }

    public int getLocY1() {
        return this._locy1;
    }

    public int getLocX2() {
        return this._locx2;
    }

    public int getLocY2() {
        return this._locy2;
    }

    public int getMinRespawnDelay() {
        return this._minRespawnDelay;
    }

    public int getMaxRespawnDelay() {
        return this._maxRespawnDelay;
    }

    public void setAmount(int amount) {
        this._maximumCount = amount;
    }

    public void setId(int id) {
        this._id = id;
    }

    public void setGroupId(int i) {
        this._groupId = i;
    }

    public void setLocation(String location) {
        this._location = location;
    }

    public void setLocX(int locx) {
        this._locx = locx;
    }

    public void setLocY(int locy) {
        this._locy = locy;
    }

    public void setNpcid(int npcid) {
        this._npcid = npcid;
    }

    public void setHeading(int heading) {
        this._heading = heading;
    }

    public void setRandomx(int randomx) {
        this._randomx = randomx;
    }

    public void setRandomy(int randomy) {
        this._randomy = randomy;
    }

    public void setLocX1(int locx1) {
        this._locx1 = locx1;
    }

    public void setLocY1(int locy1) {
        this._locy1 = locy1;
    }

    public void setLocX2(int locx2) {
        this._locx2 = locx2;
    }

    public void setLocY2(int locy2) {
        this._locy2 = locy2;
    }

    public void setMinRespawnDelay(int i) {
        this._minRespawnDelay = i;
    }

    public void setMaxRespawnDelay(int i) {
        this._maxRespawnDelay = i;
    }

    public int getTmpLocX() {
        return this._tmplocx;
    }

    public int getTmpLocY() {
        return this._tmplocy;
    }

    public int getTmpMapid() {
        return this._tmpmapid;
    }

    private boolean isSpawnTime(L1NpcInstance npcTemp) {
        if (this._nextSpawnTime == null) {
            return true;
        }
        Calendar cals = Calendar.getInstance();
        long nowTime = System.currentTimeMillis();
        cals.setTimeInMillis(nowTime);
        if (cals.after(this._nextSpawnTime)) {
            return true;
        }
        if (NpcSpawnBossTimer.MAP.get(npcTemp) == null) {
            NpcSpawnBossTimer.MAP.put(npcTemp, Long.valueOf(((this._nextSpawnTime.getTimeInMillis() - nowTime) / 1000) + 5));
        }
        return false;
    }

    public Calendar get_nextSpawnTime() {
        return this._nextSpawnTime;
    }

    public void set_nextSpawnTime(Calendar next_spawn_time) {
        this._nextSpawnTime = next_spawn_time;
    }

    public void set_spawnInterval(long spawn_interval) {
        this._spawnInterval = spawn_interval;
    }

    public long get_spawnInterval() {
        return this._spawnInterval;
    }

    public void set_existTime(int exist_time) {
        this._existTime = exist_time;
    }

    private int calcRespawnDelay() {
        int respawnDelay = this._minRespawnDelay * L1SkillId.STATUS_BRAVE;
        if (this._delayInterval > 0) {
            respawnDelay += this._random.nextInt(this._delayInterval) * L1SkillId.STATUS_BRAVE;
        }
        L1GameTime currentTime = L1GameTimeClock.getInstance().currentTime();
        if (this._time == null || this._time.getTimePeriod().includes(currentTime)) {
            return respawnDelay;
        }
        long diff = this._time.getTimeStart().getTime() - currentTime.toTime().getTime();
        if (diff < 0) {
            diff += 86400000;
        }
        return (int) (diff / 6);
    }

    public void executeSpawnTask(int spawnNumber, int objectId) {
        if (this._nextSpawnTime != null) {
            doSpawn(spawnNumber, objectId);
        } else {
            new SpawnTask(this, spawnNumber, objectId, (long) calcRespawnDelay(), null).getStart();
        }
    }

    public void init() {
        if (this._time != null && this._time.isDeleteAtEndTime()) {
            L1GameTimeClock.getInstance().addListener(this);
        }
        this._delayInterval = this._maxRespawnDelay - this._minRespawnDelay;
        this._initSpawn = true;
        if (ConfigAlt.SPAWN_HOME_POINT && ConfigAlt.SPAWN_HOME_POINT_COUNT <= getAmount() && ConfigAlt.SPAWN_HOME_POINT_DELAY >= getMinRespawnDelay() && isAreaSpawn()) {
            this._spawnHomePoint = true;
            this._homePoint = new HashMap();
        }
        int spawnNum = 0;
        while (spawnNum < this._maximumCount) {
            spawnNum++;
            doSpawn(spawnNum);
        }
        this._initSpawn = false;
    }

    /* access modifiers changed from: protected */
    public void doSpawn(int spawnNumber) {
        if (this._time == null || this._time.getTimePeriod().includes(L1GameTimeClock.getInstance().currentTime())) {
            doSpawn(spawnNumber, 0);
        } else {
            executeSpawnTask(spawnNumber, 0);
        }
    }

    /* access modifiers changed from: protected */
    public void doSpawn(int spawnNumber, int objectId) {
        Point pt;
        this._tmplocx = 0;
        this._tmplocy = 0;
        this._tmpmapid = 0;
        try {
            int newlocx = getLocX();
            int newlocy = getLocY();
            int tryCount = 0;
            L1NpcInstance npcTemp = NpcTable.get().newNpcInstance(this._template);
            synchronized (this._mobs) {
                this._mobs.add(npcTemp);
            }
            if (objectId == 0) {
                npcTemp.setId(IdFactoryNpc.get().nextId());
            } else {
                npcTemp.setId(objectId);
            }
            if (getHeading() < 0 || getHeading() > 7) {
                npcTemp.setHeading(5);
            } else {
                npcTemp.setHeading(getHeading());
            }
            int npcId = npcTemp.getNpcTemplate().get_npcId();
            if (npcId == 45488 && getMapId() == 9) {
                npcTemp.setMap( (getMapId() + this._random.nextInt(2)));
            } else if (npcId == 45601 && getMapId() == 11) {
                npcTemp.setMap( (getMapId() + this._random.nextInt(3)));
            } else {
                npcTemp.setMap(getMapId());
            }
            npcTemp.setMovementDistance(getMovementDistance());
            npcTemp.setRest(isRest());
            while (true) {
                if (tryCount > 50) {
                    break;
                }
                if (isAreaSpawn()) {
                    if (!this._spawnHomePoint || (pt = this._homePoint.get(Integer.valueOf(spawnNumber))) == null) {
                        int rangeX = getLocX2() - getLocX1();
                        int rangeY = getLocY2() - getLocY1();
                        newlocx = this._random.nextInt(rangeX) + getLocX1();
                        newlocy = this._random.nextInt(rangeY) + getLocY1();
                    } else {
                        L1Location loc = new L1Location(pt, getMapId()).randomLocation(ConfigAlt.SPAWN_HOME_POINT_RANGE, false);
                        newlocx = loc.getX();
                        newlocy = loc.getY();
                    }
                    if (tryCount > 49) {
                        if (this._nextSpawnTime == null) {
                            newlocx = getLocX();
                            newlocy = getLocY();
                        } else {
                            new SpawnTask(this, spawnNumber, npcTemp.getId(), 5000, null).getStart();
                            return;
                        }
                    }
                } else if (isRandomSpawn()) {
                    newlocx = getLocX() + (((int) (Math.random() * ((double) getRandomx()))) - ((int) (Math.random() * ((double) getRandomx()))));
                    newlocy = getLocY() + (((int) (Math.random() * ((double) getRandomy()))) - ((int) (Math.random() * ((double) getRandomy()))));
                } else {
                    newlocx = getLocX();
                    newlocy = getLocY();
                }
                if (getSpawnType() == 1) {
                    L1Location loc2 = new L1Location(newlocx, newlocy, getMapId());
                    if (World.get().getVisiblePc(loc2).size() > 0) {
                        L1Location newloc = loc2.randomLocation(20, false);
                        newlocx = newloc.getX();
                        newlocy = newloc.getY();
                    }
                }
                npcTemp.setX(newlocx);
                npcTemp.setHomeX(newlocx);
                npcTemp.setY(newlocy);
                npcTemp.setHomeY(newlocy);
                if (this._nextSpawnTime != null) {
                    if (npcTemp.getMap().isPassable(npcTemp.getLocation(), npcTemp)) {
                        break;
                    }
                } else if (npcTemp.getMap().isInMap(npcTemp.getLocation()) && npcTemp.getMap().isPassable(npcTemp.getLocation(), npcTemp) && (npcTemp instanceof L1MonsterInstance)) {
                    if (!isRespawnScreen() && World.get().getVisiblePc(npcTemp.getLocation()).size() != 0) {
                        new SpawnTask(this, spawnNumber, npcTemp.getId(), 5000, null).getStart();
                        return;
                    }
                }
                tryCount++;
            }
            if (npcTemp instanceof L1MonsterInstance) {
                ((L1MonsterInstance) npcTemp).initHide();
            }
            npcTemp.setSpawn(this);
            npcTemp.setreSpawn(true);
            npcTemp.setSpawnNumber(spawnNumber);
            if (this._initSpawn && this._spawnHomePoint) {
                this._homePoint.put(Integer.valueOf(spawnNumber), new Point(npcTemp.getX(), npcTemp.getY()));
            }
            if (this._nextSpawnTime == null || isSpawnTime(npcTemp)) {
                if (npcTemp instanceof L1MonsterInstance) {
                    L1MonsterInstance mob = (L1MonsterInstance) npcTemp;
                    if (mob.getMapId() == 666) {
                        mob.set_storeDroped(true);
                    }
                }
                if (npcId == 45573 && npcTemp.getMapId() == 2) {
                    for (L1PcInstance pc : World.get().getAllPlayers()) {
                        if (pc.getMapId() == 2) {
                            L1Teleport.teleport(pc, 32664, 32797,  2, 0, true);
                        }
                    }
                }
                if ((npcId == 46142 && npcTemp.getMapId() == 73) || (npcId == 46141 && npcTemp.getMapId() == 74)) {
                    for (L1PcInstance pc2 : World.get().getAllPlayers()) {
                        if (pc2.getMapId() >= 72 && pc2.getMapId() <= 74) {
                            L1Teleport.teleport(pc2, 32840, 32833,  72, pc2.getHeading(), true);
                        }
                    }
                }
                switch (getMes_boss()) {
                    case 1:
                        World.get().broadcastPacketToAll(new S_ServerMessage("【" + getName() + "】" + L1WilliamSystemMessage.ShowMessage(9) + "。"));
                        break;
                    case 2:
                        World.get().broadcastPacketToAll(new S_PacketBoxGree1(2, "【" + getName() + "】" + L1WilliamSystemMessage.ShowMessage(9) + "。"));
                        break;
                    case 3:
                        World.get().broadcastPacketToAll(new S_ServerMessage("【" + getName() + "】" + L1WilliamSystemMessage.ShowMessage(9) + "。"));
                        World.get().broadcastPacketToAll(new S_PacketBoxGree1(2, "【" + getName() + "】" + L1WilliamSystemMessage.ShowMessage(9) + "。"));
                        break;
                }
                doCrystalCave(npcId);
                World.get().storeObject(npcTemp);
                World.get().addVisibleObject(npcTemp);
                if (npcTemp instanceof L1MonsterInstance) {
                    L1MonsterInstance mobtemp = (L1MonsterInstance) npcTemp;
                    if (!this._initSpawn && mobtemp.getHiddenStatus() == 0) {
                        mobtemp.onNpcAI();
                    }
                    if (this._existTime > 0) {
                        mobtemp.set_spawnTime(this._existTime * 60);
                    }
                }
                if (getGroupId() != 0) {
                    L1MobGroupSpawn.getInstance().doSpawn(npcTemp, getGroupId(), isRespawnScreen(), this._initSpawn);
                }
                npcTemp.turnOnOffLight();
                npcTemp.startChat(0);
                this._tmplocx = newlocx;
                this._tmplocy = newlocy;
                this._tmpmapid = npcTemp.getMapId();
                boolean setPassable = true;
                if (npcTemp instanceof L1DollInstance) {
                    setPassable = false;
                }
                if (npcTemp instanceof L1EffectInstance) {
                    setPassable = false;
                }
                if (npcTemp instanceof L1FieldObjectInstance) {
                    setPassable = false;
                }
                if (npcTemp instanceof L1FurnitureInstance) {
                    setPassable = false;
                }
                if (npcTemp instanceof L1DoorInstance) {
                    setPassable = false;
                }
                if (npcTemp instanceof L1BowInstance) {
                    setPassable = false;
                }
                if (setPassable) {
                    L1WorldMap.get().getMap(npcTemp.getMapId()).setPassable(npcTemp.getX(), npcTemp.getY(), false, 2);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void setRest(boolean flag) {
        this._rest = flag;
    }

    public boolean isRest() {
        return this._rest;
    }

    private int getSpawnType() {
        return this._spawnType;
    }

    public void setSpawnType(int type) {
        this._spawnType = type;
    }

    private boolean isAreaSpawn() {
        return (getLocX1() == 0 || getLocY1() == 0 || getLocX2() == 0 || getLocY2() == 0) ? false : true;
    }

    private boolean isRandomSpawn() {
        return (getRandomx() == 0 && getRandomy() == 0) ? false : true;
    }

    public L1SpawnTime getTime() {
        return this._time;
    }

    public void setTime(L1SpawnTime time) {
        this._time = time;
    }

    public void setMes_boss(int mes_boss) {
        this._mes_boss = mes_boss;
    }

    public int getMes_boss() {
        return this._mes_boss;
    }

    public void setMessage(int message) {
        this._message = message;
    }

    public int getMessage() {
        return this._message;
    }

    public void setMessage_boss(int message_boss) {
        this._message_boss = message_boss;
    }

    public int getMessage_boss() {
        return this._message_boss;
    }

    @Override // com.lineage.server.model.gametime.L1GameTimeAdapter, com.lineage.server.model.gametime.L1GameTimeListener
    public void onMinuteChanged(L1GameTime time) {
        if (!this._time.getTimePeriod().includes(time)) {
            synchronized (this._mobs) {
                if (!this._mobs.isEmpty()) {
                    for (L1NpcInstance mob : this._mobs) {
                        mob.setCurrentHpDirect(0);
                        mob.setDead(true);
                        mob.setStatus(8);
                        mob.deleteMe();
                    }
                    this._mobs.clear();
                }
            }
        }
    }

    public static void doCrystalCave(int npcId) {
        int[] npcId2 = {46143, 46144, 46145, 46146, 46147, 46148, 46149, 46150, 46151, 46152};
        int[] doorId = {L1SkillId.SEXP20, L1SkillId.SEXP13, L1SkillId.SEXP15, L1SkillId.SEXP17, L1SkillId.REEXP20, 5006, 5007, 5008, 5009, 5010};
        for (int i = 0; i < npcId2.length; i++) {
            if (npcId == npcId2[i]) {
                closeDoorInCrystalCave(doorId[i]);
            }
        }
    }

    private static void closeDoorInCrystalCave(int doorId) {
        for (L1Object object : World.get().getObject()) {
            if (object instanceof L1DoorInstance) {
                L1DoorInstance door = (L1DoorInstance) object;
                if (door.getDoorId() == doorId) {
                    door.close();
                }
            }
        }
    }
}
