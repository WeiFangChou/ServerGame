package com.lineage.server.templates;

import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1MobGroupSpawn;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldSpawnBoss;
import java.sql.Timestamp;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1SpawnBoss extends L1SpawnEx {
    private static final Log _log = LogFactory.getLog(L1SpawnBoss.class);
    private static final long serialVersionUID = 6316427673712019172L;

    public L1SpawnBoss(L1Npc mobTemplate) {
        this._template = mobTemplate;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public L1Npc get_template() {
        return this._template;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public int get_id() {
        return this._id;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public void set_id(int id) {
        this._id = id;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public int get_maximumCount() {
        return this._maximumCount;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public void set_maximumCount(int maximumCount) {
        this._maximumCount = maximumCount;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public int get_npcid() {
        return this._npcid;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public void set_npcid(int npcid) {
        this._npcid = npcid;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public int get_groupId() {
        return this._groupId;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public void set_groupId(int groupId) {
        this._groupId = groupId;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public int get_tmplocx() {
        return this._tmplocx;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public void set_tmplocx(int tmplocx) {
        this._tmplocx = tmplocx;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public int get_tmplocy() {
        return this._tmplocy;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public void set_tmplocy(int tmplocy) {
        this._tmplocy = tmplocy;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public short get_tmpmapid() {
        return this._tmpmapid;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public void set_tmpmapid(short tmpmapid) {
        this._tmpmapid = tmpmapid;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public int get_locx1() {
        return this._locx1;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public void set_locx1(int locx1) {
        this._locx1 = locx1;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public int get_locy1() {
        return this._locy1;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public void set_locy1(int locy1) {
        this._locy1 = locy1;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public int get_locx2() {
        return this._locx2;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public void set_locx2(int locx2) {
        this._locx2 = locx2;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public int get_locy2() {
        return this._locy2;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public void set_locy2(int locy2) {
        this._locy2 = locy2;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public int get_heading() {
        return this._heading;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public void set_heading(int heading) {
        this._heading = heading;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public short get_mapid() {
        return this._mapid;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public void set_mapid(short mapid) {
        this._mapid = mapid;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public Timestamp get_nextSpawnTime() {
        return this._nextSpawnTime;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public void set_nextSpawnTime(Timestamp nextSpawnTime) {
        this._nextSpawnTime = nextSpawnTime;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public long get_spawnInterval() {
        return (long) this._spawnInterval;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public void set_spawnInterval(int spawnInterval) {
        this._spawnInterval = spawnInterval;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public int get_existTime() {
        return this._existTime;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public void set_existTime(int existTime) {
        this._existTime = existTime;
    }

    private boolean isAreaSpawn() {
        return (this._locx1 == 0 || this._locy1 == 0 || this._locx2 == 0 || this._locy2 == 0) ? false : true;
    }

    @Override // com.lineage.server.templates.L1SpawnEx
    public void doSpawn(int objectId) {
        if (!new Timestamp(System.currentTimeMillis()).before(this._nextSpawnTime)) {
            this._tmplocx = 0;
            this._tmplocy = 0;
            this._tmpmapid = 0;
            try {
                int newlocx = this._locx1;
                int newlocy = this._locy1;
                L1NpcInstance npcTemp = NpcTable.get().newNpcInstance(this._template);
                if (objectId == 0) {
                    npcTemp.setId(IdFactoryNpc.get().nextId());
                } else {
                    npcTemp.setId(objectId);
                }
                npcTemp.setHeading(Math.min(this._heading, 7));
                int npcId = npcTemp.getNpcTemplate().get_npcId();
                if (npcId == 45488 && this._mapid == 9) {
                    npcTemp.setMap((short) (this._mapid + _random.nextInt(2)));
                } else if (npcId == 45601 && this._mapid == 11) {
                    npcTemp.setMap((short) (this._mapid + _random.nextInt(3)));
                } else {
                    npcTemp.setMap(this._mapid);
                }
                npcTemp.setMovementDistance(80);
                npcTemp.setRest(false);
                for (int tryCount = 0; tryCount <= 50; tryCount++) {
                    if (isAreaSpawn()) {
                        int rangeX = this._locx2 - this._locx1;
                        int rangeY = this._locy2 - this._locy1;
                        newlocx = _random.nextInt(rangeX) + this._locx1;
                        newlocy = _random.nextInt(rangeY) + this._locy1;
                    } else {
                        newlocx = this._locx1;
                        newlocy = this._locy1;
                    }
                    npcTemp.setX(newlocx);
                    npcTemp.setHomeX(newlocx);
                    npcTemp.setY(newlocy);
                    npcTemp.setHomeY(newlocy);
                    if (npcTemp.getMap().isInMap(npcTemp.getLocation()) && npcTemp.getMap().isPassable(npcTemp.getLocation(), npcTemp)) {
                        break;
                    }
                }
                if (npcTemp instanceof L1MonsterInstance) {
                    ((L1MonsterInstance) npcTemp).initHide();
                }
                npcTemp.setSpawn(null);
                npcTemp.setreSpawn(true);
                npcTemp.setSpawnNumber(this._id);
                if (npcId == 45573 && npcTemp.getMapId() == 2) {
                    for (L1PcInstance pc : World.get().getAllPlayers()) {
                        if (pc.getMapId() == 2) {
                            L1Teleport.teleport(pc, 32664, 32797, (short) 2, 0, true);
                        }
                    }
                }
                if ((npcId == 46142 && npcTemp.getMapId() == 73) || (npcId == 46141 && npcTemp.getMapId() == 74)) {
                    for (L1PcInstance pc2 : World.get().getAllPlayers()) {
                        if (pc2.getMapId() >= 72 && pc2.getMapId() <= 74) {
                            L1Teleport.teleport(pc2, 32840, 32833, (short) 72, pc2.getHeading(), true);
                        }
                    }
                }
                World.get().storeObject(npcTemp);
                World.get().addVisibleObject(npcTemp);
                if (npcTemp instanceof L1MonsterInstance) {
                    L1MonsterInstance mobtemp = (L1MonsterInstance) npcTemp;
                    if (mobtemp.getHiddenStatus() == 0) {
                        mobtemp.onNpcAI();
                    }
                    if (this._existTime > 0) {
                        mobtemp.set_spawnTime(this._existTime * L1SkillId.STATUS_BRAVE);
                    }
                }
                if (this._groupId != 0) {
                    L1MobGroupSpawn.getInstance().doSpawn(npcTemp, this._groupId, true, true);
                }
                npcTemp.turnOnOffLight();
                npcTemp.startChat(0);
                this._tmplocx = newlocx;
                this._tmplocy = newlocy;
                this._tmpmapid = npcTemp.getMapId();
                L1WorldMap.get().getMap(npcTemp.getMapId()).setPassable(npcTemp.getX(), npcTemp.getY(), false, 2);
                if (WorldSpawnBoss.get().get(Integer.valueOf(npcTemp.getId())) == null) {
                    WorldSpawnBoss.get().put(Integer.valueOf(npcTemp.getId()), this);
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public void init() {
        for (int spawnNum = 0; spawnNum < this._maximumCount; spawnNum++) {
            try {
                setSpawn();
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
                return;
            }
        }
    }

    public void setSpawn() {
        try {
            doSpawn(0);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
