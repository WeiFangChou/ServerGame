package com.lineage.server.model.Instance;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigOther;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.drop.SetDrop;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_HPMeter;
import com.lineage.server.serverpackets.S_NPCPack_Summon;
import com.lineage.server.serverpackets.S_PetMenuPacket;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1SummonInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1SummonInstance.class);
    private static Random _random = new Random();
    private static final int _summonTime = 3600;
    private static final long serialVersionUID = 1;
    private int _checkMove = 0;
    private int _currentPetStatus;
    private boolean _isReturnToNature = false;
    private boolean _tamed;
    private int _tempModel = 3;
    private int _time = 0;

    public boolean tamed() {
        return this._tamed;
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public boolean noTarget() {
        if (ConfigOther.WAR_summon) {
            for (int castle_id = 1; castle_id < 8; castle_id++) {
                if (ServerWarExecutor.get().isNowWar(castle_id) && L1CastleLocation.checkInWarArea(castle_id, this)) {
                    deleteMe();
                    return true;
                }
            }
        }
        switch (this._currentPetStatus) {
            case 3:
                return true;
            case 4:
                if (this._master != null && this._master.getMapId() == getMapId() && getLocation().getTileLineDistance(this._master.getLocation()) < 5) {
                    if (this._npcMove != null) {
                        this._npcMove.setDirectionMove(this._npcMove.checkObject(this._npcMove.targetReverseDirection(this._master.getX(), this._master.getY())));
                        setSleepTime(calcSleepTime(getPassispeed(), 0));
                        break;
                    }
                } else {
                    this._currentPetStatus = 3;
                    return true;
                }
                break;
            case 5:
                if ((Math.abs(getHomeX() - getX()) > 1 || Math.abs(getHomeY() - getY()) > 1) && this._npcMove != null) {
                    int dir = this._npcMove.moveDirection(getHomeX(), getHomeY());
                    if (dir == -1) {
                        setHomeX(getX());
                        setHomeY(getY());
                        break;
                    } else {
                        this._npcMove.setDirectionMove(dir);
                        setSleepTime(calcSleepTime(getPassispeed(), 0));
                        break;
                    }
                }
            default:
                if (this._master != null && this._master.getMapId() == getMapId()) {
                    if (getLocation().getTileLineDistance(this._master.getLocation()) > 2 && this._npcMove != null) {
                        int dir2 = this._npcMove.moveDirection(this._master.getX(), this._master.getY());
                        if (dir2 == -1) {
                            this._checkMove++;
                            if (this._checkMove >= 10) {
                                this._checkMove = 0;
                                this._currentPetStatus = 3;
                                return true;
                            }
                        } else {
                            this._checkMove = 0;
                            this._npcMove.setDirectionMove(dir2);
                            setSleepTime(calcSleepTime(getPassispeed(), 0));
                        }
                    }
                    if (getLocation().getTileLineDistance(this._master.getLocation()) > 7) {
                        teleport(this, getMaster().getX(), getMaster().getY(), getMaster().getMapId(), 0);
                        break;
                    }
                } else {
                    this._currentPetStatus = 3;
                    return true;
                }
                break;
        }
        return false;
    }

    public L1SummonInstance(L1Npc template, L1Character master) {
        super(template);
        setId(IdFactoryNpc.get().nextId());
        set_showId(master.get_showId());
        set_time(_summonTime);
        setMaster(master);
        setX((master.getX() + _random.nextInt(5)) - 2);
        setY((master.getY() + _random.nextInt(5)) - 2);
        setMap(master.getMapId());
        setHeading(5);
        setLightSize(template.getLightSize());
        this._currentPetStatus = 3;
        this._tamed = false;
        World.get().storeObject(this);
        World.get().addVisibleObject(this);
        Iterator<L1PcInstance> it = World.get().getRecognizePlayer(this).iterator();
        while (it.hasNext()) {
            onPerceive(it.next());
        }
        master.addPet(this);
        if (master instanceof L1PcInstance) {
            addMaster((L1PcInstance) master);
        } else if (master instanceof L1NpcInstance) {
            this._currentPetStatus = 1;
        }
    }

    public L1SummonInstance(L1NpcInstance target, L1Character master, boolean isCreateZombie) {
        super(null);
        setId(IdFactoryNpc.get().nextId());
        set_showId(master.get_showId());
        if (isCreateZombie) {
            int npcId = 45065;
            L1PcInstance pc = (L1PcInstance) master;
            int level = pc.getLevel();
            if (pc.isWizard()) {
                if (level >= 24 && level <= 31) {
                    npcId = 81183;
                } else if (level >= 32 && level <= 39) {
                    npcId = 81184;
                } else if (level >= 40 && level <= 43) {
                    npcId = 81185;
                } else if (level >= 44 && level <= 47) {
                    npcId = 81186;
                } else if (level >= 48 && level <= 51) {
                    npcId = 81187;
                } else if (level >= 52) {
                    npcId = 81188;
                }
            } else if (pc.isElf() && level >= 48) {
                npcId = 81183;
            }
            setting_template(NpcTable.get().getTemplate(npcId).clone());
        } else {
            setting_template(target.getNpcTemplate());
            setCurrentHpDirect(target.getCurrentHp());
            setCurrentMpDirect(target.getCurrentMp());
        }
        set_time(_summonTime);
        setMaster(master);
        setX(target.getX());
        setY(target.getY());
        setMap(target.getMapId());
        setHeading(target.getHeading());
        setLightSize(target.getLightSize());
        setPetcost(6);
        if ((target instanceof L1MonsterInstance) && !((L1MonsterInstance) target).is_storeDroped()) {
            new SetDrop().setDrop(target, target.getInventory());
        }
        setInventory(target.getInventory());
        target.setInventory(null);
        this._currentPetStatus = 3;
        this._tamed = true;
        for (L1NpcInstance each : master.getPetList().values()) {
            each.targetRemove(target);
        }
        target.deleteMe();
        World.get().storeObject(this);
        World.get().addVisibleObject(this);
        Iterator<L1PcInstance> it = World.get().getRecognizePlayer(this).iterator();
        while (it.hasNext()) {
            onPerceive(it.next());
        }
        master.addPet(this);
        if (master instanceof L1PcInstance) {
            addMaster((L1PcInstance) master);
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void receiveDamage(L1Character attacker, int damage) throws Exception {
        this.ISASCAPE = false;
        if (getCurrentHp() > 0) {
            if (damage > 0) {
                setHate(attacker, 0);
                removeSkillEffect(66);
                if (!isExsistMaster()) {
                    this._currentPetStatus = 1;
                    setTarget(attacker);
                }
            }
            if ((attacker instanceof L1PcInstance) && damage > 0) {
                ((L1PcInstance) attacker).setPetTarget(this);
            }
            int newHp = getCurrentHp() - damage;
            if (newHp <= 0) {
                Death(attacker);
            } else {
                setCurrentHp(newHp);
            }
        } else if (!isDead()) {
            _log.error("NPC hp減少處理失敗 可能原因: 初始hp為0(" + getNpcId() + ")");
            Death(attacker);
        }
    }

    public synchronized void Death(L1Character lastAttacker) throws Exception {
        if (!isDead()) {
            setDead(true);
            setCurrentHp(0);
            setStatus(8);
            getMap().setPassable(getLocation(), true);
            L1Inventory targetInventory = null;
            if (!(this._master == null || this._master.getInventory() == null)) {
                targetInventory = this._master.getInventory();
            }
            for (L1ItemInstance item : this._inventory.getItems()) {
                if (targetInventory == null) {
                    item.set_showId(get_showId());
                    targetInventory = World.get().getInventory(getX(), getY(), getMapId());
                    this._inventory.tradeItem(item, item.getCount(), targetInventory);
                } else if (this._master.getInventory().checkAddItem(item, item.getCount()) == 0) {
                    this._inventory.tradeItem(item, item.getCount(), targetInventory);
                    ((L1PcInstance) this._master).sendPackets(new S_ServerMessage(143, getName(), item.getLogName()));
                } else {
                    item.set_showId(get_showId());
                    targetInventory = World.get().getInventory(getX(), getY(), getMapId());
                    this._inventory.tradeItem(item, item.getCount(), targetInventory);
                }
            }
            if (this._tamed) {
                broadcastPacketAll(new S_DoActionGFX(getId(), 8));
                startDeleteTimer(ConfigAlt.NPC_DELETION_TIME * 2);
            } else {
                deleteMe();
            }
        }
    }

    public synchronized void returnToNature() throws Exception {
        this._isReturnToNature = true;
        if (!this._tamed) {
            getMap().setPassable(getLocation(), true);
            L1Inventory targetInventory = null;
            if (!(this._master == null || this._master.getInventory() == null)) {
                targetInventory = this._master.getInventory();
            }
            for (L1ItemInstance item : this._inventory.getItems()) {
                if (targetInventory == null) {
                    item.set_showId(get_showId());
                    targetInventory = World.get().getInventory(getX(), getY(), getMapId());
                    this._inventory.tradeItem(item, item.getCount(), targetInventory);
                } else if (this._master.getInventory().checkAddItem(item, item.getCount()) == 0) {
                    this._inventory.tradeItem(item, item.getCount(), targetInventory);
                    ((L1PcInstance) this._master).sendPackets(new S_ServerMessage(143, getName(), item.getLogName()));
                } else {
                    item.set_showId(get_showId());
                    targetInventory = World.get().getInventory(getX(), getY(), getMapId());
                    this._inventory.tradeItem(item, item.getCount(), targetInventory);
                }
            }
            deleteMe();
        } else {
            liberate();
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public synchronized void deleteMe() {
        if (this._master != null) {
            this._master.removePet(this);
        }
        if (!this._destroyed) {
            if (!this._tamed && !this._isReturnToNature) {
                broadcastPacketX8(new S_SkillSound(getId(), L1SkillId.EXOTIC_VITALIZE));
            }
            super.deleteMe();
        }
    }

    public void liberate() {
        L1MonsterInstance monster = new L1MonsterInstance(getNpcTemplate());
        monster.setId(IdFactoryNpc.get().nextId());
        monster.setX(getX());
        monster.setY(getY());
        monster.setMap(getMapId());
        monster.setHeading(getHeading());
        monster.set_storeDroped(true);
        monster.setInventory(getInventory());
        setInventory(null);
        monster.setCurrentHpDirect(getCurrentHp());
        monster.setCurrentMpDirect(getCurrentMp());
        monster.setExp(0);
        deleteMe();
        World.get().storeObject(monster);
        World.get().addVisibleObject(monster);
    }

    public void setTarget(L1Character target) {
        if (target == null) {
            return;
        }
        if (this._currentPetStatus == 1 || this._currentPetStatus == 2 || this._currentPetStatus == 5) {
            setHate(target, 0);
            if (!isAiRunning()) {
                startAI();
            }
        }
    }

    public void setMasterTarget(L1Character target) {
        if (target == null) {
            return;
        }
        if (this._currentPetStatus == 1 || this._currentPetStatus == 5) {
            setHate(target, 10);
            if (!isAiRunning()) {
                startAI();
            }
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance player) throws Exception {
        L1Character cha;
        if (player != null && (cha = getMaster()) != null) {
            L1PcInstance master = (L1PcInstance) cha;
            if (master.isTeleport()) {
                return;
            }
            if (master.equals(player)) {
                new L1AttackPc(player, this).action();
            } else if ((isSafetyZone() || player.isSafetyZone()) && isExsistMaster()) {
                new L1AttackPc(player, this).action();
            } else if (!player.checkNonPvP(player, this)) {
                L1AttackMode attack = new L1AttackPc(player, this);
                if (attack.calcHit()) {
                    attack.calcDamage();
                }
                attack.action();
                attack.commit();
            }
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onTalkAction(L1PcInstance player) {
        if (!isDead() && this._master.equals(player)) {
            player.sendPackets(new S_PetMenuPacket(this, 0));
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onFinalAction(L1PcInstance player, String action) throws Exception {
        int status = Integer.parseInt(action);
        switch (status) {
            case 0:
                return;
            case 6:
                if (this._tamed) {
                    deleteMe();
                    return;
                } else {
                    Death(null);
                    return;
                }
            default:
                Object[] petList = this._master.getPetList().values().toArray();
                for (Object petObject : petList) {
                    if (petObject instanceof L1SummonInstance) {
                        ((L1SummonInstance) petObject).set_currentPetStatus(status);
                    }
                }
                return;
        }
    }

    @Override // com.lineage.server.model.L1Object, com.lineage.server.model.Instance.L1NpcInstance
    public void onPerceive(L1PcInstance perceivedFrom) {
        try {
            if (perceivedFrom.get_showId() == get_showId()) {
                perceivedFrom.addKnownObject(this);
                perceivedFrom.sendPackets(new S_NPCPack_Summon(this, perceivedFrom));
                if (getMaster() != null && perceivedFrom.getId() == getMaster().getId()) {
                    perceivedFrom.sendPackets(new S_HPMeter(getId(), (getCurrentHp() * 100) / getMaxHp()));
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onItemUse() throws Exception {
        if (!isActived()) {
            useItem(1, 100);
        }
        if ((getCurrentHp() * 100) / getMaxHp() < 40) {
            useItem(0, 100);
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onGetItem(L1ItemInstance item) throws Exception {
        if (getNpcTemplate().get_digestitem() > 0) {
            setDigestItem(item);
        }
        Arrays.sort(healPotions);
        Arrays.sort(haestPotions);
        if (Arrays.binarySearch(healPotions, item.getItem().getItemId()) >= 0) {
            if (getCurrentHp() != getMaxHp()) {
                useItem(0, 100);
            }
        } else if (Arrays.binarySearch(haestPotions, item.getItem().getItemId()) >= 0) {
            useItem(1, 100);
        }
    }

    @Override // com.lineage.server.model.L1Character
    public void setCurrentHp(int i) {
        int currentHp = Math.min(i, getMaxHp());
        if (getCurrentHp() != currentHp) {
            setCurrentHpDirect(currentHp);
            if (this._master instanceof L1PcInstance) {
                ((L1PcInstance) this._master).sendPackets(new S_HPMeter(getId(), (currentHp * 100) / getMaxHp()));
            }
        }
    }

    @Override // com.lineage.server.model.L1Character
    public void setCurrentMp(int i) {
        int currentMp = Math.min(i, (int) getMaxMp());
        if (getCurrentMp() != currentMp) {
            setCurrentMpDirect(currentMp);
        }
    }

    public void set_currentPetStatus(int i) {
        this._currentPetStatus = i;
        set_tempModel();
        switch (this._currentPetStatus) {
            case 3:
                allTargetClear();
                return;
            case 4:
            default:
                if (!isAiRunning()) {
                    startAI();
                    return;
                }
                return;
            case 5:
                setHomeX(getX());
                setHomeY(getY());
                return;
        }
    }

    public int get_currentPetStatus() {
        return this._currentPetStatus;
    }

    public boolean isExsistMaster() {
        if (getMaster() == null) {
            return true;
        }
        if (World.get().getPlayer(getMaster().getName()) == null) {
            return false;
        }
        return true;
    }

    public int get_time() {
        return this._time;
    }

    public void set_time(int time) {
        this._time = time;
    }

    public void set_tempModel() {
        this._tempModel = this._currentPetStatus;
    }

    public void get_tempModel() {
        this._currentPetStatus = this._tempModel;
    }

    private static void teleport(L1NpcInstance npc, int x, int y, short map, int head) {
        try {
            World.get().moveVisibleObject(npc, map);
            L1WorldMap.get().getMap(npc.getMapId()).setPassable(npc.getX(), npc.getY(), true, 2);
            npc.setX(x);
            npc.setY(y);
            npc.setMap(map);
            npc.setHeading(head);
            L1WorldMap.get().getMap(npc.getMapId()).setPassable(npc.getX(), npc.getY(), false, 2);
            npc.onNpcAI();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
