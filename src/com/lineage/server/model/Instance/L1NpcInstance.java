package com.lineage.server.model.Instance;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.echo.OpcodesClient;
import com.lineage.server.datatables.NpcChatTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.lock.SpawnBossReading;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackNpc;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1GroundInventory;
import com.lineage.server.model.L1HateList;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.L1MobGroupInfo;
import com.lineage.server.model.L1MobSkillUse;
import com.lineage.server.model.L1NpcChatTimer;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Spawn;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_ChangeShape;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.serverpackets.S_NewMaster;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_Resurrection;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1NpcChat;
import com.lineage.server.timecontroller.npc.NpcWorkTimer;
import com.lineage.server.types.Point;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldNpc;
import com.lineage.server.world.WorldQuest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1NpcInstance extends L1Character {
    public static final int ATTACK_SPEED = 1;
    public static final int CHAT_TIMING_APPEARANCE = 0;
    public static final int CHAT_TIMING_DEAD = 1;
    public static final int CHAT_TIMING_GAME_TIME = 3;
    public static final int CHAT_TIMING_HIDE = 2;
    public static final int DISTANCE = 20;
    public static final int HIDDEN_STATUS_FLY = 2;
    public static final int HIDDEN_STATUS_ICE = 3;
    public static final int HIDDEN_STATUS_NONE = 0;
    public static final int HIDDEN_STATUS_SINK = 1;
    public static final int MAGIC_SPEED = 2;
    public static final int MOVE_SPEED = 0;
    public static final int REST_MILLISEC = 10;
    public static final int USEITEM_HASTE = 1;
    public static final int USEITEM_HEAL = 0;
    private static final Log _log = LogFactory.getLog(L1NpcInstance.class);
    private static final Random _random = new Random();
    public static int[] haestPotions = {L1ItemId.B_POTION_OF_GREATER_HASTE_SELF, L1ItemId.POTION_OF_GREATER_HASTE_SELF, L1ItemId.B_POTION_OF_HASTE_SELF, L1ItemId.POTION_OF_HASTE_SELF};
    public static int[] healPotions = {L1ItemId.POTION_OF_GREATER_HEALING, L1ItemId.POTION_OF_EXTRA_HEALING, L1ItemId.POTION_OF_HEALING};
    private static final long serialVersionUID = 1;
    public NpcExecutor ACTION = null;
    public NpcExecutor ATTACK = null;
    public NpcExecutor DEATH = null;
    protected boolean ISASCAPE = false;
    public NpcExecutor SPAWN = null;
    public NpcExecutor TALK = null;
    public NpcExecutor WORK = null;
    private boolean _Agro;
    private boolean _Agrocoi;
    private boolean _Agrososc;
    private boolean _actived = false;
    private boolean _aiRunning = false;
    private int _atkspeed;
    private int _bowActId = -1;
    private int _deadTimerTemp = -1;
    private boolean _deathProcessing = false;
    private final HashMap<L1ItemInstance, DelItemTime> _del_map = new HashMap<>();
    public boolean _destroyed = false;
    private int _drainedMana = 0;
    protected final L1HateList _dropHateList = new L1HateList();
    private boolean _firstAttack = false;
    private boolean _forDropitems = false;
    protected final L1HateList _hateList = new L1HateList();
    private int _hiddenStatus;
    private int _homeX;
    private int _homeY;
    protected boolean _hprRunning = false;
    public L1Inventory _inventory = new L1Inventory();
    private boolean _isDropitems = false;
    private boolean _isResurrect = false;
    private boolean _isremovearmor = false;
    private boolean _isspawnTime = false;
    private int _lightSize;
    protected L1Character _master = null;
    private int _mobGroupId = 0;
    private L1MobGroupInfo _mobGroupInfo = null;
    public L1MobSkillUse _mobSkill;
    private int _movementDistance = 0;
    private boolean _mprRunning = false;
    private String _nameId;
    protected NpcMoveExecutor _npcMove = null;
    private L1Npc _npcTemplate;
    private int _passispeed;
    private int _petcost;
    private boolean _pickupItem;
    private int _randomMoveDirection = 0;
    private int _randomMoveDistance = 0;
    private int _ranged = -1;
    private boolean _reSpawn;
    private boolean _rest = false;
    private int _sleep_time;
    private L1Spawn _spawn;
    private int _spawnNumber;
    private int _spawnTime = 0;
    private int _stop_time = -1;
    protected L1Character _target = null;
    protected L1ItemInstance _targetItem = null;
    protected final List<L1ItemInstance> _targetItemList = new ArrayList();
    private int _tempLawful = 0;
    private boolean _weaponBreaked;
    private boolean firstFound = true;

    /* access modifiers changed from: protected */
    public void startAI() {
        if (isDead()) {
            setAiRunning(false);
        } else if (destroyed()) {
            setAiRunning(false);
        } else if (getCurrentHp() <= 0) {
            setAiRunning(false);
        } else if (!isAiRunning()) {
            setAiRunning(true);
            new NpcAI(this).startAI();
            startHpRegeneration();
            startMpRegeneration();
        }
    }

    public void onItemUse() throws Exception {
    }

    public void searchTarget() {
    }

    public void addMaster(L1PcInstance master) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            if (master != null) {
                if (master.get_other().get_color() != 0) {
                    stringBuilder.append(master.get_other().color());
                }
                stringBuilder.append(master.getName());
            } else {
                stringBuilder.append("");
            }
            master.sendPackets(new S_NewMaster(stringBuilder.toString(), this));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void checkTarget() {
        try {
            if (this._target == null) {
                clearTagert();
            } else if (this._target.getMapId() != getMapId()) {
                clearTagert();
            } else if (this._target.getCurrentHp() <= 0) {
                clearTagert();
            } else if (this._target.isDead()) {
                clearTagert();
            } else if (get_showId() != this._target.get_showId()) {
                clearTagert();
            } else if (this._target.isInvisble() && !getNpcTemplate().is_agrocoi() && !this._hateList.containsKey(this._target)) {
                clearTagert();
            } else if (getLocation().getTileDistance(this._target.getLocation()) > 30) {
                clearTagert();
            }
        } catch (Exception ignored) {
        }
    }

    private void clearTagert() {
        if (this._target != null) {
            tagertClear();
        }
        if (!this._hateList.isEmpty()) {
            this._target = this._hateList.getMaxHateCharacter();
            checkTarget();
        }
    }

    public boolean isHate(L1Character cha) {
        return this._hateList.isHate(cha);
    }

    public void setHate(L1Character cha, int hate) {
        if (cha != null) {
            try {
                if (cha.getId() != getId()) {
                    if (!isFirstAttack() && hate != 0) {
                        hate += getMaxHp() / 10;
                        setFirstAttack(true);
                    }
                    if (this._npcMove != null) {
                        this._npcMove.clear();
                    }
                    this._hateList.add(cha, hate);
                    this._dropHateList.add(cha, hate);
                    this._target = this._hateList.getMaxHateCharacter();
                    checkTarget();
                }
            } catch (Exception ignored) {
            }
        }
    }

    public void setLink(L1Character cha) {
    }

    public void serchLink(L1PcInstance targetPlayer, int family) {
        for (Object knownObject : targetPlayer.getKnownObjects()) {
            if (knownObject instanceof L1NpcInstance) {
                L1NpcInstance npc = (L1NpcInstance) knownObject;
                if (get_showId() == npc.get_showId()) {
                    if (npc.getNpcTemplate().get_agrofamily() > 0) {
                        if (npc.getNpcTemplate().get_agrofamily() != 1) {
                            npc.setLink(targetPlayer);
                        } else if (npc.getNpcTemplate().get_family() == family) {
                            npc.setLink(targetPlayer);
                        }
                    }
                    if (!(getMobGroupInfo() == null || getMobGroupId() == 0 || getMobGroupId() != npc.getMobGroupId())) {
                        npc.setLink(targetPlayer);
                    }
                }
            }
        }
    }

    public void onTarget() {
        try {
            setActived(true);
            if (this._targetItemList.size() > 0) {
                this._targetItemList.clear();
            }
            if (this._targetItem != null) {
                this._targetItem = null;
            }
            L1Character target = this._target;
            if (getAtkspeed() == 0 && getPassispeed() > 0) {
                this.ISASCAPE = true;
            }
            if (this.ISASCAPE) {
                ascape(target.getLocation());
            } else if (target != null) {
                attack(target);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void attack(L1Character target) throws Exception {
        if (isAttackPosition(target.getX(), target.getY(), get_ranged())) {
            if (!this._mobSkill.isSkillTrigger(target)) {
                setHeading(targetDirection(target.getX(), target.getY()));
                attackTarget(target);
            } else if (this._mobSkill.skillUse(target)) {
                setSleepTime(calcSleepTime(this._mobSkill.getSleepTime(), 2));
            } else {
                setHeading(targetDirection(target.getX(), target.getY()));
                attackTarget(target);
            }
            if (this._npcMove != null) {
                this._npcMove.clear();
            }
        } else if (glanceCheck(target.getX(), target.getY()) && this._mobSkill.skillUse(target)) {
            setSleepTime(calcSleepTime(this._mobSkill.getSleepTime(), 2));
        } else if (getPassispeed() > 0) {
            int distance = getLocation().getTileDistance(target.getLocation());
            if (this.firstFound && getNpcTemplate().is_teleport() && distance > 3 && distance < 20 && nearTeleport(target.getX(), target.getY())) {
                this.firstFound = false;
            } else if ((!getNpcTemplate().is_teleport() || 20 <= _random.nextInt(100) || getCurrentMp() < 10 || distance <= 6 || distance >= 20 || !nearTeleport(target.getX(), target.getY())) && this._npcMove != null) {
                int dir = this._npcMove.moveDirection(target.getX(), target.getY());
                if (dir == -1) {
                    tagertClear();
                    return;
                }
                this._npcMove.setDirectionMove(dir);
                setSleepTime(calcSleepTime(getPassispeed(), 0));
            }
        } else {
            switch (getGfxId()) {
                case 816:
                    attackTarget(target);
                    return;
                default:
                    tagertClear();
                    return;
            }
        }
    }

    private void ascape(L1Location location) {
        int escapeDistance = 17;
        if (hasSkillEffect(40)) {
            escapeDistance = 1;
        }
        if (getLocation().getTileLineDistance(location) > escapeDistance) {
            tagertClear();
        } else if (this._npcMove != null) {
            this._npcMove.setDirectionMove(this._npcMove.openDoor(this._npcMove.checkObject(this._npcMove.targetReverseDirection(location.getX(), location.getY()))));
            setSleepTime(calcSleepTime(getPassispeed(), 0));
        }
    }

    private boolean isTraget(L1Character cha) {
        if (cha instanceof L1PcInstance) {
            if (!((L1PcInstance) cha).isSafetyZone()) {
                return true;
            }
            return false;
        } else if (cha instanceof L1PetInstance) {
            if (!((L1PetInstance) cha).isSafetyZone()) {
                return true;
            }
            return false;
        } else if (cha instanceof L1SummonInstance) {
            if (!((L1SummonInstance) cha).isSafetyZone()) {
                return true;
            }
            return false;
        } else if (cha instanceof L1MonsterInstance) {
            return true;
        } else {
            return false;
        }
    }

    public void attackTarget(L1Character target) throws Exception {
        if (!(this instanceof L1IllusoryInstance) || isTraget(target)) {
            if (target instanceof L1PcInstance) {
                if (((L1PcInstance) target).isTeleport()) {
                    return;
                }
            } else if (target instanceof L1PetInstance) {
                L1Character cha = ((L1PetInstance) target).getMaster();
                if ((cha instanceof L1PcInstance) && ((L1PcInstance) cha).isTeleport()) {
                    return;
                }
            } else if (target instanceof L1SummonInstance) {
                L1Character cha2 = ((L1SummonInstance) target).getMaster();
                if ((cha2 instanceof L1PcInstance) && ((L1PcInstance) cha2).isTeleport()) {
                    return;
                }
            }
            if (this instanceof L1PetInstance) {
                L1Character cha3 = ((L1PetInstance) this).getMaster();
                if ((cha3 instanceof L1PcInstance) && ((L1PcInstance) cha3).isTeleport()) {
                    return;
                }
            } else if (this instanceof L1SummonInstance) {
                L1Character cha4 = ((L1SummonInstance) this).getMaster();
                if ((cha4 instanceof L1PcInstance) && ((L1PcInstance) cha4).isTeleport()) {
                    return;
                }
            }
            if (!(target instanceof L1NpcInstance) || ((L1NpcInstance) target).getHiddenStatus() == 0) {
                boolean isCounterBarrier = false;
                L1AttackMode attack = new L1AttackNpc(this, target);
                if (attack.calcHit()) {
                    if (target.hasSkillEffect(91)) {
                        boolean isProbability = new L1Magic(target, this).calcProbabilityMagic(91);
                        boolean isShortDistance = attack.isShortDistance();
                        if (isProbability && isShortDistance) {
                            isCounterBarrier = true;
                        }
                    }
                    if (!isCounterBarrier) {
                        attack.calcDamage();
                    }
                }
                if (isCounterBarrier) {
                    attack.commitCounterBarrier();
                } else {
                    attack.action();
                    attack.commit();
                }
                setSleepTime(calcSleepTime(getAtkspeed(), 1));
                return;
            }
            allTargetClear();
        }
    }

    public void searchTargetItem() {
        ArrayList<L1GroundInventory> gInventorys = new ArrayList<>();
        Iterator<L1Object> it = World.get().getVisibleObjects(this).iterator();
        while (it.hasNext()) {
            L1Object obj = it.next();
            if (obj != null && (obj instanceof L1GroundInventory)) {
                gInventorys.add((L1GroundInventory) obj);
            }
        }
        if (gInventorys.size() != 0) {
            for (L1ItemInstance item : gInventorys.get((int) (Math.random() * ((double) gInventorys.size()))).getItems()) {
                if (getInventory().checkAddItem(item, item.getCount()) == 0) {
                    this._targetItem = item;
                    this._targetItemList.add(this._targetItem);
                }
            }
        }
    }

    public void searchItemFromAir() {
        ArrayList<L1GroundInventory> gInventorys = new ArrayList<>();
        Iterator<L1Object> it = World.get().getVisibleObjects(this).iterator();
        while (it.hasNext()) {
            L1Object obj = it.next();
            if (obj != null && (obj instanceof L1GroundInventory)) {
                gInventorys.add((L1GroundInventory) obj);
            }
        }
        if (gInventorys.size() != 0) {
            for (L1ItemInstance item : gInventorys.get((int) (Math.random() * ((double) gInventorys.size()))).getItems()) {
                if ((item.getItem().getType() == 6 || item.getItem().getType() == 7) && getInventory().checkAddItem(item, item.getCount()) == 0 && getHiddenStatus() == 2) {
                    setHiddenStatus(0);
                    broadcastPacketAll(new S_DoActionGFX(getId(), 45));
                    setStatus(0);
                    broadcastPacketAll(new S_NPCPack(this));
                    onNpcAI();
                    startChat(2);
                    this._targetItem = item;
                    this._targetItemList.add(this._targetItem);
                }
            }
        }
    }

    public static void shuffle(L1Object[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int t = (int) (Math.random() * ((double) i));
            L1Object tmp = arr[i];
            arr[i] = arr[t];
            arr[t] = tmp;
        }
    }

    public void checkTargetItem() {
        if (this._targetItem != null && this._targetItem.getMapId() == getMapId() && getLocation().getTileDistance(this._targetItem.getLocation()) <= 15) {
            return;
        }
        if (!this._targetItemList.isEmpty()) {
            this._targetItem = this._targetItemList.get(0);
            this._targetItemList.remove(0);
            checkTargetItem();
            return;
        }
        this._targetItem = null;
    }

    public void onTargetItem() {
        if (getLocation().getTileLineDistance(this._targetItem.getLocation()) == 0) {
            pickupTargetItem(this._targetItem);
        } else if (this._npcMove != null) {
            int dir = this._npcMove.moveDirection(this._targetItem.getX(), this._targetItem.getY());
            if (dir == -1) {
                this._targetItemList.remove(this._targetItem);
                this._targetItem = null;
                return;
            }
            this._npcMove.setDirectionMove(dir);
            setSleepTime(calcSleepTime(getPassispeed(), 0));
        }
    }

    public void pickupTargetItem(L1ItemInstance targetItem) {
        try {
            onGetItem(World.get().getInventory(targetItem.getX(), targetItem.getY(), targetItem.getMapId()).tradeItem(targetItem, targetItem.getCount(), getInventory()));
            this._targetItemList.remove(this._targetItem);
            this._targetItem = null;
            setSleepTime(L1SkillId.STATUS_BRAVE);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public boolean noTarget() throws Exception {
        int dir;
        int dir2;
        if (this._master == null || this._master.getMapId() != getMapId() || getLocation().getTileLineDistance(this._master.getLocation()) <= 2) {
            if (World.get().getVisiblePlayer(this, 20).size() <= 0) {
                teleport(getHomeX(), getHomeY(), getHeading());
                return true;
            } else if (this._master == null && getPassispeed() > 0 && !isRest()) {
                L1MobGroupInfo mobGroupInfo = getMobGroupInfo();
                if (mobGroupInfo == null || (mobGroupInfo != null && mobGroupInfo.isLeader(this))) {
                    if (this._randomMoveDistance == 0) {
                        this._randomMoveDistance = _random.nextInt(3) + 1;
                        this._randomMoveDirection = _random.nextInt(40);
                        if (!(getHomeX() == 0 || getHomeY() == 0 || this._randomMoveDirection >= 8 || getLocation().getLineDistance(new Point(getHomeX(), getHomeY())) <= 8.0d || this._npcMove == null)) {
                            this._randomMoveDirection = this._npcMove.moveDirection(getHomeX(), getHomeY());
                        }
                    } else {
                        this._randomMoveDistance--;
                    }
                    if (!(this._npcMove == null || this._randomMoveDirection >= 8 || (dir = this._npcMove.openDoor(this._npcMove.checkObject(this._randomMoveDirection))) == -1)) {
                        this._npcMove.setDirectionMove(dir);
                        setSleepTime(calcSleepTime(getPassispeed(), 0));
                    }
                } else {
                    L1NpcInstance leader = mobGroupInfo.getLeader();
                    if (getPassispeed() > 0 && getLocation().getTileLineDistance(leader.getLocation()) > 2 && this._npcMove != null) {
                        int dir3 = this._npcMove.moveDirection(leader.getX(), leader.getY());
                        if (dir3 == -1) {
                            return true;
                        }
                        this._npcMove.setDirectionMove(dir3);
                        setSleepTime(calcSleepTime(getPassispeed(), 0));
                    }
                }
            }
        } else if (getPassispeed() > 0) {
            if (this._npcMove == null || (dir2 = this._npcMove.moveDirection(this._master.getX(), this._master.getY())) == -1) {
                return true;
            }
            this._npcMove.setDirectionMove(dir2);
            setSleepTime(calcSleepTime(getPassispeed(), 0));
        }
        return false;
    }

    public void onFinalAction(L1PcInstance pc, String s) throws Exception {
    }

    private void tagertClear() {
        if (this._target != null) {
            this._hateList.remove(this._target);
            this._target = null;
        }
    }

    public void targetRemove(L1Character target) {
        this._hateList.remove(target);
        if (this._target != null && this._target.equals(target)) {
            this._target = null;
        }
    }

    public void allTargetClear() {
        if (this._npcMove != null) {
            this._npcMove.clear();
        }
        this._hateList.clear();
        this._dropHateList.clear();
        this._target = null;
        this._targetItemList.clear();
        this._targetItem = null;
    }

    public void setMaster(L1Character cha) {
        this._master = cha;
    }

    public L1Character getMaster() {
        return this._master;
    }

    public void onNpcAI() {
    }

    public void refineItem() throws Exception {
        if (this._npcTemplate.get_npcId() == 45032) {
            if (getExp() != 0 && !this._inventory.checkItem(20)) {
                int[] materials = {40508, 40521, 40045};
                int[] counts = {150, 3, 3};
                int[] createitem = {20};
                int[] createcount = {1};
                if (this._inventory.checkItem(materials, counts)) {
                    for (int i = 0; i < materials.length; i++) {
                        this._inventory.consumeItem(materials[i], (long) counts[i]);
                    }
                    for (int j = 0; j < createitem.length; j++) {
                        this._inventory.storeItem(createitem[j], (long) createcount[j]);
                    }
                }
            }
            if (getExp() != 0 && !this._inventory.checkItem(19)) {
                int[] materials2 = {40494, 40521};
                int[] counts2 = {150, 3};
                int[] createitem2 = {19};
                int[] createcount2 = {1};
                if (this._inventory.checkItem(materials2, counts2)) {
                    for (int i2 = 0; i2 < materials2.length; i2++) {
                        this._inventory.consumeItem(materials2[i2], (long) counts2[i2]);
                    }
                    for (int j2 = 0; j2 < createitem2.length; j2++) {
                        this._inventory.storeItem(createitem2[j2], (long) createcount2[j2]);
                    }
                }
            }
            if (getExp() != 0 && !this._inventory.checkItem(3)) {
                int[] materials3 = {40494, 40521};
                int[] counts3 = {50, 1};
                int[] createitem3 = {3};
                int[] createcount3 = {1};
                if (this._inventory.checkItem(materials3, counts3)) {
                    for (int i3 = 0; i3 < materials3.length; i3++) {
                        this._inventory.consumeItem(materials3[i3], (long) counts3[i3]);
                    }
                    for (int j3 = 0; j3 < createitem3.length; j3++) {
                        this._inventory.storeItem(createitem3[j3], (long) createcount3[j3]);
                    }
                }
            }
            if (getExp() != 0 && !this._inventory.checkItem(100)) {
                int[] materials4 = {88, 40508, 40045};
                int[] counts4 = {4, 80, 3};
                int[] createitem4 = {100};
                int[] createcount4 = {1};
                if (this._inventory.checkItem(materials4, counts4)) {
                    for (int i4 = 0; i4 < materials4.length; i4++) {
                        this._inventory.consumeItem(materials4[i4], (long) counts4[i4]);
                    }
                    for (int j4 = 0; j4 < createitem4.length; j4++) {
                        this._inventory.storeItem(createitem4[j4], (long) createcount4[j4]);
                    }
                }
            }
            if (!(getExp() == 0 || this._inventory.checkItem(89))) {
                int[] materials5 = {88, 40494};
                int[] counts5 = {2, 80};
                int[] createitem5 = {89};
                int[] createcount5 = {1};
                if (this._inventory.checkItem(materials5, counts5)) {
                    for (int i5 = 0; i5 < materials5.length; i5++) {
                        this._inventory.consumeItem(materials5[i5], (long) counts5[i5]);
                    }
                    for (int j5 = 0; j5 < createitem5.length; j5++) {
                        L1ItemInstance item = this._inventory.storeItem(createitem5[j5], (long) createcount5[j5]);
                        if (getNpcTemplate().get_digestitem() > 0) {
                            setDigestItem(item);
                        }
                    }
                }
            }
        } else if ((this._npcTemplate.get_npcId() == 45166 || this._npcTemplate.get_npcId() == 45167) && getExp() != 0 && !this._inventory.checkItem(40726)) {
            int[] materials6 = {40725};
            int[] counts6 = {1};
            int[] createitem6 = {40726};
            int[] createcount6 = {1};
            if (this._inventory.checkItem(materials6, counts6)) {
                for (int i6 = 0; i6 < materials6.length; i6++) {
                    this._inventory.consumeItem(materials6[i6], (long) counts6[i6]);
                }
                for (int j6 = 0; j6 < createitem6.length; j6++) {
                    this._inventory.storeItem(createitem6[j6], (long) createcount6[j6]);
                }
            }
        }
    }

    public L1HateList getHateList() {
        return this._hateList;
    }

    public final boolean isHpR() {
        if (!destroyed() && !isDead() && getMaxHp() > 0 && getCurrentHp() > 0 && getCurrentHp() < getMaxHp()) {
            return true;
        }
        return false;
    }

    public final void startHpRegeneration() {
        if ((!destroyed() || !isDead()) && getCurrentHp() > 0 && !this._hprRunning) {
            this._hprRunning = true;
        }
    }

    public final boolean isHpRegenerationX() {
        return this._hprRunning;
    }

    public final void stopHpRegeneration() {
        if (this._hprRunning) {
            this._hprRunning = false;
        }
    }

    public final boolean isMpR() {
        if (!destroyed() && !isDead() && getMaxHp() > 0 && getMaxMp() > 0 && getCurrentHp() > 0 && getCurrentMp() < getMaxMp()) {
            return true;
        }
        return false;
    }

    public final void startMpRegeneration() {
        if ((!destroyed() || !isDead()) && getCurrentHp() > 0 && !this._mprRunning) {
            this._mprRunning = true;
        }
    }

    public final boolean isMpRegenerationX() {
        return this._mprRunning;
    }

    public final void stopMpRegeneration() {
        if (this._mprRunning) {
            this._mprRunning = false;
        }
    }

    public L1NpcInstance(L1Npc template) {
        setStatus(0);
        setMoveSpeed(0);
        setDead(false);
        setStatus(0);
        setreSpawn(false);
        this._npcMove = new NpcMove(this);
        if (template != null) {
            setting_template(template);
        }
    }

    public void setting_template(L1Npc template) {
        this._npcTemplate = template;
        double rate = 0.0d;
        double diff = 0.0d;
        setName(template.get_name());
        setNameId(String.valueOf(newName(template.get_npcId())) + template.get_nameid());
        if (template.get_randomlevel() == 0) {
            setLevel(template.get_level());
        } else {
            int randomlevel = _random.nextInt((template.get_randomlevel() - template.get_level()) + 1);
            diff = (double) (template.get_randomlevel() - template.get_level());
            rate = ((double) randomlevel) / diff;
            setLevel(randomlevel + template.get_level());
        }
        if (template.get_randomhp() == 0) {
            setMaxHp(template.get_hp());
            setCurrentHpDirect(template.get_hp());
        } else {
            int hp = (int) (((double) template.get_hp()) + (rate * ((double) (template.get_randomhp() - template.get_hp()))));
            setMaxHp(hp);
            setCurrentHpDirect(hp);
        }
        if (template.get_randommp() == 0) {
            setMaxMp(template.get_mp());
            setCurrentMpDirect(template.get_mp());
        } else {
            int mp = (int) (((double) template.get_mp()) + (rate * ((double) (template.get_randommp() - template.get_mp()))));
            setMaxMp(mp);
            setCurrentMpDirect(mp);
        }
        if (template.get_randomac() == 0) {
            setAc(template.get_ac());
        } else {
            setAc((int) (((double) template.get_ac()) + (rate * ((double) (template.get_randomac() - template.get_ac())))));
        }
        if (template.get_randomlevel() == 0) {
            setStr(template.get_str());
            setCon(template.get_con());
            setDex(template.get_dex());
            setInt(template.get_int());
            setWis(template.get_wis());
            setMr(template.get_mr());
        } else {
            setStr((byte) ((int) Math.min(((double) template.get_str()) + diff, 127.0d)));
            setCon((byte) ((int) Math.min(((double) template.get_con()) + diff, 127.0d)));
            setDex((byte) ((int) Math.min(((double) template.get_dex()) + diff, 127.0d)));
            setInt((byte) ((int) Math.min(((double) template.get_int()) + diff, 127.0d)));
            setWis((byte) ((int) Math.min(((double) template.get_wis()) + diff, 127.0d)));
            setMr((byte) ((int) Math.min(((double) template.get_mr()) + diff, 127.0d)));
            addHitup(((int) diff) * 2);
            addDmgup(((int) diff) * 2);
        }
        setPassispeed(template.get_passispeed());
        setAtkspeed(template.get_atkspeed());
        setAgro(template.is_agro());
        setAgrocoi(template.is_agrocoi());
        setAgrososc(template.is_agrososc());
        if (getNpcTemplate().get_weakAttr() != 0) {
            switch (getNpcTemplate().get_weakAttr()) {
                case OpcodesClient.C_OPCODE_SOLDIERGIVEOK:
                    addWind(50);
                    break;
                case OpcodesClient.C_OPCODE_HORUN:
                    addWater(50);
                    break;
                case OpcodesClient.C_OPCODE_HIRESOLDIER:
                    addFire(50);
                    break;
                case -1:
                    addEarth(50);
                    break;
                case 1:
                    addEarth(-50);
                    break;
                case 2:
                    addFire(-50);
                    break;
                case 4:
                    addWater(-50);
                    break;
                case 8:
                    addWind(-50);
                    break;
            }
        }
        int gfxid = newGfx(template.get_gfxid());
        setTempCharGfx(gfxid);
        setGfxId(gfxid);
        setGfxidInStatus(gfxid);
        if (template.get_randomexp() == 0) {
            setExp((long) template.get_exp());
        } else {
            int level = getLevel();
            setExp((long) ((level * level) + 1));
        }
        if (template.get_randomlawful() == 0) {
            setLawful(template.get_lawful());
            setTempLawful(template.get_lawful());
        } else {
            int lawful = (int) (((double) template.get_lawful()) + (rate * ((double) (template.get_randomlawful() - template.get_lawful()))));
            setLawful(lawful);
            setTempLawful(lawful);
        }
        setPickupItem(template.is_picupitem());
        if (template.is_bravespeed()) {
            setBraveSpeed(1);
        } else {
            setBraveSpeed(0);
        }
        setKarma(template.getKarma());
        setLightSize(template.getLightSize());
        if (template.talk()) {
            this.TALK = template.getNpcExecutor();
        }
        if (template.action()) {
            this.ACTION = template.getNpcExecutor();
        }
        if (template.attack()) {
            this.ATTACK = template.getNpcExecutor();
        }
        if (template.death()) {
            this.DEATH = template.getNpcExecutor();
        }
        if (template.work()) {
            this.WORK = template.getNpcExecutor();
            if (this.WORK.workTime() != 0) {
                NpcWorkTimer.put(this, Integer.valueOf(this.WORK.workTime()));
            } else {
                this.WORK.work(this);
            }
        }
        if (template.spawn()) {
            this.SPAWN = template.getNpcExecutor();
            this.SPAWN.spawn(this);
        }
        this._mobSkill = new L1MobSkillUse(this);
    }

    public void setGfxidInStatus(int gfxid) {
        switch (gfxid) {
            case 51:
            case 110:
            case L1SkillId.ELEMENTAL_PROTECTION /*{ENCODED_INT: 147}*/:
                setStatus(24);
                return;
            case 57:
            case 816:
            case 3137:
            case 3140:
            case 3145:
            case 3148:
            case 3151:
            case 7621:
                setStatus(20);
                return;
            case 111:
                setStatus(4);
                return;
            default:
                return;
        }
    }

    private String newName(int npcid) {
        return "";
    }

    private int newGfx(int get_gfxid) {
        int[] r = null;
        switch (get_gfxid) {
            case 998:
            case 999:
            case L1SkillId.STATUS_BLUE_POTION /*{ENCODED_INT: 1002}*/:
            case L1SkillId.STATUS_UNDERWATER_BREATH /*{ENCODED_INT: 1003}*/:
                r = new int[]{998, 999, L1SkillId.STATUS_BLUE_POTION, L1SkillId.STATUS_UNDERWATER_BREATH};
                break;
            case 1318:
            case 1321:
                r = new int[]{1318, 1321};
                break;
            case 1597:
            case 1600:
                r = new int[]{1597, 1600};
                break;
            case 5942:
                r = new int[]{5942, 5135, 5137, 5139, 5141, 5143, 5145, 5156, 5158, 5160, 5162};
                break;
        }
        if (r != null) {
            return r[_random.nextInt(r.length)];
        }
        return get_gfxid;
    }

    public int getPassispeed() {
        return this._passispeed;
    }

    public void setPassispeed(int i) {
        this._passispeed = i;
    }

    public int getAtkspeed() {
        return this._atkspeed;
    }

    public void setAtkspeed(int i) {
        this._atkspeed = i;
    }

    public boolean isPickupItem() {
        return this._pickupItem;
    }

    public void setPickupItem(boolean flag) {
        this._pickupItem = flag;
    }

    @Override // com.lineage.server.model.L1Character
    public L1Inventory getInventory() {
        return this._inventory;
    }

    public void setInventory(L1Inventory inventory) {
        this._inventory = inventory;
    }

    public L1Npc getNpcTemplate() {
        return this._npcTemplate;
    }

    public int getNpcId() {
        return this._npcTemplate.get_npcId();
    }

    public void setPetcost(int i) {
        this._petcost = i;
    }

    public int getPetcost() {
        return this._petcost;
    }

    public void setSpawn(L1Spawn spawn) {
        this._spawn = spawn;
    }

    public L1Spawn getSpawn() {
        return this._spawn;
    }

    public void setSpawnNumber(int number) {
        this._spawnNumber = number;
    }

    public int getSpawnNumber() {
        return this._spawnNumber;
    }

    public void onDecay(boolean isReuseId) {
        int id;
        if (isReuseId) {
            id = getId();
        } else {
            id = 0;
        }
        this._spawn.executeSpawnTask(this._spawnNumber, id);
    }

    @Override // com.lineage.server.model.L1Object
    public void onPerceive(L1PcInstance perceivedFrom) {
        try {
            perceivedFrom.addKnownObject(this);
            perceivedFrom.sendPackets(new S_NPCPack(this));
            onNpcAI();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void deleteMe() {
        L1PcInstance masterPc;
        if (!this._destroyed) {
            int tgid = 0;
            switch (getNpcId()) {
                case 92000:
                    tgid = 92001;
                    break;
                case 92001:
                    tgid = 92000;
                    break;
            }
            if ((this instanceof L1IllusoryInstance) && getMaster() != null && (getMaster() instanceof L1PcInstance) && (masterPc = (L1PcInstance) getMaster()) != null) {
                masterPc.get_otherList().removeIllusoryList(Integer.valueOf(getId()));
            }
            L1NpcInstance tgMob = null;
            if (tgid != 0) {
                Iterator<L1Object> it = World.get().getVisibleObjects(this).iterator();
                while (true) {
                    if (it.hasNext()) {
                        L1Object objects = it.next();
                        if (objects instanceof L1NpcInstance) {
                            L1NpcInstance tgMobR = (L1NpcInstance) objects;
                            if (tgMobR.getNpcId() != tgid) {
                                continue;
                            } else if (!tgMobR.isDead()) {
                                resurrect(getMaxHp());
                                return;
                            } else if (!tgMobR._destroyed) {
                                tgMob = tgMobR;
                            }
                        }
                    }
                }
            }
            this._destroyed = true;
            if (getInventory() != null) {
                getInventory().clearItems();
            }
            allTargetClear();
            this._master = null;
            int showid = get_showId();
            if (WorldQuest.get().isQuest(showid)) {
                WorldQuest.get().remove(showid, this);
            }
            if (!getDolls().isEmpty()) {
                Object[] array = getDolls().values().toArray();
                int length = array.length;
                for (int i = 0; i < length; i++) {
                    L1DollInstance doll = (L1DollInstance) array[i];
                    if (doll != null) {
                        doll.deleteDoll();
                    }
                }
            }
            if (!getPetList().isEmpty()) {
                Object[] array2 = getPetList().values().toArray();
                int length2 = array2.length;
                for (int i2 = 0; i2 < length2; i2++) {
                    L1NpcInstance summon = (L1NpcInstance) array2[i2];
                    if (summon != null && (summon instanceof L1SummonInstance)) {
                        ((L1SummonInstance) summon).deleteMe();
                    }
                }
            }
            this._hateList.clear();
            this._dropHateList.clear();
            this._targetItemList.clear();
            this._del_map.clear();
            World.get().removeVisibleObject(this);
            World.get().removeObject(this);
            List<L1PcInstance> players = World.get().getRecognizePlayer(this);
            if (players.size() > 0) {
                for (L1PcInstance tgpc : players) {
                    if (tgpc != null) {
                        tgpc.removeKnownObject(this);
                        tgpc.sendPackets(new S_RemoveObject(this));
                    }
                }
            }
            removeAllKnownObjects();
            if (tgMob != null) {
                tgMob.deleteMe();
            }
            try {
                if (getNpcTemplate().is_boss() && this._spawn.get_nextSpawnTime() != null) {
                    long newTime = this._spawn.get_spawnInterval() * 60 * 1000;
                    Calendar cals = Calendar.getInstance();
                    cals.setTimeInMillis(System.currentTimeMillis() + newTime);
                    this._spawn.get_nextSpawnTime().setTimeInMillis(cals.getTimeInMillis());
                    SpawnBossReading.get().upDateNextSpawnTime(this._spawn.getId(), cals);
                    switch (this._spawn.getHeading()) {
                        case 0:
                            World.get().broadcastPacketToAll(new S_ServerMessage(String.valueOf(getNameId()) + "已經死亡，下次出現時間" + new SimpleDateFormat("yyyy/MM/dd HH:mm").format(this._spawn.get_nextSpawnTime().getTime())));
                            break;
                        case 1:
                            World.get().broadcastPacketToAll(new S_ServerMessage(String.valueOf(getNameId()) + "已經死亡!"));
                            break;
                    }
                }
            } catch (Exception ignored) {
            }
            L1MobGroupInfo mobGroupInfo = getMobGroupInfo();
            if (mobGroupInfo != null && mobGroupInfo.removeMember(this) == 0) {
                setMobGroupInfo(null);
            }
            if (isReSpawn()) {
                this._spawn.executeSpawnTask(this._spawnNumber, 0);
            }
        }
    }

    public void outParty(L1NpcInstance mob) {
        for (L1NpcInstance tgNpc : WorldNpc.get().all()) {
            if (!tgNpc.isDead() && tgNpc.getMaster() != null && tgNpc.getMaster().equals(mob)) {
                tgNpc.broadcastPacketAll(new S_SkillSound(tgNpc.getId(), 2236));
                tgNpc.deleteMe();
            }
        }
    }

    public void ReceiveManaDamage(L1Character attacker, int damageMp) {
    }

    public void receiveDamage(L1Character attacker, int damage) throws Exception {
    }

    public class DelItemTime {
        public int _del_item_time = 0;

        public DelItemTime() {
        }
    }

    public void setDigestItem(L1ItemInstance item) {
        DelItemTime delItemTime = new DelItemTime();
        delItemTime._del_item_time = getNpcTemplate().get_digestitem();
        this._del_map.put(item, delItemTime);
    }

    public HashMap<L1ItemInstance, DelItemTime> getDigestItem() {
        return this._del_map;
    }

    public void getDigestItemClear() {
        this._del_map.clear();
    }

    public boolean getDigestItemEmpty() {
        return this._del_map.isEmpty();
    }

    public void onGetItem(L1ItemInstance item) throws Exception {
        refineItem();
        getInventory().shuffle();
        if (getNpcTemplate().get_digestitem() > 0) {
            setDigestItem(item);
        }
    }

    public void approachPlayer(L1PcInstance pc) {
        if (!pc.hasSkillEffect(60) && !pc.hasSkillEffect(97)) {
            switch (getHiddenStatus()) {
                case 1:
                    if (getCurrentHp() == getMaxHp() && pc.getLocation().getTileLineDistance(getLocation()) <= 2) {
                        appearOnGround(pc);
                        return;
                    }
                    return;
                case 2:
                    if (getCurrentHp() != getMaxHp()) {
                        searchItemFromAir();
                        return;
                    } else if (pc.getLocation().getTileLineDistance(getLocation()) <= 1) {
                        appearOnGround(pc);
                        return;
                    } else {
                        return;
                    }
                case 3:
                    if (getCurrentHp() < getMaxHp()) {
                        appearOnGround(pc);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void appearOnGround(L1PcInstance pc) {
        switch (getHiddenStatus()) {
            case 1:
            case 3:
                setHiddenStatus(0);
                broadcastPacketAll(new S_DoActionGFX(getId(), 4));
                setStatus(0);
                broadcastPacketAll(new S_NPCPack(this));
                if (!pc.hasSkillEffect(60) && !pc.hasSkillEffect(97) && !pc.isGm()) {
                    this._hateList.add(pc, 0);
                    this._target = pc;
                }
                onNpcAI();
                return;
            case 2:
                setHiddenStatus(0);
                broadcastPacketAll(new S_DoActionGFX(getId(), 45));
                setStatus(0);
                broadcastPacketAll(new S_NPCPack(this));
                if (!pc.hasSkillEffect(60) && !pc.hasSkillEffect(97) && !pc.isGm()) {
                    this._hateList.add(pc, 0);
                    this._target = pc;
                }
                onNpcAI();
                startChat(2);
                return;
            default:
                return;
        }
    }

    public L1Character is_now_target() {
        return this._target;
    }

    public L1ItemInstance is_now_targetItem() {
        return this._targetItem;
    }

    public void set_now_targetItem(L1ItemInstance item) {
        this._targetItem = item;
    }

    public NpcMoveExecutor getMove() {
        return this._npcMove;
    }

    private void useHealPotion(int healHp, int effectId) {
        broadcastPacketAll(new S_SkillSound(getId(), effectId));
        if (hasSkillEffect(173)) {
            healHp >>= 1;
        }
        if (hasSkillEffect(L1SkillId.ADLV80_2_2)) {
            healHp >>= 1;
        }
        if (hasSkillEffect(L1SkillId.ADLV80_2_1)) {
            healHp *= -1;
        }
        if (this instanceof L1PetInstance) {
            ((L1PetInstance) this).setCurrentHp(getCurrentHp() + healHp);
        } else if (this instanceof L1SummonInstance) {
            ((L1SummonInstance) this).setCurrentHp(getCurrentHp() + healHp);
        } else {
            setCurrentHpDirect(getCurrentHp() + healHp);
        }
    }

    private void useHastePotion(int time) {
        broadcastPacketAll(new S_SkillHaste(getId(), 1, time));
        broadcastPacketX8(new S_SkillSound(getId(), L1SkillId.MORTAL_BODY));
        setMoveSpeed(1);
        setSkillEffect(L1SkillId.STATUS_HASTE, time * L1SkillId.STATUS_BRAVE);
    }

    public void useItem(int type, int chance) throws Exception {
        if (!hasSkillEffect(71) && _random.nextInt(100) <= chance && getInventory() != null) {
            switch (type) {
                case 0:
                    if (getInventory().consumeItem(L1ItemId.POTION_OF_GREATER_HEALING, serialVersionUID)) {
                        useHealPotion(75, 197);
                        return;
                    } else if (getInventory().consumeItem(L1ItemId.POTION_OF_EXTRA_HEALING, serialVersionUID)) {
                        useHealPotion(45, 194);
                        return;
                    } else if (getInventory().consumeItem(L1ItemId.POTION_OF_HEALING, serialVersionUID)) {
                        useHealPotion(15, L1SkillId.SHOCK_SKIN);
                        return;
                    } else {
                        return;
                    }
                case 1:
                    if (hasSkillEffect(L1SkillId.STATUS_HASTE)) {
                        return;
                    }
                    if (getInventory().consumeItem(L1ItemId.B_POTION_OF_GREATER_HASTE_SELF, serialVersionUID)) {
                        useHastePotion(2100);
                        return;
                    } else if (getInventory().consumeItem(L1ItemId.POTION_OF_GREATER_HASTE_SELF, serialVersionUID)) {
                        useHastePotion(1800);
                        return;
                    } else if (getInventory().consumeItem(L1ItemId.B_POTION_OF_HASTE_SELF, serialVersionUID)) {
                        useHastePotion(350);
                        return;
                    } else if (getInventory().consumeItem(L1ItemId.POTION_OF_HASTE_SELF, serialVersionUID)) {
                        useHastePotion(300);
                        return;
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    }

    public void setDirectionMoveSrc(int dir) {
        if (dir >= 0 && dir <= 7) {
            int locx = getX() + HEADING_TABLE_X[dir];
            setHeading(dir);
            setX(locx);
            setY(getY() + HEADING_TABLE_Y[dir]);
        }
    }

    public boolean nearTeleport(int nx, int ny) {
        int rdir = _random.nextInt(8);
        for (int i = 0; i < 8; i++) {
            int dir = rdir + i;
            if (dir > 7) {
                dir -= 8;
            }
            nx += HEADING_TABLE_X[dir];
            ny += HEADING_TABLE_Y[dir];
            if (getMap().isPassable(nx, ny, this)) {
                int dir2 = dir + 4;
                if (dir2 > 7) {
                    dir2 -= 8;
                }
                teleport(nx, ny, dir2);
                setCurrentMp(getCurrentMp() - 10);
                return true;
            }
        }
        return false;
    }

    public void teleport(int nx, int ny, int dir) {
        try {
            Iterator<L1PcInstance> it = World.get().getRecognizePlayer(this).iterator();
            while (it.hasNext()) {
                L1PcInstance pc = it.next();
                pc.sendPackets(new S_SkillSound(getId(), L1SkillId.EXOTIC_VITALIZE));
                pc.sendPackets(new S_RemoveObject(this));
                pc.removeKnownObject(this);
            }
            setX(nx);
            setY(ny);
            setHeading(dir);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public String getNameId() {
        return this._nameId;
    }

    public void setNameId(String s) {
        this._nameId = s;
    }

    public boolean isAgro() {
        return this._Agro;
    }

    public void setAgro(boolean flag) {
        this._Agro = flag;
    }

    public boolean isAgrocoi() {
        return this._Agrocoi;
    }

    public void setAgrocoi(boolean flag) {
        this._Agrocoi = flag;
    }

    public boolean isAgrososc() {
        return this._Agrososc;
    }

    public void setAgrososc(boolean flag) {
        this._Agrososc = flag;
    }

    public int getHomeX() {
        return this._homeX;
    }

    public void setHomeX(int i) {
        this._homeX = i;
    }

    public int getHomeY() {
        return this._homeY;
    }

    public void setHomeY(int i) {
        this._homeY = i;
    }

    public boolean isReSpawn() {
        return this._reSpawn;
    }

    public void setreSpawn(boolean flag) {
        this._reSpawn = flag;
    }

    public int getLightSize() {
        return this._lightSize;
    }

    public void setLightSize(int i) {
        this._lightSize = i;
    }

    public boolean isWeaponBreaked() {
        return this._weaponBreaked;
    }

    public void setWeaponBreaked(boolean flag) {
        this._weaponBreaked = flag;
    }

    public int getHiddenStatus() {
        return this._hiddenStatus;
    }

    public void setHiddenStatus(int i) {
        this._hiddenStatus = i;
    }

    public int getMovementDistance() {
        return this._movementDistance;
    }

    public void setMovementDistance(int i) {
        this._movementDistance = i;
    }

    public int getTempLawful() {
        return this._tempLawful;
    }

    public void setTempLawful(int i) {
        this._tempLawful = i;
    }

    public int calcSleepTime(int sleepTime, int type) {
        if (sleepTime <= 0) {
            sleepTime = 960;
        }
        switch (getMoveSpeed()) {
            case 1:
                sleepTime = (int) (((double) sleepTime) - (((double) sleepTime) * 0.25d));
                break;
            case 2:
                sleepTime *= 2;
                break;
        }
        if (getBraveSpeed() == 1) {
            sleepTime = (int) (((double) sleepTime) - (((double) sleepTime) * 0.25d));
        }
        if (!hasSkillEffect(167)) {
            return sleepTime;
        }
        if (type == 1 || type == 2) {
            return (int) (((double) sleepTime) + (((double) sleepTime) * 0.25d));
        }
        return sleepTime;
    }

    /* access modifiers changed from: protected */
    public void setAiRunning(boolean aiRunning) {
        this._aiRunning = aiRunning;
    }

    /* access modifiers changed from: protected */
    public boolean isAiRunning() {
        return this._aiRunning;
    }

    public boolean destroyed() {
        return this._destroyed;
    }

    /* access modifiers changed from: protected */
    public L1MobSkillUse mobSkill() {
        return this._mobSkill;
    }

    /* access modifiers changed from: protected */
    public void setActived(boolean actived) {
        this._actived = actived;
    }

    /* access modifiers changed from: protected */
    public boolean isActived() {
        return this._actived;
    }

    /* access modifiers changed from: protected */
    public void setFirstAttack(boolean firstAttack) {
        this._firstAttack = firstAttack;
    }

    /* access modifiers changed from: protected */
    public boolean isFirstAttack() {
        return this._firstAttack;
    }

    public void setSleepTime(int sleep_time) {
        this._sleep_time = sleep_time;
    }

    /* access modifiers changed from: protected */
    public int getSleepTime() {
        return this._sleep_time;
    }

    /* access modifiers changed from: protected */
    public void setDeathProcessing(boolean deathProcessing) {
        this._deathProcessing = deathProcessing;
    }

    /* access modifiers changed from: protected */
    public boolean isDeathProcessing() {
        return this._deathProcessing;
    }

    public int drainMana(int drain) {
        if (this._drainedMana >= 40) {
            return 0;
        }
        int result = Math.min(drain, getCurrentMp());
        if (this._drainedMana + result > 40) {
            result = 40 - this._drainedMana;
        }
        this._drainedMana += result;
        return result;
    }

    /* access modifiers changed from: protected */
    public void transform(int transformId) {
        stopHpRegeneration();
        stopMpRegeneration();
        int transformGfxId = getNpcTemplate().getTransformGfxId();
        if (transformGfxId != 0) {
            broadcastPacketAll(new S_SkillSound(getId(), transformGfxId));
        }
        setting_template(NpcTable.get().getTemplate(transformId));
        broadcastPacketAll(new S_ChangeShape(this, getTempCharGfx()));
        Iterator<L1PcInstance> it = World.get().getRecognizePlayer(this).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            if (pc.get_showId() == get_showId()) {
                onPerceive(pc);
            }
        }
    }

    public void setRest(boolean rest) {
        this._rest = rest;
    }

    public boolean isRest() {
        return this._rest;
    }

    public boolean isResurrect() {
        return this._isResurrect;
    }

    public void setResurrect(boolean flag) {
        this._isResurrect = flag;
    }

    @Override // com.lineage.server.model.L1Character
    public synchronized void resurrect(int hp) {
        if (!this._destroyed) {
            super.resurrect(hp);
            startHpRegeneration();
            startMpRegeneration();
            broadcastPacketAll(new S_Resurrection(this, this, 0));
            new L1SkillUse().handleCommands(null, 44, getId(), getX(), getY(), 0, 1, this);
            if (this._deadTimerTemp != -1) {
                this._deadTimerTemp = -1;
            }
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void startDeleteTimer(int deltime) {
        if (this._deadTimerTemp == -1) {
            this._deadTimerTemp = deltime;
        }
    }

    public boolean isInMobGroup() {
        return getMobGroupInfo() != null;
    }

    public L1MobGroupInfo getMobGroupInfo() {
        return this._mobGroupInfo;
    }

    public void setMobGroupInfo(L1MobGroupInfo m) {
        this._mobGroupInfo = m;
    }

    public int getMobGroupId() {
        return this._mobGroupId;
    }

    public void setMobGroupId(int i) {
        this._mobGroupId = i;
    }

    public void startChat(int chatTiming) {
        if (chatTiming == 0 && isDead()) {
            return;
        }
        if (chatTiming == 1 && !isDead()) {
            return;
        }
        if (chatTiming == 2 && isDead()) {
            return;
        }
        if (chatTiming != 3 || !isDead()) {
            int npcId = getNpcTemplate().get_npcId();
            L1NpcChat npcChat = null;
            switch (chatTiming) {
                case 0:
                    npcChat = NpcChatTable.get().getTemplateAppearance(npcId);
                    break;
                case 1:
                    npcChat = NpcChatTable.get().getTemplateDead(npcId);
                    break;
                case 2:
                    npcChat = NpcChatTable.get().getTemplateHide(npcId);
                    break;
                case 3:
                    npcChat = NpcChatTable.get().getTemplateGameTime(npcId);
                    break;
            }
            if (npcChat != null) {
                Timer timer = new Timer(true);
                L1NpcChatTimer npcChatTimer = new L1NpcChatTimer(this, npcChat);
                if (!npcChat.isRepeat()) {
                    timer.schedule(npcChatTimer, (long) npcChat.getStartDelayTime());
                } else {
                    timer.scheduleAtFixedRate(npcChatTimer, (long) npcChat.getStartDelayTime(), (long) npcChat.getRepeatInterval());
                }
            }
        }
    }

    public int getBowActId() {
        if (this._bowActId != -1) {
            return this._bowActId;
        }
        return getNpcTemplate().getBowActId();
    }

    public void setBowActId(int bowActId) {
        this._bowActId = bowActId;
    }

    public int get_ranged() {
        if (this._ranged != -1) {
            return this._ranged;
        }
        return getNpcTemplate().get_ranged();
    }

    public void set_ranged(int ranged) {
        this._ranged = ranged;
    }

    public void set_spawnTime(int spawnTime) {
        this._spawnTime = spawnTime;
        this._isspawnTime = true;
    }

    public int get_spawnTime() {
        return this._spawnTime;
    }

    public boolean is_spawnTime() {
        return this._isspawnTime;
    }

    public boolean isremovearmor() {
        return this._isremovearmor;
    }

    public void set_removearmor(boolean isremovearmor) {
        this._isremovearmor = isremovearmor;
    }

    public void set_deadTimerTemp(int time) {
        this._deadTimerTemp = time;
    }

    public int get_deadTimerTemp() {
        return this._deadTimerTemp;
    }

    public void set_stop_time(int time) {
        this._stop_time = time;
    }

    public int get_stop_time() {
        return this._stop_time;
    }

    public boolean isDropitems() {
        return this._isDropitems;
    }

    public void setDropItems(boolean i) {
        this._isDropitems = i;
    }

    public boolean forDropitems() {
        return this._forDropitems;
    }

    public void giveDropItems(boolean i) {
        this._forDropitems = i;
    }
}
