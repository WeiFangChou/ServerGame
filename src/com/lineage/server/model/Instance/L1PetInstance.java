package com.lineage.server.model.Instance;

import com.lineage.config.ConfigOther;
import com.lineage.server.IdFactory;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.ExpTable;
import com.lineage.server.datatables.PetItemTable;
import com.lineage.server.datatables.PetTypeTable;
import com.lineage.server.datatables.lock.PetReading;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1GroundInventory;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_HPMeter;
import com.lineage.server.serverpackets.S_NPCPack_Pet;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.serverpackets.S_PetMenuPacket;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1Pet;
import com.lineage.server.templates.L1PetItem;
import com.lineage.server.templates.L1PetType;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1PetInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1PetInstance.class);
    private static Random _random = new Random();
    private static final long serialVersionUID = 1;
    private L1ItemInstance _armor;
    private int _checkMove = 0;
    private int _currentPetStatus;
    private int _damageByWeapon;
    private int _expPercent;
    private int _hitByWeapon;
    private int _itemObjId;
    private L1PcInstance _petMaster;
    private int _tempModel = 3;
    private L1PetType _type;
    private L1ItemInstance _weapon;

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public boolean noTarget() throws Exception {
        if (ConfigOther.WAR_Hier) {
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
                if (this._petMaster != null && this._petMaster.getMapId() == getMapId() && getLocation().getTileLineDistance(this._petMaster.getLocation()) < 5) {
                    if (this._npcMove != null) {
                        this._npcMove.setDirectionMove(this._npcMove.checkObject(this._npcMove.targetReverseDirection(this._petMaster.getX(), this._petMaster.getY())));
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
            case 6:
            default:
                if (this._petMaster != null && this._petMaster.getMapId() == getMapId()) {
                    if (getLocation().getTileLineDistance(this._petMaster.getLocation()) > 2 && this._npcMove != null) {
                        int dir2 = this._npcMove.moveDirection(this._petMaster.getX(), this._petMaster.getY());
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
                            if (this._currentPetStatus == 8) {
                                collect(false);
                                this._currentPetStatus = 2;
                            }
                        }
                    }
                    if (getLocation().getTileLineDistance(this._petMaster.getLocation()) > 7) {
                        teleport(this, getMaster().getX(), getMaster().getY(), getMaster().getMapId(), 0);
                        this._petMaster.sendPackets(new S_ServerMessage("主人等等我，汪！汪！汪！為了跟上主人，我只能飛行到主人身邊！"));
                        break;
                    }
                } else {
                    this._currentPetStatus = 3;
                    return true;
                }
                break;
            case 7:
                if (this._petMaster != null && this._petMaster.getMapId() == getMapId() && getLocation().getTileLineDistance(this._petMaster.getLocation()) <= 1) {
                    this._currentPetStatus = 3;
                    return true;
                } else if (this._npcMove != null) {
                    int dirx = this._npcMove.moveDirection(this._petMaster.getX() + _random.nextInt(1), this._petMaster.getY() + _random.nextInt(1));
                    if (dirx != -1) {
                        this._npcMove.setDirectionMove(dirx);
                        setSleepTime(calcSleepTime(getPassispeed(), 0));
                        break;
                    } else {
                        this._currentPetStatus = 3;
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    public L1PetInstance(L1Npc template, L1PcInstance master) {
        super(template);
        this._petMaster = master;
        this._itemObjId = -1;
        this._type = null;
        setId(IdFactoryNpc.get().nextId());
        set_showId(master.get_showId());
        setName(template.get_name());
        setLevel(template.get_level());
        setMaxHp(template.get_hp());
        setCurrentHpDirect(template.get_hp());
        setMaxMp(template.get_mp());
        setCurrentMpDirect(template.get_mp());
        setExp((long) template.get_exp());
        setExpPercent(ExpTable.getExpPercentage(template.get_level(), (long) template.get_exp()));
        setLawful(template.get_lawful());
        setTempLawful(template.get_lawful());
        setMaster(master);
        setX((master.getX() + _random.nextInt(5)) - 2);
        setY((master.getY() + _random.nextInt(5)) - 2);
        setMap(master.getMapId());
        setHeading(master.getHeading());
        setLightSize(template.getLightSize());
        this._currentPetStatus = 3;
        World.get().storeObject(this);
        World.get().addVisibleObject(this);
        Iterator<L1PcInstance> it = World.get().getRecognizePlayer(this).iterator();
        while (it.hasNext()) {
            onPerceive(it.next());
        }
        master.addPet(this);
        addMaster(master);
    }

    public L1PetInstance(L1Npc template, L1PcInstance master, L1Pet pet) {
        super(template);
        this._petMaster = master;
        this._itemObjId = pet.get_itemobjid();
        this._type = PetTypeTable.getInstance().get(template.get_npcId());
        setId(pet.get_objid());
        set_showId(master.get_showId());
        setName(pet.get_name());
        setLevel(pet.get_level());
        setMaxHp(pet.get_hp());
        setCurrentHpDirect(pet.get_hp());
        setMaxMp(pet.get_mp());
        setCurrentMpDirect(pet.get_mp());
        setExp((long) pet.get_exp());
        setExpPercent(ExpTable.getExpPercentage(pet.get_level(), (long) pet.get_exp()));
        setLawful(pet.get_lawful());
        setTempLawful(pet.get_lawful());
        setMaster(master);
        setX((master.getX() + _random.nextInt(5)) - 2);
        setY((master.getY() + _random.nextInt(5)) - 2);
        setMap(master.getMapId());
        setHeading(5);
        setLightSize(template.getLightSize());
        this._currentPetStatus = 3;
        World.get().storeObject(this);
        World.get().addVisibleObject(this);
        Iterator<L1PcInstance> it = World.get().getRecognizePlayer(this).iterator();
        while (it.hasNext()) {
            onPerceive(it.next());
        }
        broadcastPacketX8(new S_SkillSound(getId(), 3104));
        broadcastPacketX8(new S_SkillHaste(getId(), 1, 0));
        setMoveSpeed(1);
        setSkillEffect(43, 1800000);
        master.addPet(this);
        addMaster(master);
    }

    public L1PetInstance(L1NpcInstance target, L1PcInstance master, int itemobjid) {
        super(null);
        this._petMaster = master;
        this._itemObjId = itemobjid;
        this._type = PetTypeTable.getInstance().get(target.getNpcTemplate().get_npcId());
        setId(IdFactory.get().nextId());
        set_showId(master.get_showId());
        setting_template(target.getNpcTemplate());
        setCurrentHpDirect(target.getCurrentHp());
        setCurrentMpDirect(target.getCurrentMp());
        setExp(750);
        setExpPercent(0);
        setLawful(0);
        setTempLawful(0);
        setMaster(master);
        setX(target.getX());
        setY(target.getY());
        setMap(target.getMapId());
        setHeading(target.getHeading());
        setLightSize(target.getLightSize());
        setPetcost(6);
        setInventory(target.getInventory());
        target.setInventory(null);
        this._currentPetStatus = 3;
        stopHpRegeneration();
        if (getMaxHp() > getCurrentHp()) {
            startHpRegeneration();
        }
        stopMpRegeneration();
        if (getMaxMp() > getCurrentMp()) {
            startMpRegeneration();
        }
        target.deleteMe();
        World.get().storeObject(this);
        World.get().addVisibleObject(this);
        Iterator<L1PcInstance> it = World.get().getRecognizePlayer(this).iterator();
        while (it.hasNext()) {
            onPerceive(it.next());
        }
        master.addPet(this);
        PetReading.get().storeNewPet(target, getId(), itemobjid);
        addMaster(master);
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void receiveDamage(L1Character attacker, int damage) {
        this.ISASCAPE = false;
        if (getCurrentHp() > 0) {
            if (damage > 0) {
                setHate(attacker, 0);
                removeSkillEffect(66);
            }
            if ((attacker instanceof L1PcInstance) && damage > 0) {
                ((L1PcInstance) attacker).setPetTarget(this);
            }
            if (attacker instanceof L1PetInstance) {
                L1PetInstance pet = (L1PetInstance) attacker;
                if (getZoneType() == 1 || pet.getZoneType() == 1) {
                    damage = 0;
                }
            } else if (attacker instanceof L1SummonInstance) {
                L1SummonInstance summon = (L1SummonInstance) attacker;
                if (getZoneType() == 1 || summon.getZoneType() == 1) {
                    damage = 0;
                }
            }
            int newHp = getCurrentHp() - damage;
            if (newHp <= 0) {
                death(attacker);
            } else {
                setCurrentHp(newHp);
            }
        } else if (!isDead()) {
            death(attacker);
        }
    }

    public synchronized void death(L1Character lastAttacker) {
        if (!isDead()) {
            setDead(true);
            setStatus(8);
            setCurrentHp(0);
            this._currentPetStatus = 3;
            getMap().setPassable(getLocation(), true);
            broadcastPacketAll(new S_DoActionGFX(getId(), 8));
        }
    }

    public void evolvePet(int new_itemobjid) throws Exception {
        L1Pet pet = PetReading.get().getTemplate(this._itemObjId);
        if (pet != null) {
            int newNpcId = this._type.getNpcIdForEvolving();
            int evolvItem = this._type.getEvolvItemId();
            int tmpMaxHp = getMaxHp();
            int tmpMaxMp = getMaxMp();
            transform(newNpcId);
            this._type = PetTypeTable.getInstance().get(newNpcId);
            setLevel(1);
            setMaxHp(tmpMaxHp / 2);
            setMaxMp(tmpMaxMp / 2);
            setCurrentHpDirect(getMaxHp());
            setCurrentMpDirect(getMaxMp());
            setExp(0);
            setExpPercent(0);
            getInventory().consumeItem(evolvItem, serialVersionUID);
            L1Object obj = World.get().findObject(pet.get_objid());
            if (obj != null && (obj instanceof L1NpcInstance)) {
                L1PetInstance new_pet = (L1PetInstance) obj;
                L1Inventory new_petInventory = new_pet.getInventory();
                for (L1ItemInstance item : getInventory().getItems()) {
                    if (item != null) {
                        if (item.isEquipped()) {
                            item.setEquipped(false);
                            if (PetItemTable.get().getTemplate(item.getItemId()).isWeapom()) {
                                setWeapon(null);
                                new_pet.usePetWeapon(this, item);
                            } else {
                                setArmor(null);
                                new_pet.usePetArmor(this, item);
                            }
                        }
                        if (new_pet.getInventory().checkAddItem(item, item.getCount()) == 0) {
                            getInventory().tradeItem(item, item.getCount(), new_petInventory);
                        } else {
                            getInventory().tradeItem(item, item.getCount(), World.get().getInventory(getX(), getY(), getMapId()));
                        }
                    }
                }
                new_pet.broadcastPacketAll(new S_SkillSound(new_pet.getId(), 2127));
            }
            PetReading.get().deletePet(this._itemObjId);
            pet.set_itemobjid(new_itemobjid);
            pet.set_npcid(newNpcId);
            pet.set_name(getName());
            pet.set_level(getLevel());
            pet.set_hp(getMaxHp());
            pet.set_mp(getMaxMp());
            pet.set_exp((int) getExp());
            PetReading.get().storeNewPet(this, getId(), new_itemobjid);
            this._itemObjId = new_itemobjid;
        }
    }

    public void liberate() throws Exception {
        L1MonsterInstance monster = new L1MonsterInstance(getNpcTemplate());
        monster.setId(IdFactoryNpc.get().nextId());
        monster.setX(getX());
        monster.setY(getY());
        monster.setMap(getMapId());
        monster.setHeading(getHeading());
        monster.set_storeDroped(true);
        monster.setInventory(getInventory());
        setInventory(null);
        monster.setLevel(getLevel());
        monster.setMaxHp(getMaxHp());
        monster.setCurrentHpDirect(getCurrentHp());
        monster.setMaxMp(getMaxMp());
        monster.setCurrentMpDirect(getCurrentMp());
        this._petMaster.getPetList().remove(Integer.valueOf(getId()));
        deleteMe();
        this._petMaster.getInventory().removeItem(this._itemObjId, serialVersionUID);
        PetReading.get().deletePet(this._itemObjId);
        World.get().storeObject(monster);
        World.get().addVisibleObject(monster);
        Iterator<L1PcInstance> it = World.get().getRecognizePlayer(monster).iterator();
        while (it.hasNext()) {
            onPerceive(it.next());
        }
    }

    public void collect(boolean isDepositnpc) throws Exception {
        L1Inventory masterInv = this._petMaster.getInventory();
        for (L1ItemInstance item : this._inventory.getItems()) {
            if (item.isEquipped()) {
                if (isDepositnpc) {
                    L1PetItem petItem = PetItemTable.get().getTemplate(item.getItem().getItemId());
                    if (petItem != null) {
                        setHitByWeapon(0);
                        setDamageByWeapon(0);
                        addStr(-petItem.getAddStr());
                        addCon(-petItem.getAddCon());
                        addDex(-petItem.getAddDex());
                        addInt(-petItem.getAddInt());
                        addWis(-petItem.getAddWis());
                        addMaxHp(-petItem.getAddHp());
                        addMaxMp(-petItem.getAddMp());
                        addSp(-petItem.getAddSp());
                        addMr(-petItem.getAddMr());
                        setWeapon(null);
                        setArmor(null);
                        item.setEquipped(false);
                    }
                }
            }
            if (this._petMaster.getInventory().checkAddItem(item, item.getCount()) == 0) {
                this._inventory.tradeItem(item, item.getCount(), masterInv);
                this._petMaster.sendPackets(new S_ServerMessage(143, getName(), item.getLogName()));
            } else {
                item.set_showId(get_showId());
                masterInv = World.get().getInventory(getX(), getY(), getMapId());
                this._inventory.tradeItem(item, item.getCount(), masterInv);
            }
        }
        savePet();
    }

    public void collection() {
        ArrayList<L1GroundInventory> gInventorys = new ArrayList<>();
        Iterator<L1Object> it = World.get().getVisibleObjects(this, 10).iterator();
        while (it.hasNext()) {
            L1Object obj = it.next();
            if (obj != null && (obj instanceof L1GroundInventory)) {
                gInventorys.add((L1GroundInventory) obj);
            }
        }
        int groundinv = gInventorys.size();
        for (int i = 0; i < groundinv; i++) {
            for (L1ItemInstance item : gInventorys.get(i).getItems()) {
                if (getInventory().checkAddItem(item, item.getCount()) == 0 && !item.getItem().isUseHighPet()) {
                    this._targetItem = item;
                    this._targetItemList.add(this._targetItem);
                }
            }
        }
    }

    public void dropItem(L1PetInstance pet) throws Exception {
        L1PetItem petItem;
        L1Inventory worldInv = World.get().getInventory(getX(), getY(), getMapId());
        for (L1ItemInstance item : this._inventory.getItems()) {
            item.set_showId(get_showId());
            if (item.isEquipped() && (petItem = PetItemTable.get().getTemplate(item.getItem().getItemId())) != null) {
                setHitByWeapon(0);
                setDamageByWeapon(0);
                addStr(-petItem.getAddStr());
                addCon(-petItem.getAddCon());
                addDex(-petItem.getAddDex());
                addInt(-petItem.getAddInt());
                addWis(-petItem.getAddWis());
                addMaxHp(-petItem.getAddHp());
                addMaxMp(-petItem.getAddMp());
                addSp(-petItem.getAddSp());
                addMr(-petItem.getAddMr());
                setWeapon(null);
                setArmor(null);
                item.setEquipped(false);
            }
            this._inventory.tradeItem(item, item.getCount(), worldInv);
        }
        savePet();
    }

    private void savePet() {
        try {
            L1Pet pet = PetReading.get().getTemplate(this._itemObjId);
            if (pet != null) {
                pet.set_exp((int) getExp());
                pet.set_level(getLevel());
                pet.set_hp(getMaxHp());
                pet.set_mp(getMaxMp());
                PetReading.get().storePet(pet);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void call() {
        int id;
        if (!(this._type == null || (id = this._type.getMessageId(L1PetType.getMessageNumber(getLevel()))) == 0)) {
            broadcastPacketAll(new S_NpcChat(this, "$" + id));
        }
        setCurrentPetStatus(7);
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onNpcAI() {
        if (!isAiRunning()) {
            setActived(false);
            startAI();
        }
    }

    public void setTarget(L1Character target) {
        if (target == null) {
            return;
        }
        if (this._currentPetStatus == 1 || this._currentPetStatus == 2 || this._currentPetStatus == 5) {
            setHate(target, 0);
            onNpcAI();
        }
    }

    public void setMasterTarget(L1Character target) {
        if (target == null) {
            return;
        }
        if (this._currentPetStatus == 1 || this._currentPetStatus == 5) {
            setHate(target, 0);
            onNpcAI();
        }
    }

    public void setMasterSelectTarget(L1Character target) {
        if (target != null) {
            setHate(target, 0);
            onNpcAI();
        }
    }

    @Override // com.lineage.server.model.L1Object, com.lineage.server.model.Instance.L1NpcInstance
    public void onPerceive(L1PcInstance perceivedFrom) {
        try {
            if (perceivedFrom.get_showId() == get_showId()) {
                perceivedFrom.addKnownObject(this);
                perceivedFrom.sendPackets(new S_NPCPack_Pet(this, perceivedFrom));
                if (getMaster() != null && perceivedFrom.getId() == getMaster().getId()) {
                    perceivedFrom.sendPackets(new S_HPMeter(getId(), (getCurrentHp() * 100) / getMaxHp()));
                }
                onNpcAI();
                if (isDead()) {
                    perceivedFrom.sendPackets(new S_DoActionGFX(getId(), 8));
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
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
            } else if (isSafetyZone() || player.isSafetyZone()) {
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
        if (!isDead() && this._petMaster.equals(player)) {
            player.sendPackets(new S_PetMenuPacket(this, getExpPercent()));
            L1Pet l1pet = PetReading.get().getTemplate(this._itemObjId);
            if (l1pet != null) {
                l1pet.set_exp((int) getExp());
                l1pet.set_level(getLevel());
                l1pet.set_hp(getMaxHp());
                l1pet.set_mp(getMaxMp());
                PetReading.get().storePet(l1pet);
            }
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onFinalAction(L1PcInstance player, String action) throws Exception {
        int id;
        int status = Integer.parseInt(action);
        switch (status) {
            case 0:
                return;
            case 6:
                liberate();
                return;
            default:
                Object[] petList = this._petMaster.getPetList().values().toArray();
                for (Object petObject : petList) {
                    if (petObject instanceof L1PetInstance) {
                        L1PetInstance pet = (L1PetInstance) petObject;
                        if (this._petMaster != null) {
                            if (this._petMaster.isGm()) {
                                pet.setCurrentPetStatus(status);
                            } else if (this._petMaster.getLevel() >= pet.getLevel()) {
                                pet.setCurrentPetStatus(status);
                            } else if (!(this._type == null || (id = this._type.getDefyMessageId()) == 0)) {
                                broadcastPacketX8(new S_NpcChat(pet, "$" + id));
                            }
                        }
                    }
                }
                return;
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onItemUse() throws Exception {
        if (!hasSkillEffect(L1SkillId.STATUS_HASTE) && !hasSkillEffect(43)) {
            useItem(1, 100);
        }
        if ((getCurrentHp() * 100) / getMaxHp() < 90) {
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
            if (this._petMaster != null) {
                this._petMaster.sendPackets(new S_HPMeter(getId(), (currentHp * 100) / getMaxHp()));
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

    public void setCurrentPetStatus(int i) {
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

    public int getCurrentPetStatus() {
        return this._currentPetStatus;
    }

    public int getItemObjId() {
        return this._itemObjId;
    }

    public void setExpPercent(int expPercent) {
        this._expPercent = expPercent;
    }

    public int getExpPercent() {
        return this._expPercent;
    }

    public void setWeapon(L1ItemInstance weapon) {
        this._weapon = weapon;
    }

    public L1ItemInstance getWeapon() {
        return this._weapon;
    }

    public void setArmor(L1ItemInstance armor) {
        this._armor = armor;
    }

    public L1ItemInstance getArmor() {
        return this._armor;
    }

    public void setHitByWeapon(int i) {
        this._hitByWeapon = i;
    }

    public int getHitByWeapon() {
        return this._hitByWeapon;
    }

    public void setDamageByWeapon(int i) {
        this._damageByWeapon = i;
    }

    public int getDamageByWeapon() {
        return this._damageByWeapon;
    }

    public L1PetType getPetType() {
        return this._type;
    }

    public void set_tempModel() {
        this._tempModel = this._currentPetStatus;
    }

    public void get_tempModel() {
        this._currentPetStatus = this._tempModel;
    }

    public void usePetWeapon(L1PetInstance pet, L1ItemInstance weapon) {
        if (pet.getWeapon() == null) {
            setPetWeapon(pet, weapon);
        } else if (pet.getWeapon().equals(weapon)) {
            removePetWeapon(pet, pet.getWeapon());
        } else {
            removePetWeapon(pet, pet.getWeapon());
            setPetWeapon(pet, weapon);
        }
    }

    public void usePetArmor(L1PetInstance pet, L1ItemInstance armor) {
        if (pet.getArmor() == null) {
            setPetArmor(pet, armor);
        } else if (pet.getArmor().equals(armor)) {
            removePetArmor(pet, pet.getArmor());
        } else {
            removePetArmor(pet, pet.getArmor());
            setPetArmor(pet, armor);
        }
    }

    private void setPetWeapon(L1PetInstance pet, L1ItemInstance weapon) {
        L1PetItem petItem = PetItemTable.get().getTemplate(weapon.getItem().getItemId());
        if (petItem != null) {
            pet.setHitByWeapon(petItem.getHitModifier());
            pet.setDamageByWeapon(petItem.getDamageModifier());
            pet.addStr(petItem.getAddStr());
            pet.addCon(petItem.getAddCon());
            pet.addDex(petItem.getAddDex());
            pet.addInt(petItem.getAddInt());
            pet.addWis(petItem.getAddWis());
            pet.addMaxHp(petItem.getAddHp());
            pet.addMaxMp(petItem.getAddMp());
            pet.addSp(petItem.getAddSp());
            pet.addMr(petItem.getAddMr());
            pet.setWeapon(weapon);
            weapon.setEquipped(true);
        }
    }

    private void removePetWeapon(L1PetInstance pet, L1ItemInstance weapon) {
        L1PetItem petItem = PetItemTable.get().getTemplate(weapon.getItem().getItemId());
        if (petItem != null) {
            pet.setHitByWeapon(0);
            pet.setDamageByWeapon(0);
            pet.addStr(-petItem.getAddStr());
            pet.addCon(-petItem.getAddCon());
            pet.addDex(-petItem.getAddDex());
            pet.addInt(-petItem.getAddInt());
            pet.addWis(-petItem.getAddWis());
            pet.addMaxHp(-petItem.getAddHp());
            pet.addMaxMp(-petItem.getAddMp());
            pet.addSp(-petItem.getAddSp());
            pet.addMr(-petItem.getAddMr());
            pet.setWeapon(null);
            weapon.setEquipped(false);
        }
    }

    private void setPetArmor(L1PetInstance pet, L1ItemInstance armor) {
        L1PetItem petItem = PetItemTable.get().getTemplate(armor.getItem().getItemId());
        if (petItem != null) {
            pet.addAc(petItem.getAddAc());
            pet.addStr(petItem.getAddStr());
            pet.addCon(petItem.getAddCon());
            pet.addDex(petItem.getAddDex());
            pet.addInt(petItem.getAddInt());
            pet.addWis(petItem.getAddWis());
            pet.addMaxHp(petItem.getAddHp());
            pet.addMaxMp(petItem.getAddMp());
            pet.addSp(petItem.getAddSp());
            pet.addMr(petItem.getAddMr());
            pet.setArmor(armor);
            armor.setEquipped(true);
        }
    }

    private void removePetArmor(L1PetInstance pet, L1ItemInstance armor) {
        L1PetItem petItem = PetItemTable.get().getTemplate(armor.getItem().getItemId());
        if (petItem != null) {
            pet.addAc(-petItem.getAddAc());
            pet.addStr(-petItem.getAddStr());
            pet.addCon(-petItem.getAddCon());
            pet.addDex(-petItem.getAddDex());
            pet.addInt(-petItem.getAddInt());
            pet.addWis(-petItem.getAddWis());
            pet.addMaxHp(-petItem.getAddHp());
            pet.addMaxMp(-petItem.getAddMp());
            pet.addSp(-petItem.getAddSp());
            pet.addMr(-petItem.getAddMr());
            pet.setArmor(null);
            armor.setEquipped(false);
        }
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
