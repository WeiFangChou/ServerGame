package com.lineage.server.model;

import com.lineage.server.datatables.MobSkillTable;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.skill.L1SkillDelayforMob;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_PacketBoxWindShackle;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_ShowPolyList;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1MobSkill;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1MobSkillUse {
    private static final Log _log = LogFactory.getLog(L1MobSkillUse.class);
    private static final Random _rnd = new Random();
    private L1NpcInstance _attacker = null;
    private L1MobSkill _mobSkillTemplate = null;
    private int _skillSize;
    private int[] _skillUseCount;
    private int _sleepTime = 0;
    private L1Character _target = null;

    public L1MobSkillUse(L1NpcInstance npc) {
        try {
            this._sleepTime = 0;
            this._mobSkillTemplate = MobSkillTable.getInstance().getTemplate(npc.getNpcTemplate().get_npcId());
            if (this._mobSkillTemplate != null) {
                this._attacker = npc;
                this._skillSize = getMobSkillTemplate().getSkillSize();
                this._skillUseCount = new int[this._skillSize];
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private int getSkillUseCount(int idx) {
        return this._skillUseCount[idx];
    }

    private void skillUseCountUp(int idx) {
        int[] iArr = this._skillUseCount;
        iArr[idx] = iArr[idx] + 1;
    }

    public void resetAllSkillUseCount() {
        if (this._mobSkillTemplate != null) {
            for (int i = 0; i < this._skillSize; i++) {
                this._skillUseCount[i] = 0;
            }
        }
    }

    public int getSleepTime() {
        return this._sleepTime;
    }

    public void setSleepTime(int i) {
        this._sleepTime = i;
    }

    public L1MobSkill getMobSkillTemplate() {
        return this._mobSkillTemplate;
    }

    public boolean isSkillTrigger(L1Character tg) {
        if (this._mobSkillTemplate == null) {
            return false;
        }
        this._target = tg;
        if (getMobSkillTemplate().getType(0) == 0) {
            return false;
        }
        for (int i = 0; i < this._skillSize; i++) {
            if (isSkillUseble(i)) {
                return true;
            }
        }
        return false;
    }

    public boolean skillUse(L1Character tg) throws Exception {
        if (this._mobSkillTemplate == null) {
            return false;
        }
        this._target = tg;
        if (getMobSkillTemplate().getType(0) == 0) {
            return false;
        }
        int[] skills = null;
        int skillSizeCounter = 0;
        if (this._skillSize >= 0) {
            skills = new int[this._skillSize];
        }
        for (int i = 0; i < this._skillSize; i++) {
            if (isSkillUseble(i)) {
                skills[skillSizeCounter] = i;
                skillSizeCounter++;
            }
        }
        if (skillSizeCounter == 0 || !useSkill(skills[_rnd.nextInt(skillSizeCounter)])) {
            return false;
        }
        return true;
    }

    private void setDelay(int idx, int skillType) {
        int reusedelay = this._mobSkillTemplate.getReuseDelay(idx);
        if (reusedelay > 0) {
            L1SkillDelayforMob.onSkillUse(this._attacker, reusedelay, idx, skillType);
        }
    }

    private boolean useSkill(int idx) throws Exception {
        int changeType = getMobSkillTemplate().getChangeTarget(idx);
        if (changeType > 0) {
            this._target = changeTarget(changeType, idx);
        }
        int skillType = getMobSkillTemplate().getType(idx);
        if (this._mobSkillTemplate.isSkillDelayType(skillType)) {
            return false;
        }
        if (this._mobSkillTemplate.isSkillDelayIdx(idx)) {
            return false;
        }
        int rnd = getMobSkillTemplate().getTriggerRandom(idx);
        if (rnd <= 0 || rnd > _rnd.nextInt(100) + 1) {
            return false;
        }
        switch (skillType) {
            case 1:
                if (!physicalAttack(idx)) {
                    return false;
                }
                skillUseCountUp(idx);
                setDelay(idx, 0);
                return true;
            case 2:
                if (!magicAttack(idx)) {
                    return false;
                }
                skillUseCountUp(idx);
                setDelay(idx, 0);
                return true;
            case 3:
                if (!summon(idx)) {
                    return false;
                }
                skillUseCountUp(idx);
                setDelay(idx, skillType);
                return true;
            case 4:
                if (!poly(idx)) {
                    return false;
                }
                skillUseCountUp(idx);
                setDelay(idx, skillType);
                return true;
            case 5:
                if (!areashock_stun(idx)) {
                    return false;
                }
                skillUseCountUp(idx);
                setDelay(idx, 0);
                return true;
            case 6:
                if (!areacancellation(idx)) {
                    return false;
                }
                skillUseCountUp(idx);
                setDelay(idx, 0);
                return true;
            case 7:
                if (!weapon_break(idx)) {
                    return false;
                }
                skillUseCountUp(idx);
                setDelay(idx, 0);
                return true;
            case 8:
                if (!potionturntodmg(idx)) {
                    return false;
                }
                skillUseCountUp(idx);
                setDelay(idx, 0);
                return true;
            case 9:
                if (!pollutewaterwave(idx)) {
                    return false;
                }
                skillUseCountUp(idx);
                setDelay(idx, 0);
                return true;
            case 10:
                if (!healturntodmg(idx)) {
                    return false;
                }
                skillUseCountUp(idx);
                setDelay(idx, 0);
                return true;
            case 11:
                if (!areasilence(idx)) {
                    return false;
                }
                skillUseCountUp(idx);
                setDelay(idx, 0);
                return true;
            case 12:
                if (!areadecaypotion(idx)) {
                    return false;
                }
                skillUseCountUp(idx);
                setDelay(idx, 0);
                return true;
            case 13:
                if (!areawindshackle(idx)) {
                    return false;
                }
                skillUseCountUp(idx);
                setDelay(idx, 0);
                return true;
            case 14:
                if (!areadebuff(idx)) {
                    return false;
                }
                skillUseCountUp(idx);
                setDelay(idx, 0);
                return true;
            case 15:
                if (!area_poison(idx)) {
                    return false;
                }
                skillUseCountUp(idx);
                setDelay(idx, 0);
                return true;
            case 16:
                if (!SpawnEffect(idx)) {
                    return false;
                }
                skillUseCountUp(idx);
                setDelay(idx, 0);
                return true;
            default:
                return false;
        }
    }

    private boolean SpawnEffect(int idx) {
        if (this._attacker.getMapId() == 93) {
            return false;
        }
        int actId = getMobSkillTemplate().getActid(idx);
        int actionid = 1;
        if (actId > 0) {
            actionid = actId;
        }
        int summonId = getMobSkillTemplate().getSummon(idx);
        if (summonId == 0) {
            return false;
        }
        int time = getMobSkillTemplate().getLeverage(idx);
        if (time == 0) {
            time = 10;
        }
        L1SpawnUtil.spawnEffect(summonId, time, this._attacker.getX(), this._attacker.getY(), this._attacker.getMapId(), this._attacker, 0);
        this._attacker.broadcastPacketAll(new S_DoActionGFX(this._attacker.getId(), actionid));
        this._sleepTime = this._attacker.getNpcTemplate().getSubMagicSpeed();
        return true;
    }

    private boolean area_poison(int idx) {
        if (this._attacker.getMapId() == 93) {
            return false;
        }
        int actId = getMobSkillTemplate().getActid(idx);
        int actionid = 1;
        if (actId > 0) {
            actionid = actId;
        }
        int summonId = getMobSkillTemplate().getSummon(idx);
        if (summonId == 0) {
            return false;
        }
        int time = getMobSkillTemplate().getLeverage(idx);
        if (time == 0) {
            time = 10;
        }
        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(this._attacker).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            if (!pc.isGmInvis()) {
                L1SpawnUtil.spawnEffect(summonId, time, pc.getX(), pc.getY(), this._attacker.getMapId(), this._attacker, 0);
            }
        }
        this._attacker.broadcastPacketAll(new S_DoActionGFX(this._attacker.getId(), actionid));
        this._sleepTime = this._attacker.getNpcTemplate().getSubMagicSpeed();
        return true;
    }

    private boolean areadebuff(int idx) {
        if (this._attacker.getMapId() == 93) {
            return false;
        }
        int skillid = getMobSkillTemplate().getSkillId(idx);
        int actId = getMobSkillTemplate().getActid(idx);
        int gfxId = getMobSkillTemplate().getGfxid(idx);
        int time = getMobSkillTemplate().getLeverage(idx);
        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(this._attacker).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            if (skillid != 71 || !pc.hasSkillEffect(L1SkillId.ADLV80_2_1)) {
                L1SkillUse skillUse = new L1SkillUse();
                boolean canUseSkill = false;
                if (skillid > 0) {
                    canUseSkill = skillUse.checkUseSkill(null, skillid, pc.getId(), pc.getX(), pc.getY(), time, 4, this._attacker, actId, gfxId);
                }
                if (canUseSkill && !pc.hasSkillEffect(skillid)) {
                    skillUse.handleCommands(null, skillid, pc.getId(), pc.getX(), pc.getY(), time, 4, this._attacker);
                }
            }
        }
        if (!SkillsTable.get().getTemplate(skillid).getTarget().equals("attack") || skillid == 18) {
            this._sleepTime = this._attacker.getNpcTemplate().getSubMagicSpeed();
        } else {
            this._sleepTime = this._attacker.getNpcTemplate().getAtkMagicSpeed();
        }
        return true;
    }

    private boolean areawindshackle(int idx) {
        if (this._attacker.getMapId() == 93) {
            return false;
        }
        int actId = getMobSkillTemplate().getActid(idx);
        int actionid = 19;
        if (actId > 0) {
            actionid = actId;
        }
        int gfxId = getMobSkillTemplate().getGfxid(idx);
        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(this._attacker).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            if (!pc.hasSkillEffect(167)) {
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), gfxId));
                pc.sendPackets(new S_PacketBoxWindShackle(pc.getId(), 16));
                pc.setSkillEffect(167, 16000);
            }
        }
        this._attacker.broadcastPacketAll(new S_DoActionGFX(this._attacker.getId(), actionid));
        this._sleepTime = this._attacker.getNpcTemplate().getSubMagicSpeed();
        return true;
    }

    private boolean areadecaypotion(int idx) {
        if (this._attacker.getMapId() == 93) {
            return false;
        }
        int actId = getMobSkillTemplate().getActid(idx);
        int actionid = 19;
        if (actId > 0) {
            actionid = actId;
        }
        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(this._attacker).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            if (!pc.hasSkillEffect(71)) {
                new L1SkillUse().handleCommands(pc, 71, pc.getId(), pc.getX(), pc.getY(), 0, 4);
            }
        }
        this._attacker.broadcastPacketAll(new S_DoActionGFX(this._attacker.getId(), actionid));
        this._sleepTime = this._attacker.getNpcTemplate().getSubMagicSpeed();
        return true;
    }

    private boolean areasilence(int idx) {
        if (this._attacker.getMapId() == 93) {
            return false;
        }
        int actId = getMobSkillTemplate().getActid(idx);
        int actionid = 19;
        if (actId > 0) {
            actionid = actId;
        }
        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(this._attacker).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            if (!pc.hasSkillEffect(64) && !pc.hasSkillEffect(161)) {
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 10708));
                pc.setSkillEffect(64, 16000);
            }
        }
        this._attacker.broadcastPacketAll(new S_DoActionGFX(this._attacker.getId(), actionid));
        this._sleepTime = this._attacker.getNpcTemplate().getSubMagicSpeed();
        return true;
    }

    private boolean healturntodmg(int idx) {
        if (this._attacker.getMapId() == 93) {
            return false;
        }
        int actId = getMobSkillTemplate().getActid(idx);
        int actionid = 19;
        if (actId > 0) {
            actionid = actId;
        }
        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(this._attacker).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            if (!pc.hasSkillEffect(L1SkillId.ADLV80_2_1) && !pc.hasSkillEffect(L1SkillId.ADLV80_2_2) && !pc.hasSkillEffect(L1SkillId.ADLV80_2_3)) {
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 7780));
                pc.setSkillEffect(L1SkillId.ADLV80_2_3, 12000);
            }
        }
        this._attacker.broadcastPacketAll(new S_DoActionGFX(this._attacker.getId(), actionid));
        this._sleepTime = this._attacker.getNpcTemplate().getSubMagicSpeed();
        return true;
    }

    private boolean pollutewaterwave(int idx) {
        if (this._attacker.getMapId() == 93) {
            return false;
        }
        int actId = getMobSkillTemplate().getActid(idx);
        int actionid = 19;
        if (actId > 0) {
            actionid = actId;
        }
        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(this._attacker).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            if (!pc.hasSkillEffect(L1SkillId.ADLV80_2_1) && !pc.hasSkillEffect(L1SkillId.ADLV80_2_2) && !pc.hasSkillEffect(L1SkillId.ADLV80_2_3)) {
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 7782));
                pc.setSkillEffect(L1SkillId.ADLV80_2_2, 12000);
            }
        }
        this._attacker.broadcastPacketAll(new S_DoActionGFX(this._attacker.getId(), actionid));
        this._sleepTime = this._attacker.getNpcTemplate().getSubMagicSpeed();
        return true;
    }

    private boolean potionturntodmg(int idx) {
        if (this._attacker.getMapId() == 93) {
            return false;
        }
        int actId = getMobSkillTemplate().getActid(idx);
        int actionid = 19;
        if (actId > 0) {
            actionid = actId;
        }
        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(this._attacker).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            if (!pc.hasSkillEffect(L1SkillId.ADLV80_2_1) && !pc.hasSkillEffect(L1SkillId.ADLV80_2_2) && !pc.hasSkillEffect(L1SkillId.ADLV80_2_3) && !pc.hasSkillEffect(71)) {
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 7781));
                pc.setSkillEffect(L1SkillId.ADLV80_2_1, 12000);
            }
        }
        this._attacker.broadcastPacketAll(new S_DoActionGFX(this._attacker.getId(), actionid));
        this._sleepTime = this._attacker.getNpcTemplate().getSubMagicSpeed();
        return true;
    }

    private boolean weapon_break(int idx) {
        if (this._attacker.getMapId() == 93) {
            return false;
        }
        int actId = getMobSkillTemplate().getActid(idx);
        int actionid = 19;
        if (actId > 0) {
            actionid = actId;
        }
        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(this._attacker).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            L1ItemInstance weapon = pc.getWeapon();
            if (weapon != null) {
                int weaponDamage = _rnd.nextInt(this._attacker.getInt() / 3) + 1;
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), L1SkillId.STORM_WALK));
                pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()));
                pc.getInventory().receiveDamage(weapon, weaponDamage);
            }
        }
        this._attacker.broadcastPacketAll(new S_DoActionGFX(this._attacker.getId(), actionid));
        this._sleepTime = this._attacker.getNpcTemplate().getSubMagicSpeed();
        return true;
    }

    private boolean areacancellation(int idx) {
        if (this._attacker.getMapId() == 93) {
            return false;
        }
        int actId = getMobSkillTemplate().getActid(idx);
        int actionid = 19;
        if (actId > 0) {
            actionid = actId;
        }
        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(this._attacker).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            new L1SkillUse().handleCommands(pc, 44, pc.getId(), pc.getX(), pc.getY(), 0, 4);
        }
        this._attacker.broadcastPacketAll(new S_DoActionGFX(this._attacker.getId(), actionid));
        this._sleepTime = this._attacker.getNpcTemplate().getSubMagicSpeed();
        return true;
    }

    private boolean areashock_stun(int idx) {
        if (this._attacker.getMapId() == 93) {
            return false;
        }
        int actId = getMobSkillTemplate().getActid(idx);
        int actionid = 1;
        if (actId > 0) {
            actionid = actId;
        }
        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(this._attacker).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            int shock = new Random().nextInt(4) + 2;
            if (!pc.isGmInvis() && !pc.hasSkillEffect(87)) {
                pc.setSkillEffect(87, shock * L1SkillId.STATUS_BRAVE);
                pc.sendPackets(new S_Paralysis(5, true));
                L1SpawnUtil.spawnEffect(81162, shock, pc.getX(), pc.getY(), this._attacker.getMapId(), this._attacker, 0);
            }
        }
        this._attacker.broadcastPacketAll(new S_DoActionGFX(this._attacker.getId(), actionid));
        this._sleepTime = this._attacker.getNpcTemplate().getSubMagicSpeed();
        return true;
    }

    private boolean summon(int idx) {
        if (this._attacker.getMapId() == 93) {
            return false;
        }
        int actId = getMobSkillTemplate().getActid(idx);
        int actionid = 19;
        if (actId > 0) {
            actionid = actId;
        }
        int summonId = getMobSkillTemplate().getSummon(idx);
        int min = getMobSkillTemplate().getSummonMin(idx);
        int max = getMobSkillTemplate().getSummonMax(idx);
        if (summonId == 0) {
            return false;
        }
        new L1MobSkillUseSpawn().mobspawn(this._attacker, this._target, summonId, _rnd.nextInt((max - min) + 1) + min);
        this._attacker.broadcastPacketAll(new S_SkillSound(this._attacker.getId(), 761));
        this._attacker.broadcastPacketAll(new S_DoActionGFX(this._attacker.getId(), actionid));
        this._sleepTime = this._attacker.getNpcTemplate().getSubMagicSpeed();
        return true;
    }

    private boolean poly(int idx) {
        if (this._attacker.getMapId() == 93) {
            return false;
        }
        int polyId = getMobSkillTemplate().getPolyId(idx);
        int actId = getMobSkillTemplate().getActid(idx);
        int actionid = 19;
        if (actId > 0) {
            actionid = actId;
        }
        boolean usePoly = false;
        if (polyId == 0) {
            return false;
        }
        Iterator<L1PcInstance> localIterator = World.get().getVisiblePlayer(this._attacker).iterator();
        while (localIterator.hasNext()) {
            L1PcInstance pc = localIterator.next();
            if (!pc.isDead() && !pc.isGhost() && !pc.isGmInvis() && this._attacker.glanceCheck(pc.getX(), pc.getY())) {
                if (pc.getInventory().checkEquipped(20281) || pc.getInventory().checkEquipped(120281)) {
                    pc.sendPackets(new S_ShowPolyList(pc.getId()));
                    if (!pc.isShapeChange()) {
                        pc.setSummonMonster(false);
                        pc.setShapeChange(true);
                    }
                    pc.sendPackets(new S_ServerMessage(966));
                } else {
                    L1PolyMorph.doPoly(pc, polyId, 1800, 4);
                }
                usePoly = true;
            }
        }
        if (!usePoly) {
            return usePoly;
        }
        Iterator<L1PcInstance> localIterator2 = World.get().getVisiblePlayer(this._attacker).iterator();
        if (localIterator2.hasNext()) {
            L1PcInstance pc2 = localIterator2.next();
            pc2.sendPacketsAll(new S_SkillSound(pc2.getId(), 230));
        }
        this._attacker.broadcastPacketAll(new S_DoActionGFX(this._attacker.getId(), actionid));
        this._sleepTime = this._attacker.getNpcTemplate().getSubMagicSpeed();
        return usePoly;
    }

    private boolean magicAttack(int idx) {
        L1SkillUse skillUse = new L1SkillUse();
        int skillid = getMobSkillTemplate().getSkillId(idx);
        int actId = getMobSkillTemplate().getActid(idx);
        int gfxId = getMobSkillTemplate().getGfxid(idx);
        boolean canUseSkill = false;
        if (skillid > 0) {
            canUseSkill = skillUse.checkUseSkill(null, skillid, this._target.getId(), this._target.getX(), this._target.getY(), 0, 0, this._attacker, actId, gfxId);
        }
        if (!canUseSkill) {
            return false;
        }
        if (getMobSkillTemplate().getLeverage(idx) > 0) {
            skillUse.setLeverage(getMobSkillTemplate().getLeverage(idx));
        }
        skillUse.handleCommands(null, skillid, this._target.getId(), this._target.getX(), this._target.getX(), 0, 0, this._attacker);
        if (!SkillsTable.get().getTemplate(skillid).getTarget().equals("attack") || skillid == 18) {
            this._sleepTime = this._attacker.getNpcTemplate().getSubMagicSpeed();
        } else {
            this._sleepTime = this._attacker.getNpcTemplate().getAtkMagicSpeed();
        }
        return true;
    }

    private boolean physicalAttack(int idx) throws Exception {
        Map<Integer, Integer> targetList = new HashMap<>();
        int areaWidth = getMobSkillTemplate().getAreaWidth(idx);
        int areaHeight = getMobSkillTemplate().getAreaHeight(idx);
        int range = getMobSkillTemplate().getRange(idx);
        int actId = getMobSkillTemplate().getActid(idx);
        int gfxId = getMobSkillTemplate().getGfxid(idx);
        if (this._attacker.getLocation().getTileLineDistance(this._target.getLocation()) > range || !this._attacker.glanceCheck(this._target.getX(), this._target.getY())) {
            return false;
        }
        this._attacker.setHeading(this._attacker.targetDirection(this._target.getX(), this._target.getY()));
        if (areaHeight > 0) {
            Iterator<L1Object> it = World.get().getVisibleBoxObjects(this._attacker, this._attacker.getHeading(), areaWidth, areaHeight).iterator();
            while (it.hasNext()) {
                L1Object obj = it.next();
                if (obj instanceof L1Character) {
                    L1Character cha = (L1Character) obj;
                    if (!cha.isDead() && ((!(cha instanceof L1PcInstance) || !((L1PcInstance) cha).isGhost()) && this._attacker.glanceCheck(cha.getX(), cha.getY()))) {
                        if ((this._target instanceof L1PcInstance) || (this._target instanceof L1SummonInstance) || (this._target instanceof L1PetInstance)) {
                            if (!(!(obj instanceof L1PcInstance) || ((L1PcInstance) obj).isGhost() || ((L1PcInstance) obj).isGmInvis()) || (obj instanceof L1SummonInstance) || (obj instanceof L1PetInstance)) {
                                targetList.put(Integer.valueOf(obj.getId()), 0);
                            }
                        } else if (obj instanceof L1MonsterInstance) {
                            targetList.put(Integer.valueOf(obj.getId()), 0);
                        }
                    }
                }
            }
        } else {
            targetList.put(Integer.valueOf(this._target.getId()), 0);
        }
        if (targetList.size() == 0) {
            return false;
        }
        for (Integer num : targetList.keySet()) {
            int targetId = num.intValue();
            L1AttackMode attack = new L1AttackNpc(this._attacker, (L1Character) World.get().findObject(targetId));
            if (attack.calcHit()) {
                if (getMobSkillTemplate().getLeverage(idx) > 0) {
                    attack.setLeverage(getMobSkillTemplate().getLeverage(idx));
                }
                attack.calcDamage();
            }
            if (actId > 0) {
                attack.setActId(actId);
            }
            if (targetId == this._target.getId()) {
                if (gfxId > 0) {
                    this._attacker.broadcastPacketAll(new S_SkillSound(this._attacker.getId(), gfxId));
                }
                attack.action();
            }
            attack.commit();
        }
        this._sleepTime = this._attacker.getAtkspeed();
        return true;
    }

    private boolean isSkillUseble(int skillIdx) {
        L1NpcInstance companionNpc;
        boolean useble = false;
        if (getMobSkillTemplate().getTriggerHp(skillIdx) > 0) {
            if ((this._attacker.getCurrentHp() * 100) / this._attacker.getMaxHp() > getMobSkillTemplate().getTriggerHp(skillIdx)) {
                return false;
            }
            useble = true;
        }
        if (getMobSkillTemplate().getTriggerCompanionHp(skillIdx) > 0 && (companionNpc = searchMinCompanionHp()) != null) {
            if ((companionNpc.getCurrentHp() * 100) / companionNpc.getMaxHp() > getMobSkillTemplate().getTriggerCompanionHp(skillIdx)) {
                return false;
            }
            useble = true;
            this._target = companionNpc;
        }
        if (getMobSkillTemplate().getTriggerRange(skillIdx) != 0) {
            if (!getMobSkillTemplate().isTriggerDistance(skillIdx, this._attacker.getLocation().getTileLineDistance(this._target.getLocation()))) {
                return false;
            }
            useble = true;
        }
        if (getMobSkillTemplate().getTriggerCount(skillIdx) > 0) {
            if (getSkillUseCount(skillIdx) >= getMobSkillTemplate().getTriggerCount(skillIdx)) {
                return false;
            }
            useble = true;
        }
        return useble;
    }

    private L1NpcInstance searchMinCompanionHp() {
        L1NpcInstance minHpNpc = null;
        int family = this._attacker.getNpcTemplate().get_family();
        Iterator<L1Object> it = World.get().getVisibleObjects(this._attacker).iterator();
        while (it.hasNext()) {
            L1Object object = it.next();
            if (object instanceof L1NpcInstance) {
                L1NpcInstance npc = (L1NpcInstance) object;
                if (npc.getNpcTemplate().get_family() == family) {
                    minHpNpc = npc;
                }
            }
        }
        return minHpNpc;
    }

    private L1Character changeTarget(int type, int idx) {
        switch (type) {
            case 2:
                return this._attacker;
            case 3:
                List<L1Character> targetList = new ArrayList<>();
                Iterator<L1Object> it = World.get().getVisibleObjects(this._attacker).iterator();
                while (it.hasNext()) {
                    L1Object obj = it.next();
                    if ((obj instanceof L1PcInstance) || (obj instanceof L1PetInstance) || (obj instanceof L1SummonInstance) || (this._attacker.getMapId() == 93 && (obj instanceof L1MonsterInstance))) {
                        L1Character cha = (L1Character) obj;
                        if (getMobSkillTemplate().isTriggerDistance(idx, this._attacker.getLocation().getTileLineDistance(cha.getLocation())) && this._attacker.glanceCheck(cha.getX(), cha.getY())) {
                            if ((this._attacker.getMapId() == 93 || this._attacker.getHateList().containsKey(cha)) && !cha.isDead()) {
                                if (!(cha instanceof L1PcInstance) || (!((L1PcInstance) cha).isGhost() && !((L1PcInstance) cha).isGmInvis())) {
                                    targetList.add((L1Character) obj);
                                }
                            }
                        }
                    }
                }
                if (targetList.size() == 0) {
                    return this._target;
                }
                return targetList.get(_rnd.nextInt(targetList.size()));
            default:
                return this._target;
        }
    }
}
