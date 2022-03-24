package com.lineage.server.model.Instance;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigRate;
import com.lineage.server.datatables.NpcScoreTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.UBTable;
import com.lineage.server.datatables.lock.SpawnBossReading;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.L1UltimateBattle;
import com.lineage.server.model.drop.DropShare;
import com.lineage.server.model.drop.SetDrop;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBoxGree1;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.CalcExp;
import com.lineage.server.utils.CheckUtil;
import com.lineage.server.world.World;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import william.L1WilliamSystemMessage;
import william.Reward_Ub;

public class L1MonsterInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1MonsterInstance.class);
    private static final Random _random = new Random();
    private static final long serialVersionUID = 1;
    private long _lasthprtime = 0;
    private long _lastmprtime = 0;
    private boolean _storeDroped = false;
    private int _ubId = 0;
    private int _ubSealCount = 0;

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onItemUse() throws Exception {
        if (!isActived() && this._target != null) {
            useItem(1, 40);
            if (getNpcTemplate().is_doppel() && (this._target instanceof L1PcInstance)) {
                setName(this._target.getName());
                setNameId(this._target.getName());
                setTitle(this._target.getTitle());
                setTempLawful(this._target.getLawful());
                switch (((L1PcInstance) this._target).getClassId()) {
                    case 0:
                        setTempCharGfx(5853);
                        setGfxId(5853);
                        setStatus(4);
                        setAtkspeed(900);
                        break;
                    case 1:
                        setTempCharGfx(5854);
                        setGfxId(5854);
                        setStatus(4);
                        setAtkspeed(900);
                        break;
                    case 37:
                        setTempCharGfx(5858);
                        setGfxId(5858);
                        setStatus(4);
                        setAtkspeed(900);
                        break;
                    case 48:
                        setTempCharGfx(5856);
                        setGfxId(5856);
                        setStatus(4);
                        setAtkspeed(900);
                        break;
                    case 61:
                        setTempCharGfx(5855);
                        setGfxId(5855);
                        setStatus(4);
                        setAtkspeed(900);
                        break;
                    case 138:
                        setTempCharGfx(5857);
                        setGfxId(5857);
                        setStatus(4);
                        setAtkspeed(900);
                        break;
                    case L1PcInstance.CLASSID_WIZARD_MALE /*{ENCODED_INT: 734}*/:
                        setTempCharGfx(5859);
                        setGfxId(5859);
                        setStatus(4);
                        setAtkspeed(900);
                        break;
                    case L1PcInstance.CLASSID_WIZARD_FEMALE /*{ENCODED_INT: 1186}*/:
                        setTempCharGfx(5860);
                        setGfxId(5860);
                        setStatus(4);
                        setAtkspeed(900);
                        break;
                    case L1PcInstance.CLASSID_DARK_ELF_MALE /*{ENCODED_INT: 2786}*/:
                        setTempCharGfx(5861);
                        setGfxId(5861);
                        setStatus(4);
                        setAtkspeed(900);
                        break;
                    case L1PcInstance.CLASSID_DARK_ELF_FEMALE /*{ENCODED_INT: 2796}*/:
                        setTempCharGfx(5862);
                        setGfxId(5862);
                        setStatus(4);
                        setAtkspeed(900);
                        break;
                    case L1PcInstance.CLASSID_ILLUSIONIST_FEMALE /*{ENCODED_INT: 6650}*/:
                        setTempCharGfx(7142);
                        setGfxId(7142);
                        setStatus(40);
                        setAtkspeed(880);
                        break;
                    case L1PcInstance.CLASSID_DRAGON_KNIGHT_MALE /*{ENCODED_INT: 6658}*/:
                        setTempCharGfx(7139);
                        setGfxId(7139);
                        setStatus(4);
                        setAtkspeed(900);
                        break;
                    case L1PcInstance.CLASSID_DRAGON_KNIGHT_FEMALE /*{ENCODED_INT: 6661}*/:
                        setTempCharGfx(7140);
                        setGfxId(7140);
                        setStatus(4);
                        setAtkspeed(900);
                        break;
                    case 6671:
                        setTempCharGfx(7141);
                        setGfxId(7141);
                        setStatus(40);
                        setAtkspeed(880);
                        break;
                }
                setPassispeed(640);
                Iterator<L1PcInstance> it = World.get().getRecognizePlayer(this).iterator();
                while (it.hasNext()) {
                    L1PcInstance pc = it.next();
                    pc.sendPackets(new S_RemoveObject(this));
                    pc.removeKnownObject(this);
                    pc.updateObject();
                }
            }
        }
        if ((getCurrentHp() * 100) / getMaxHp() < 40) {
            useItem(0, 50);
        }
    }

    @Override // com.lineage.server.model.L1Object, com.lineage.server.model.Instance.L1NpcInstance
    public void onPerceive(L1PcInstance perceivedFrom) {
        try {
            if (perceivedFrom.get_showId() == get_showId()) {
                perceivedFrom.addKnownObject(this);
                if (getCurrentHp() > 0) {
                    if (getHiddenStatus() == 1 || getHiddenStatus() == 3) {
                        perceivedFrom.sendPackets(new S_DoActionGFX(getId(), 11));
                    } else if (getHiddenStatus() == 2) {
                        perceivedFrom.sendPackets(new S_DoActionGFX(getId(), 44));
                    }
                    perceivedFrom.sendPackets(new S_NPCPack(this));
                    onNpcAI();
                    if (getBraveSpeed() == 1) {
                        perceivedFrom.sendPackets(new S_SkillBrave(getId(), 1, 600000));
                        return;
                    }
                    return;
                }
                perceivedFrom.sendPackets(new S_NPCPack(this));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void searchTarget() {
        L1PcInstance targetPlayer = searchTarget(this);
        if (targetPlayer != null) {
            this._hateList.add(targetPlayer, 0);
            this._target = targetPlayer;
            return;
        }
        this.ISASCAPE = false;
    }

    private L1PcInstance searchTarget(L1MonsterInstance npc) {
        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(npc).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                _log.error(e.getLocalizedMessage(), e);
            }
            if (pc.getCurrentHp() > 0 && !pc.isDead() && !pc.isGhost() && !pc.isGm() && npc.get_showId() == pc.get_showId()) {
                if (!((npc.getMapId() == 410 && pc.getTempCharGfx() == 4261) || (npc.getNpcTemplate().get_family() == NpcTable.ORC && pc.getClan() != null && pc.getClan().getCastleId() == 2))) {
                    L1PcInstance tgpc1 = npc.attackPc1(pc);
                    if (tgpc1 != null) {
                        return tgpc1;
                    }
                    L1PcInstance tgpc2 = npc.attackPc2(pc);
                    if (tgpc2 != null) {
                        return tgpc2;
                    }
                    if ((npc.getNpcTemplate().getKarma() >= 0 || pc.getKarmaLevel() < 1) && ((npc.getNpcTemplate().getKarma() <= 0 || pc.getKarmaLevel() > -1) && ((pc.getTempCharGfx() != 6034 || npc.getNpcTemplate().getKarma() >= 0) && !(pc.getTempCharGfx() == 6035 && (npc.getNpcTemplate().getKarma() > 0 || npc.getNpcTemplate().get_npcId() == 46070 || npc.getNpcTemplate().get_npcId() == 46072))))) {
                        L1PcInstance tgpc = npc.targetPlayer1000(pc);
                        if (tgpc != null) {
                            return tgpc;
                        }
                        if (!pc.isInvisble() || npc.getNpcTemplate().is_agrocoi()) {
                            if (pc.hasSkillEffect(67)) {
                                if (npc.getNpcTemplate().is_agrososc()) {
                                    return pc;
                                }
                            } else if (npc.getNpcTemplate().is_agro()) {
                                return pc;
                            }
                            if (npc.getNpcTemplate().is_agrogfxid1() >= 0 && pc.getGfxId() == npc.getNpcTemplate().is_agrogfxid1()) {
                                return pc;
                            }
                            if (npc.getNpcTemplate().is_agrogfxid2() >= 0 && pc.getGfxId() == npc.getNpcTemplate().is_agrogfxid2()) {
                                return pc;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private L1PcInstance attackPc2(L1PcInstance pc) {
        if (getNpcId() != 45600 || ((!pc.isCrown() || pc.getTempCharGfx() != pc.getClassId()) && !pc.isDarkelf())) {
            return null;
        }
        return pc;
    }

    private L1PcInstance attackPc1(L1PcInstance pc) {
        int mapId = getMapId();
        boolean isCheck = false;
        if (mapId == 88) {
            isCheck = true;
        }
        if (mapId == 98) {
            isCheck = true;
        }
        if (mapId == 92) {
            isCheck = true;
        }
        if (mapId == 91) {
            isCheck = true;
        }
        if (mapId == 95) {
            isCheck = true;
        }
        if (!isCheck || (pc.isInvisble() && !getNpcTemplate().is_agrocoi())) {
            return null;
        }
        return pc;
    }

    private L1PcInstance targetPlayer1000(L1PcInstance pc) {
        if (!ConfigOther.KILLRED || getNpcTemplate().is_agro() || getNpcTemplate().is_agrososc() || getNpcTemplate().is_agrogfxid1() >= 0 || getNpcTemplate().is_agrogfxid2() >= 0 || pc.getLawful() >= -1000) {
            return null;
        }
        return pc;
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void setLink(L1Character cha) {
        if (get_showId() == cha.get_showId() && cha != null && this._hateList.isEmpty()) {
            this._hateList.add(cha, 0);
            checkTarget();
        }
    }

    public L1MonsterInstance(L1Npc template) {
        super(template);
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onNpcAI() {
        if (!isAiRunning()) {
            if (!this._storeDroped) {
                new SetDrop().setDrop(this, getInventory());
                getInventory().shuffle();
                this._storeDroped = true;
            }
            setActived(false);
            startAI();
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onTalkAction(L1PcInstance pc) {
        setHeading(targetDirection(pc.getX(), pc.getY()));
        broadcastPacketAll(new S_ChangeHeading(this));
        set_stop_time(10);
        setRest(true);
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance pc) throws Exception {
        if (this.ATTACK != null) {
            this.ATTACK.attack(pc, this);
        }
        if (getCurrentHp() > 0 && !isDead()) {
            L1AttackMode attack = new L1AttackPc(pc, this);
            if (attack.calcHit()) {
                attack.calcDamage();
                attack.calcStaffOfMana();
                attack.addChaserAttack();
            }
            attack.action();
            attack.commit();
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void ReceiveManaDamage(L1Character attacker, int mpDamage) {
        if (mpDamage > 0 && !isDead()) {
            setHate(attacker, mpDamage);
            onNpcAI();
            if (attacker instanceof L1PcInstance) {
                serchLink((L1PcInstance) attacker, getNpcTemplate().get_family());
            }
            int newMp = getCurrentMp() - mpDamage;
            if (newMp < 0) {
                newMp = 0;
            }
            setCurrentMp(newMp);
        }
    }

    public void receiveDamage(L1Character attacker, double damage, int attr) {
        int resistFloor;
        if (getMr() >= _random.nextInt(300) + 1) {
            damage /= 2.01d;
        }
        int resist = 0;
        switch (attr) {
            case 1:
                resist = getEarth();
                break;
            case 2:
                resist = getFire();
                break;
            case 4:
                resist = getWater();
                break;
            case 8:
                resist = getWind();
                break;
        }
        int resistFloor2 = (int) (0.32d * ((double) Math.abs(resist)));
        if (resist >= 0) {
            resistFloor = resistFloor2 * 1;
        } else {
            resistFloor = resistFloor2 * -1;
        }
        double coefficient = (1.0d - (((double) resistFloor) / 32.0d)) + 0.09375d;
        if (coefficient > 0.0d) {
            damage *= coefficient;
        }
        receiveDamage(attacker, (int) damage);
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void receiveDamage(L1Character attacker, int damage) {
        this.ISASCAPE = false;
        if (getCurrentHp() <= 0 || isDead()) {
            if (!isDead()) {
                setDead(true);
                setStatus(8);
                GeneralThreadPool.get().execute(new Death(attacker));
            }
        } else if (getHiddenStatus() != 1 && getHiddenStatus() != 2) {
            if (damage >= 0) {
                if (attacker instanceof L1EffectInstance) {
                    attacker = ((L1EffectInstance) attacker).getMaster();
                    if (attacker != null) {
                        setHate(attacker, damage);
                    }
                } else if (attacker instanceof L1IllusoryInstance) {
                    attacker = ((L1IllusoryInstance) attacker).getMaster();
                    if (attacker != null) {
                        setHate(attacker, damage);
                    }
                } else if (attacker instanceof L1MonsterInstance) {
                    switch (getNpcTemplate().get_npcId()) {
                        case 91290:
                        case 91294:
                        case 91295:
                        case 91296:
                            setHate(attacker, damage);
                            damage = 0;
                            break;
                    }
                } else {
                    setHate(attacker, damage);
                }
            }
            if (damage > 0) {
                removeSkillEffect(66);
            }
            onNpcAI();
            L1PcInstance atkpc = null;
            if (attacker instanceof L1PcInstance) {
                atkpc = (L1PcInstance) attacker;
                if (damage > 0) {
                    atkpc.setPetTarget(this);
                    switch (getNpcTemplate().get_npcId()) {
                        case 45681:
                        case 45682:
                        case 45683:
                        case 45684:
                            recall(atkpc);
                            break;
                    }
                }
                serchLink(atkpc, getNpcTemplate().get_family());
            }
            int newHp = getCurrentHp() - damage;
            if (newHp <= 0 && !isDead()) {
                int transformId = getNpcTemplate().getTransformId();
                if (transformId == -1) {
                    setCurrentHpDirect(0);
                    setDead(true);
                    setStatus(8);
                    openDoorWhenNpcDied(this);
                    GeneralThreadPool.get().execute(new Death(attacker));
                } else {
                    transform(transformId);
                }
            }
            if (newHp > 0) {
                setCurrentHp(newHp);
                hide();
            }
            if (ConfigOther.HPBAR && atkpc != null) {
                broadcastPacketHP(atkpc);
            }
        }
    }

    private static void openDoorWhenNpcDied(L1NpcInstance npc) {
        int[] npcId = {46143, 46144, 46145, 46146, 46147, 46148, 46149, 46150, 46151, 46152};
        int[] doorId = {L1SkillId.SEXP20, L1SkillId.SEXP13, L1SkillId.SEXP15, L1SkillId.SEXP17, L1SkillId.REEXP20, 5006, 5007, 5008, 5009, 5010};
        for (int i = 0; i < npcId.length; i++) {
            if (npc.getNpcTemplate().get_npcId() == npcId[i]) {
                openDoorInCrystalCave(doorId[i]);
            }
        }
    }

    private static void openDoorInCrystalCave(int doorId) {
        for (L1Object object : World.get().getObject()) {
            if (object instanceof L1DoorInstance) {
                L1DoorInstance door = (L1DoorInstance) object;
                if (door.getDoorId() == doorId) {
                    door.open();
                }
            }
        }
    }

    private void recall(L1PcInstance pc) {
        if (getMapId() == pc.getMapId() && getLocation().getTileLineDistance(pc.getLocation()) > 4) {
            for (int count = 0; count < 10; count++) {
                L1Location newLoc = getLocation().randomLocation(3, 4, false);
                if (glanceCheck(newLoc.getX(), newLoc.getY())) {
                    L1Teleport.teleport(pc, newLoc.getX(), newLoc.getY(), getMapId(), 5, true);
                    return;
                }
            }
        }
    }

    @Override // com.lineage.server.model.L1Character
    public void setCurrentHp(int i) {
        int currentHp = Math.min(i, getMaxHp());
        if (getCurrentHp() != currentHp) {
            setCurrentHpDirect(currentHp);
        }
    }

    @Override // com.lineage.server.model.L1Character
    public void setCurrentMp(int i) {
        int currentMp = Math.min(i, (int) getMaxMp());
        if (getCurrentMp() != currentMp) {
            setCurrentMpDirect(currentMp);
        }
    }

    /* access modifiers changed from: package-private */
    public class Death implements Runnable {
        L1Character _lastAttacker;

        public Death(L1Character lastAttacker) {
            this._lastAttacker = lastAttacker;
        }

        public void run() {
            int deltime;
            L1MonsterInstance mob = L1MonsterInstance.this;
            if (this._lastAttacker instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) this._lastAttacker;
                pc.setKillCount(pc.getKillCount() + 1);
                pc.sendPackets(new S_OwnCharStatus(pc));
            }
            tark(mob);
            spawn(mob);
            mob.setDeathProcessing(true);
            mob.setCurrentHpDirect(0);
            mob.setDead(true);
            mob.setStatus(8);
            mob.broadcastPacketAll(new S_DoActionGFX(mob.getId(), 8));
            mob.getMap().setPassable(mob.getLocation(), true);
            mob.startChat(1);
            mob.distributeExpDropKarma(this._lastAttacker);
            mob.giveUbSeal();
            mob.setDeathProcessing(false);
            mob.setExp(0);
            mob.setKarma(0);
            mob.allTargetClear();
            switch (mob.getNpcId()) {
                case 92000:
                case 92001:
                    deltime = 60;
                    break;
                default:
                    deltime = ConfigAlt.NPC_DELETION_TIME;
                    break;
            }
            mob.startDeleteTimer(deltime);
        }

        private void spawn(L1MonsterInstance mob) {
            mob.getNpcId();
            mob.getMapId();
        }

        private void tark(L1MonsterInstance mob) {
            mob.getNpcId();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void distributeExpDropKarma(L1Character lastAttacker) {
        L1PcInstance pc;
        if (lastAttacker != null) {
            if (this.DEATH != null) {
                pc = this.DEATH.death(lastAttacker, this);
            } else {
                pc = CheckUtil.checkAtkPc(lastAttacker);
            }
            if (pc != null) {
                CalcExp.calcExp(pc, getId(), this._hateList.toTargetArrayList(), this._hateList.toHateArrayList(), getExp());
                int score = NpcScoreTable.get().get_score(getNpcId());
                if (score > 0 && !isResurrect()) {
                    pc.sendPackets(new S_ServerMessage("\\fX你得到了" + score + "積分"));
                    pc.get_other().add_score(score);
                }
                if (!(getSpawn() == null || getSpawn().get_nextSpawnTime() == null)) {
                    long newTime = getSpawn().get_spawnInterval() * 60 * 1000;
                    Calendar cals = Calendar.getInstance();
                    cals.setTimeInMillis(System.currentTimeMillis() + newTime);
                    getSpawn().get_nextSpawnTime().setTimeInMillis(cals.getTimeInMillis());
                    SpawnBossReading.get().upDateNextSpawnTime(getSpawn().getId(), cals);
                    switch (getSpawn().getMessage_boss()) {
                        case 1:
                            String nextTime = new SimpleDateFormat("MM-dd HH:mm").format(getSpawn().get_nextSpawnTime().getTime());
                            World.get().broadcastPacketToAll(new S_PacketBoxGree1(2, L1WilliamSystemMessage.ShowMessage(15) + "：【" + pc.getName() + "】" + L1WilliamSystemMessage.ShowMessage(16) + "【" + getName() + "】" + L1WilliamSystemMessage.ShowMessage(10) + " " + nextTime + "。"));
                            World.get().broadcastPacketToAll(new S_SystemMessage(L1WilliamSystemMessage.ShowMessage(15) + "：【" + pc.getName() + "】" + L1WilliamSystemMessage.ShowMessage(16) + "【" + getName() + "】" + L1WilliamSystemMessage.ShowMessage(10) + " " + nextTime + "。"));
                            break;
                    }
                }
                if (isDead()) {
                    distributeDrop();
                    giveKarma(pc);
                }
            }
        }
    }

    private void distributeDrop() {
        try {
            new DropShare().dropShare(this, this._dropHateList.toTargetArrayList(), this._dropHateList.toHateArrayList());
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void giveKarma(L1PcInstance pc) {
        int karma = getKarma();
        if (karma != 0) {
            int karmaSign = Integer.signum(karma);
            int pcKarmaLevelSign = Integer.signum(pc.getKarmaLevel());
            if (!(pcKarmaLevelSign == 0 || karmaSign == pcKarmaLevelSign)) {
                karma *= 5;
            }
            pc.addKarma((int) (((double) karma) * ConfigRate.RATE_KARMA));
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void giveUbSeal() {
        L1UltimateBattle ub;
        if (!(getUbSealCount() == 0 || (ub = UBTable.getInstance().getUb(getUbId())) == null)) {
            L1PcInstance[] membersArray = ub.getMembersArray();
            for (L1PcInstance pc : membersArray) {
                if (pc != null && !pc.isDead() && !pc.isGhost()) {
                    Reward_Ub.getItem(pc, ub.getUbId(), (long) getUbSealCount());
                }
            }
        }
    }

    public boolean is_storeDroped() {
        return this._storeDroped;
    }

    public void set_storeDroped(boolean flag) {
        this._storeDroped = flag;
    }

    public int getUbSealCount() {
        return this._ubSealCount;
    }

    public void setUbSealCount(int i) {
        this._ubSealCount = i;
    }

    public int getUbId() {
        return this._ubId;
    }

    public void setUbId(int i) {
        this._ubId = i;
    }

    private void hide() {
        switch (getNpcTemplate().get_npcId()) {
            case 45061:
            case 45161:
            case 45181:
            case 45455:
                if (getMaxHp() / 3 > getCurrentHp() && 1 > _random.nextInt(10)) {
                    allTargetClear();
                    setHiddenStatus(1);
                    broadcastPacketAll(new S_DoActionGFX(getId(), 11));
                    setStatus(13);
                    broadcastPacketAll(new S_NPCPack(this));
                    return;
                }
                return;
            case 45067:
            case 45090:
            case 45264:
            case 45321:
            case 45445:
            case 45452:
                if (getMaxHp() / 3 > getCurrentHp() && 1 > _random.nextInt(10)) {
                    allTargetClear();
                    setHiddenStatus(2);
                    broadcastPacketAll(new S_DoActionGFX(getId(), 44));
                    setStatus(4);
                    broadcastPacketAll(new S_NPCPack(this));
                    return;
                }
                return;
            case 45682:
                if (getMaxHp() / 3 > getCurrentHp() && 1 > _random.nextInt(50)) {
                    allTargetClear();
                    setHiddenStatus(1);
                    broadcastPacketAll(new S_DoActionGFX(getId(), 20));
                    setStatus(20);
                    broadcastPacketAll(new S_NPCPack(this));
                    return;
                }
                return;
            case 46107:
            case 46108:
                if (getMaxHp() / 4 > getCurrentHp() && 1 > _random.nextInt(10)) {
                    allTargetClear();
                    setHiddenStatus(1);
                    broadcastPacketAll(new S_DoActionGFX(getId(), 11));
                    setStatus(13);
                    broadcastPacketAll(new S_NPCPack(this));
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void initHide() {
        int npcid = getNpcTemplate().get_npcId();
        int rnd = _random.nextInt(3);
        switch (npcid) {
            case 45045:
            case 45126:
            case 45134:
            case 45281:
                if (1 > rnd) {
                    setHiddenStatus(1);
                    setStatus(4);
                    return;
                }
                return;
            case 45061:
            case 45161:
            case 45181:
            case 45455:
                if (1 > rnd) {
                    setHiddenStatus(1);
                    setStatus(13);
                    return;
                }
                return;
            case 45067:
            case 45090:
            case 45264:
            case 45321:
            case 45445:
            case 45452:
                setHiddenStatus(2);
                setStatus(4);
                return;
            case 45681:
                setHiddenStatus(2);
                setStatus(11);
                return;
            case 46107:
            case 46108:
                if (1 > rnd) {
                    setHiddenStatus(1);
                    setStatus(13);
                    return;
                }
                return;
            case 46125:
            case 46126:
            case 46127:
            case 46128:
                setHiddenStatus(3);
                setStatus(4);
                return;
            default:
                return;
        }
    }

    public void initHideForMinion(L1NpcInstance leader) {
        int npcid = getNpcTemplate().get_npcId();
        if (leader.getHiddenStatus() == 1) {
            switch (npcid) {
                case 45045:
                case 45126:
                case 45134:
                case 45281:
                    setHiddenStatus(1);
                    setStatus(4);
                    return;
                case 45061:
                case 45161:
                case 45181:
                case 45455:
                    setHiddenStatus(1);
                    setStatus(13);
                    return;
                case 46107:
                case 46108:
                    setHiddenStatus(1);
                    setStatus(13);
                    return;
                default:
                    return;
            }
        } else if (leader.getHiddenStatus() == 2) {
            switch (npcid) {
                case 45067:
                case 45090:
                case 45264:
                case 45321:
                case 45445:
                case 45452:
                    setHiddenStatus(2);
                    setStatus(4);
                    return;
                case 45681:
                    setHiddenStatus(2);
                    setStatus(11);
                    return;
                case 46125:
                case 46126:
                case 46127:
                case 46128:
                    setHiddenStatus(3);
                    setStatus(4);
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void transform(int transformId) {
        super.transform(transformId);
        getInventory().clearItems();
        new SetDrop().setDrop(this, getInventory());
        getInventory().shuffle();
    }

    public long getLastHprTime() {
        if (this._lasthprtime != 0) {
            return this._lasthprtime;
        }
        long currentTimeMillis = (System.currentTimeMillis() / 1000) - 5;
        this._lasthprtime = currentTimeMillis;
        return currentTimeMillis;
    }

    public void setLastHprTime(long time) {
        this._lasthprtime = time;
    }

    public long getLastMprTime() {
        if (this._lastmprtime != 0) {
            return this._lastmprtime;
        }
        long currentTimeMillis = (System.currentTimeMillis() / 1000) - 5;
        this._lastmprtime = currentTimeMillis;
        return currentTimeMillis;
    }

    public void setLastMprTime(long time) {
        this._lastmprtime = time;
    }
}
