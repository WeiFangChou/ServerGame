package com.lineage.server.utils;

import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.datatables.lock.FurnitureSpawnReading;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1FieldObjectInstance;
import com.lineage.server.model.Instance.L1FurnitureInstance;
import com.lineage.server.model.Instance.L1IllusoryInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.templates.L1Furniture;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldEffect;
import com.lineage.server.world.WorldQuest;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1SpawnUtil {
    private static final byte[] HEADING_TABLE_X;
    private static final byte[] HEADING_TABLE_Y;
    private static final Log _log = LogFactory.getLog(L1SpawnUtil.class);

    static {
        byte[] bArr = new byte[8];
        bArr[1] = 1;
        bArr[2] = 1;
        bArr[3] = 1;
        bArr[5] = -1;
        bArr[6] = -1;
        bArr[7] = -1;
        HEADING_TABLE_X = bArr;
        byte[] bArr2 = new byte[8];
        bArr2[0] = -1;
        bArr2[1] = -1;
        bArr2[3] = 1;
        bArr2[4] = 1;
        bArr2[5] = 1;
        bArr2[7] = -1;
        HEADING_TABLE_Y = bArr2;
    }

    public static void spawn(L1PcInstance pc, int npcId, int r, int t) {
        new L1SpawnUtil().spawnR(pc.getLocation(), npcId, pc.get_showId(), r, t);
    }

    public static L1NpcInstance spawnRx(L1Location loc, int npcId, int showid, int r, int time) {
        try {
            L1NpcInstance npc = NpcTable.get().newNpcInstance(npcId);
            if (npc == null) {
                return null;
            }
            npc.setId(IdFactoryNpc.get().nextId());
            npc.setMap(loc.getMap());
            if (r == 0) {
                npc.getLocation().set(loc);
            } else {
                int tryCount = 0;
                do {
                    tryCount++;
                    npc.setX((loc.getX() + ((int) (Math.random() * ((double) r)))) - ((int) (Math.random() * ((double) r))));
                    npc.setY((loc.getY() + ((int) (Math.random() * ((double) r)))) - ((int) (Math.random() * ((double) r))));
                    if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation(), npc)) {
                        break;
                    }
                    Thread.sleep(2);
                } while (tryCount < 50);
                if (tryCount >= 50) {
                    npc.getLocation().set(loc);
                }
            }
            npc.setHomeX(npc.getX());
            npc.setHomeY(npc.getY());
            npc.setHeading(5);
            npc.set_showId(showid);
            L1QuestUser q = WorldQuest.get().get(showid);
            if (q != null) {
                q.addNpc(npc);
            }
            World.get().storeObject(npc);
            World.get().addVisibleObject(npc);
            npc.turnOnOffLight();
            npc.startChat(0);
            if (time <= 0) {
                return npc;
            }
            npc.set_spawnTime(time);
            return npc;
        } catch (Exception e) {
            _log.error("依LOC位置召喚指定NPC(傳回NPC訊息)發生異常: " + npcId, e);
            return null;
        }
    }

    public static void spawnR(L1Location loc, int npcId, int showid, int randomRange) {
        new L1SpawnUtil().spawnR(loc, npcId, showid, randomRange, 0);
    }

    private void spawnR(L1Location loc, int npcId, int showid, int randomRange, int timeMillisToDelete) {
        GeneralThreadPool.get().schedule(new SpawnR1(this, loc, npcId, showid, randomRange, timeMillisToDelete, null), 0);
    }

    /* access modifiers changed from: private */
    public class SpawnR1 implements Runnable {
        final L1Location _location;
        final int _npcId;
        final int _randomRange;
        final int _showid;
        final int _timeMillisToDelete;

        /* synthetic */ SpawnR1(L1SpawnUtil l1SpawnUtil, L1Location l1Location, int i, int i2, int i3, int i4, SpawnR1 spawnR1) {
            this(l1Location, i, i2, i3, i4);
        }

        private SpawnR1(L1Location location, int npcId, int showid, int randomRange, int timeMillisToDelete) {
            this._location = location;
            this._npcId = npcId;
            this._showid = showid;
            this._randomRange = randomRange;
            this._timeMillisToDelete = timeMillisToDelete;
        }

        public void run() {
            try {
                L1NpcInstance npc = NpcTable.get().newNpcInstance(this._npcId);
                if (npc != null) {
                    npc.setId(IdFactoryNpc.get().nextId());
                    npc.setMap(this._location.getMap());
                    if (this._randomRange == 0) {
                        npc.getLocation().set(this._location);
                    } else {
                        int tryCount = 0;
                        do {
                            tryCount++;
                            npc.setX((this._location.getX() + ((int) (Math.random() * ((double) this._randomRange)))) - ((int) (Math.random() * ((double) this._randomRange))));
                            npc.setY((this._location.getY() + ((int) (Math.random() * ((double) this._randomRange)))) - ((int) (Math.random() * ((double) this._randomRange))));
                            if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation(), npc)) {
                                break;
                            }
                            Thread.sleep(2);
                        } while (tryCount < 50);
                        if (tryCount >= 50) {
                            npc.getLocation().set(this._location);
                        }
                    }
                    npc.setHomeX(npc.getX());
                    npc.setHomeY(npc.getY());
                    npc.setHeading(5);
                    npc.set_showId(this._showid);
                    L1QuestUser q = WorldQuest.get().get(this._showid);
                    if (q != null) {
                        q.addNpc(npc);
                    }
                    World.get().storeObject(npc);
                    World.get().addVisibleObject(npc);
                    npc.turnOnOffLight();
                    npc.startChat(0);
                    if (this._timeMillisToDelete > 0) {
                        npc.set_spawnTime(this._timeMillisToDelete);
                    }
                }
            } catch (Exception e) {
                L1SpawnUtil._log.error("執行NPC召喚發生異常: " + this._npcId, e);
            }
        }
    }

    public static void spawn(L1Furniture temp) {
        try {
            L1NpcInstance npc = NpcTable.get().newNpcInstance(temp.get_npcid());
            if (npc != null) {
                L1FurnitureInstance furniture = (L1FurnitureInstance) npc;
                furniture.setId(IdFactoryNpc.get().nextId());
                furniture.setMap(temp.get_mapid());
                furniture.setX(temp.get_locx());
                furniture.setY(temp.get_locy());
                furniture.setHomeX(furniture.getX());
                furniture.setHomeY(furniture.getY());
                furniture.setHeading(0);
                furniture.setItemObjId(temp.get_item_obj_id());
                World.get().storeObject(furniture);
                World.get().addVisibleObject(furniture);
            }
        } catch (Exception e) {
            _log.error("執行家具召喚發生異常!", e);
        }
    }

    public static L1FurnitureInstance spawn(L1PcInstance pc, int npcid, int itemObjectId) {
        try {
            L1NpcInstance npc = NpcTable.get().newNpcInstance(npcid);
            if (npc == null) {
                return null;
            }
            L1FurnitureInstance furniture = (L1FurnitureInstance) npc;
            furniture.setId(IdFactoryNpc.get().nextId());
            furniture.setMap(pc.getMapId());
            if (pc.getHeading() == 0) {
                furniture.setX(pc.getX());
                furniture.setY(pc.getY() - 1);
            } else if (pc.getHeading() == 2) {
                furniture.setX(pc.getX() + 1);
                furniture.setY(pc.getY());
            }
            furniture.setHomeX(furniture.getX());
            furniture.setHomeY(furniture.getY());
            furniture.setHeading(0);
            furniture.setItemObjId(itemObjectId);
            World.get().storeObject(furniture);
            World.get().addVisibleObject(furniture);
            FurnitureSpawnReading.get().insertFurniture(furniture);
            return furniture;
        } catch (Exception e) {
            _log.error("執行家具召喚發生異常!", e);
            return null;
        }
    }

    public static L1NpcInstance spawn(int npcid, int x, int y, int m, int h) {
        return spawnT(npcid, x, y, m, h, 0);
    }

    public static L1NpcInstance spawnT(int npcid, int x, int y, int m, int h, int time) {
        try {
            L1NpcInstance npc = NpcTable.get().newNpcInstance(npcid);
            if (npc == null) {
                return null;
            }
            npc.setId(IdFactoryNpc.get().nextId());
            npc.setMap(m);
            npc.setX(x);
            npc.setY(y);
            npc.setHomeX(npc.getX());
            npc.setHomeY(npc.getY());
            npc.setHeading(h);
            World.get().storeObject(npc);
            World.get().addVisibleObject(npc);
            npc.turnOnOffLight();
            npc.startChat(0);
            if (time <= 0) {
                return npc;
            }
            npc.set_spawnTime(time);
            return npc;
        } catch (Exception e) {
            _log.error("執行NPC召喚發生異常: " + npcid, e);
            return null;
        }
    }

    public static L1IllusoryInstance spawn(L1PcInstance pc, L1Location loc, int h, int time) {
        try {
            L1NpcInstance npc = NpcTable.get().newNpcInstance(81003);
            if (npc == null) {
                return null;
            }
            L1IllusoryInstance ill = (L1IllusoryInstance) npc;
            ill.setId(IdFactoryNpc.get().nextId());
            ill.setMap(loc.getMap());
            ill.setX(loc.getX());
            ill.setY(loc.getY());
            ill.setHomeX(ill.getX());
            ill.setHomeY(ill.getY());
            ill.setHeading(h);
            ill.setNameId("分身");
            ill.setTitle(String.valueOf(pc.getName()) + " 的");
            ill.setMaster(pc);
            ill.set_showId(pc.get_showId());
            L1QuestUser q = WorldQuest.get().get(pc.get_showId());
            if (q != null) {
                q.addNpc(npc);
            }
            World.get().storeObject(ill);
            World.get().addVisibleObject(ill);
            if (time > 0) {
                ill.set_spawnTime(time);
            }
            if (pc.getWeapon() != null) {
                ill.setStatus(pc.getWeapon().getItem().getType1());
                if (pc.getWeapon().getItem().getRange() != -1) {
                    ill.set_ranged(2);
                } else {
                    ill.set_ranged(10);
                    ill.setBowActId(66);
                }
            }
            ill.setLevel((int) (((double) pc.getLevel()) * 0.7d));
            ill.setStr((int) (((double) pc.getStr()) * 0.7d));
            ill.setCon((int) (((double) pc.getCon()) * 0.7d));
            ill.setDex((int) (((double) pc.getDex()) * 0.7d));
            ill.setInt((int) (((double) pc.getInt()) * 0.7d));
            ill.setWis((int) (((double) pc.getWis()) * 0.7d));
            ill.setMaxMp(10);
            ill.setCurrentMpDirect(10);
            ill.setTempCharGfx(pc.getTempCharGfx());
            ill.setGfxId(pc.getGfxId());
            int attack = SprTable.get().getAttackSpeed(pc.getGfxId(), 1);
            ill.setPassispeed(SprTable.get().getMoveSpeed(pc.getGfxId(), ill.getStatus()));
            ill.setAtkspeed(attack);
            ill.setBraveSpeed(pc.getBraveSpeed());
            ill.setMoveSpeed(pc.getMoveSpeed());
            return ill;
        } catch (Exception e) {
            _log.error("執行分身召喚發生異常!", e);
            return null;
        }
    }

    public static L1MonsterInstance spawnX(int npcid, L1Location loc, int show_id) {
        try {
            L1NpcInstance npc = NpcTable.get().newNpcInstance(npcid);
            if (npc == null) {
                return null;
            }
            L1MonsterInstance mob = (L1MonsterInstance) npc;
            mob.setId(IdFactoryNpc.get().nextId());
            mob.setMap(loc.getMap());
            mob.setX(loc.getX());
            mob.setY(loc.getY());
            mob.setHomeX(mob.getX());
            mob.setHomeY(mob.getY());
            mob.set_showId(show_id);
            L1QuestUser q = WorldQuest.get().get(show_id);
            if (q != null) {
                q.addNpc(npc);
            }
            mob.setMovementDistance(20);
            World.get().storeObject(mob);
            World.get().addVisibleObject(mob);
            return mob;
        } catch (Exception e) {
            _log.error("執行召喚救援發生異常!", e);
            return null;
        }
    }

    public static L1MonsterInstance spawnParty(L1NpcInstance master, int npcid, L1Location loc) {
        try {
            L1NpcInstance npc = NpcTable.get().newNpcInstance(npcid);
            if (npc == null) {
                return null;
            }
            L1MonsterInstance mob = (L1MonsterInstance) npc;
            mob.setId(IdFactoryNpc.get().nextId());
            mob.setMap(loc.getMap());
            mob.setX(loc.getX());
            mob.setY(loc.getY());
            mob.setHomeX(mob.getX());
            mob.setHomeY(mob.getY());
            mob.setHeading(master.getHeading());
            mob.setMaster(master);
            mob.set_showId(master.get_showId());
            L1QuestUser q = WorldQuest.get().get(master.get_showId());
            if (q != null) {
                q.addNpc(npc);
            }
            World.get().storeObject(mob);
            World.get().addVisibleObject(mob);
            return mob;
        } catch (Exception e) {
            _log.error("執行召喚指定對員發生異常!", e);
            return null;
        }
    }

    public static L1NpcInstance spawn(int npcid, L1Location loc) {
        try {
            L1NpcInstance npc = NpcTable.get().newNpcInstance(npcid);
            if (npc == null) {
                return null;
            }
            npc.setId(IdFactoryNpc.get().nextId());
            npc.setMap(loc.getMap());
            npc.setX(loc.getX());
            npc.setY(loc.getY());
            npc.setHomeX(npc.getX());
            npc.setHomeY(npc.getY());
            World.get().storeObject(npc);
            World.get().addVisibleObject(npc);
            npc.turnOnOffLight();
            return npc;
        } catch (Exception e) {
            _log.error("執行分身召喚發生異常!", e);
            return null;
        }
    }

    public static L1NpcInstance spawn(int npcid, L1Location loc, int heading, int showId) {
        try {
            L1NpcInstance npc = NpcTable.get().newNpcInstance(npcid);
            if (npc == null) {
                return null;
            }
            npc.setId(IdFactoryNpc.get().nextId());
            npc.setMap(loc.getMap());
            npc.setX(loc.getX());
            npc.setY(loc.getY());
            npc.setHomeX(npc.getX());
            npc.setHomeY(npc.getY());
            npc.setHeading(heading);
            npc.set_showId(showId);
            L1QuestUser q = WorldQuest.get().get(showId);
            if (q != null) {
                q.addNpc(npc);
            }
            World.get().storeObject(npc);
            World.get().addVisibleObject(npc);
            npc.turnOnOffLight();
            return npc;
        } catch (Exception e) {
            _log.error("執行副本NPC召喚發生異常!", e);
            return null;
        }
    }

    public static L1NpcInstance spawn(int npcid, int x, int y, short m, int h, int gfxid) {
        try {
            L1NpcInstance npc = NpcTable.get().newNpcInstance(npcid);
            if (npc == null) {
                return null;
            }
            npc.setId(IdFactoryNpc.get().nextId());
            npc.setMap(m);
            npc.setX(x);
            npc.setY(y);
            npc.setHomeX(npc.getX());
            npc.setHomeY(npc.getY());
            npc.setHeading(h);
            npc.setTempCharGfx(gfxid);
            npc.setGfxId(gfxid);
            World.get().storeObject(npc);
            World.get().addVisibleObject(npc);
            npc.turnOnOffLight();
            return npc;
        } catch (Exception e) {
            _log.error("執行NPC召喚發生異常: " + npcid, e);
            return null;
        }
    }

    public static void doSpawnFireWall(L1Character cha, int targetX, int targetY) {
        new L1SpawnUtil().doSpawnFireWallR(cha, targetX, targetY);
    }

    public void doSpawnFireWallR(L1Character cha, int targetX, int targetY) {
        GeneralThreadPool.get().schedule(new SpawnR2(this, cha, targetX, targetY, null), 0);
    }

    /* access modifiers changed from: private */
    public class SpawnR2 implements Runnable {
        private final L1Character _cha;
        private final int _targetX;
        private final int _targetY;

        private SpawnR2(L1Character cha, int targetX, int targetY) {
            this._cha = cha;
            this._targetX = targetX;
            this._targetY = targetY;
        }

        /* synthetic */ SpawnR2(L1SpawnUtil l1SpawnUtil, L1Character l1Character, int i, int i2, SpawnR2 spawnR2) {
            this(l1Character, i, i2);
        }

        public void run() {
            try {
                L1Npc firewall = NpcTable.get().getTemplate(81157);
                int duration = SkillsTable.get().getTemplate(58).getBuffDuration();
                if (firewall != null) {
                    L1Character base = this._cha;
                    for (int i = 0; i < 8; i++) {
                        Thread.sleep(2);
                        int tmp = 0;
                        Iterator<L1Object> it = World.get().getVisibleObjects(this._cha).iterator();
                        while (it.hasNext()) {
                            L1Object objects = it.next();
                            if (objects != null && (objects instanceof L1EffectInstance)) {
                                L1EffectInstance effect = (L1EffectInstance) objects;
                                if (this._cha != null && effect.getMaster().equals(this._cha)) {
                                    tmp++;
                                }
                            }
                        }
                        if (tmp < 24) {
                            int a = base.targetDirection(this._targetX, this._targetY);
                            int x = base.getX();
                            int y = base.getY();
                            int x2 = x + L1SpawnUtil.HEADING_TABLE_X[a];
                            int y2 = y + L1SpawnUtil.HEADING_TABLE_Y[a];
                            if (!base.isAttackPosition(x2, y2, 1)) {
                                x2 = base.getX();
                                y2 = base.getY();
                            }
                            if (!WorldEffect.get().isEffect(new L1Location(x2, y2, this._cha.getMapId()), 81157)) {
                                if (!L1WorldMap.get().getMap(this._cha.getMapId()).isArrowPassable(x2, y2, this._cha.getHeading())) {
                                    return;
                                }
                                if (this._cha instanceof L1PcInstance) {
                                    L1PcInstance user = (L1PcInstance) this._cha;
                                    base = L1SpawnUtil.spawnEffect(81157, duration, x2, y2, user.getMapId(), user, 58);
                                } else {
                                    base = L1SpawnUtil.spawnEffect(81157, duration, x2, y2, this._cha.getMapId(), null, 58);
                                }
                            }
                        } else {
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                L1SpawnUtil._log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public static L1EffectInstance spawnEffect(int npcId, int time, int locX, int locY, int mapId, L1Character user, int skiiId) {
        L1EffectInstance effect = null;
        try {
            L1NpcInstance npc = NpcTable.get().newNpcInstance(npcId);
            if (npc == null) {
                return null;
            }
            effect = (L1EffectInstance) npc;
            effect.setId(IdFactoryNpc.get().nextId());
            effect.setGfxId(effect.getGfxId());
            effect.setX(locX);
            effect.setY(locY);
            effect.setHomeX(locX);
            effect.setHomeY(locY);
            effect.setHeading(0);
            effect.setMap(mapId);
            if (user != null) {
                effect.setMaster(user);
                effect.set_showId(user.get_showId());
                L1QuestUser q = WorldQuest.get().get(user.get_showId());
                if (q != null) {
                    q.addNpc(npc);
                }
            }
            effect.setSkillId(skiiId);
            World.get().storeObject(effect);
            World.get().addVisibleObject(effect);
            effect.broadcastPacketAll(new S_NPCPack(effect));
            Iterator<L1PcInstance> it = World.get().getRecognizePlayer(effect).iterator();
            while (it.hasNext()) {
                L1PcInstance pc = it.next();
                effect.addKnownObject(pc);
                pc.addKnownObject(effect);
            }
            if (time > 0) {
                effect.set_spawnTime(time);
            }
            return effect;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public static L1EffectInstance spawnEffect(int npcId, int time, int locX, int locY, int mapId, L1Character user, int skiiId, int gfxid) {
        L1EffectInstance effect = null;
        try {
            L1NpcInstance npc = NpcTable.get().newNpcInstance(npcId);
            if (npc == null) {
                return null;
            }
            effect = (L1EffectInstance) npc;
            effect.setId(IdFactoryNpc.get().nextId());
            effect.setGfxId(gfxid);
            effect.setTempCharGfx(gfxid);
            effect.setX(locX);
            effect.setY(locY);
            effect.setHomeX(locX);
            effect.setHomeY(locY);
            effect.setHeading(0);
            effect.setMap(mapId);
            if (user != null) {
                effect.setMaster(user);
                effect.set_showId(user.get_showId());
                L1QuestUser q = WorldQuest.get().get(user.get_showId());
                if (q != null) {
                    q.addNpc(npc);
                }
            }
            effect.setSkillId(skiiId);
            World.get().storeObject(effect);
            World.get().addVisibleObject(effect);
            effect.broadcastPacketAll(new S_NPCPack(effect));
            Iterator<L1PcInstance> it = World.get().getRecognizePlayer(effect).iterator();
            while (it.hasNext()) {
                L1PcInstance pc = it.next();
                effect.addKnownObject(pc);
                pc.addKnownObject(effect);
            }
            if (time > 0) {
                effect.set_spawnTime(time);
            }
            return effect;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public static L1DoorInstance spawnDoor(L1QuestUser quest, int doorId, int gfxid, int x, int y, int mapid, int direction) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        L1Npc l1npc = NpcTable.get().getTemplate(81158);
        if (l1npc == null) {
            return null;
        }
        L1DoorInstance door = (L1DoorInstance) NpcTable.get().newNpcInstance(l1npc);
        door.setId(IdFactoryNpc.get().nextId());
        door.setDoorId(doorId);
        door.setGfxId(gfxid);
        door.setX(x);
        door.setY(y);
        door.setMap(mapid);
        door.setHomeX(x);
        door.setHomeY(y);
        door.setDirection(direction);
        switch (gfxid) {
            case 88:
                if (mapid != 9000) {
                    door.setLeftEdgeLocation(x - 1);
                    door.setRightEdgeLocation(x + 1);
                    break;
                } else {
                    door.setLeftEdgeLocation(x - 1);
                    door.setRightEdgeLocation(x + 1);
                    break;
                }
            case L1SkillId.BOUNCE_ATTACK:
                door.setLeftEdgeLocation(y);
                door.setRightEdgeLocation(y);
                break;
            case 90:
                door.setLeftEdgeLocation(x);
                door.setRightEdgeLocation(x + 1);
                break;
            case 7556:
                door.setLeftEdgeLocation(y - 1);
                door.setRightEdgeLocation(y + 3);
                break;
            case 7858:
                door.setLeftEdgeLocation(x - 2);
                door.setRightEdgeLocation(x + 3);
                break;
            case 7859:
                door.setLeftEdgeLocation(y - 2);
                door.setRightEdgeLocation(y + 3);
                break;
            default:
                door.setLeftEdgeLocation(y);
                door.setRightEdgeLocation(y);
                break;
        }
        door.setMaxHp(0);
        door.setCurrentHp(0);
        door.setKeeperId(0);
        if (quest != null) {
            door.set_showId(quest.get_id());
            quest.addNpc(door);
        }
        door.close();
        World.get().storeObject(door);
        World.get().addVisibleObject(door);
        return door;
    }

    public static L1FieldObjectInstance spawn(int showid, int gfxid, int x, int y, int map, int timeMillisToDelete) {
        try {
            L1FieldObjectInstance field = (L1FieldObjectInstance) NpcTable.get().newNpcInstance(71081);
            if (field != null) {
                field.setId(IdFactoryNpc.get().nextId());
                field.setGfxId(gfxid);
                field.setTempCharGfx(gfxid);
                field.setMap( map);
                field.setX(x);
                field.setY(y);
                field.setHomeX(x);
                field.setHomeY(y);
                field.setHeading(5);
                field.set_showId(showid);
                L1QuestUser q = WorldQuest.get().get(showid);
                if (q != null) {
                    q.addNpc(field);
                }
                World.get().storeObject(field);
                World.get().addVisibleObject(field);
                if (timeMillisToDelete <= 0) {
                    return field;
                }
                field.set_spawnTime(timeMillisToDelete);
                return field;
            }
        } catch (Exception e) {
            _log.error("執行景觀(副本專用)召喚發生異常", e);
        }
        return null;
    }
}
