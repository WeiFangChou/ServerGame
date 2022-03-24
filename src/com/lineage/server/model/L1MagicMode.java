package com.lineage.server.model;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import java.util.ConcurrentModificationException;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class L1MagicMode {
    protected static final int NPC_NPC = 4;
    protected static final int NPC_PC = 3;
    protected static final int PC_NPC = 2;
    protected static final int PC_PC = 1;
    private static final Log _log = LogFactory.getLog(L1MagicMode.class);
    protected static final Random _random = new Random();
    protected int _calcType;
    protected int _leverage = 10;
    protected L1NpcInstance _npc = null;
    protected L1PcInstance _pc = null;
    protected L1NpcInstance _targetNpc = null;
    protected L1PcInstance _targetPc = null;

    public abstract int calcHealing(int i);

    public abstract int calcMagicDamage(int i) throws Exception;

    public abstract boolean calcProbabilityMagic(int i);

    public abstract void commit(int i, int i2) throws Exception;

    protected static boolean dmg0(L1Character character) {
        if (character == null) {
            return false;
        }
        try {
            if (character.getSkillisEmpty() || character.getSkillEffect().size() <= 0) {
                return false;
            }
            for (Integer key : character.getSkillEffect()) {
                if (L1AttackList.SKM0.get(key) != null) {
                    return true;
                }
            }
            return false;
        } catch (ConcurrentModificationException e) {
            return false;
        } catch (Exception e2) {
            _log.error(e2.getLocalizedMessage(), e2);
            return false;
        }
    }

    protected static double getDamageUpByClan(L1PcInstance pc) {
        if (pc == null) {
            return 0.0d;
        }
        try {
            double dmg = 0.0d + ((double) pc.get_magic_modifier_dmg());
            L1Clan clan = pc.getClan();
            if (clan == null) {
                return dmg;
            }
            if (clan.isClanskill() && pc.get_other().get_clanskill() == 4) {
                dmg += 0.25d * ((double) clan.getOnlineClanMemberSize());
            }
            return dmg;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return 0.0d;
        }
    }

    protected static double getDamageReductionByClan(L1PcInstance targetPc) {
        if (targetPc == null) {
            return 0.0d;
        }
        try {
            double dmg = 0.0d + ((double) targetPc.get_magic_reduction_dmg());
            L1Clan clan = targetPc.getClan();
            if (clan == null) {
                return 0.0d;
            }
            if (clan.isClanskill() && targetPc.get_other().get_clanskill() == 8) {
                dmg += 0.25d * ((double) clan.getOnlineClanMemberSize());
            }
            return dmg;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return 0.0d;
        }
    }

    public void setLeverage(int i) {
        this._leverage = i;
    }

    /* access modifiers changed from: protected */
    public int getLeverage() {
        return this._leverage;
    }

    /* access modifiers changed from: protected */
    public int getTargetSp() {
        switch (this._calcType) {
            case 1:
            case 2:
                int sp = this._pc.getSp() - this._pc.getTrueSp();
                switch (this._pc.guardianEncounter()) {
                    case 3:
                        sp++;
                        break;
                    case 4:
                        sp += 2;
                        break;
                    case 5:
                        sp += 3;
                        break;
                }
                if (this._pc.hasSkillEffect(L1SkillId.DRAGON4)) {
                    sp++;
                }
                if (this._pc.hasSkillEffect(L1SkillId.DRAGON5)) {
                    sp++;
                }
                if (this._pc.hasSkillEffect(L1SkillId.DRAGON7)) {
                    return sp + 1;
                }
                return sp;
            case 3:
            case 4:
                return this._npc.getSp() - this._npc.getTrueSp();
            default:
                return 0;
        }
    }

    /* access modifiers changed from: protected */
    public int getTargetMr() {
        int mr = 0;
        switch (this._calcType) {
            case 1:
            case 3:
                if (this._targetPc != null) {
                    mr = this._targetPc.getMr();
                    switch (this._targetPc.guardianEncounter()) {
                        case 0:
                            mr += 3;
                            break;
                        case 1:
                            mr += 6;
                            break;
                        case 2:
                            mr += 9;
                            break;
                    }
                } else {
                    return 0;
                }
            case 2:
            case 4:
                if (this._targetNpc != null) {
                    mr = this._targetNpc.getMr();
                    break;
                } else {
                    return 0;
                }
        }
        return mr;
    }

    /* access modifiers changed from: protected */
    public boolean calcEvasion() {
        int ev;
        if (this._targetPc == null || (ev = this._targetPc.get_evasion()) == 0 || ev < _random.nextInt(L1SkillId.STATUS_BRAVE) + 1) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public double calcAttrResistance(int attr) {
        int resistFloor;
        int resist = 0;
        switch (this._calcType) {
            case 1:
            case 3:
                if (this._targetPc != null) {
                    switch (attr) {
                        case 1:
                            resist = this._targetPc.getEarth();
                            break;
                        case 2:
                            resist = this._targetPc.getFire();
                            break;
                        case 4:
                            resist = this._targetPc.getWater();
                            break;
                        case 8:
                            resist = this._targetPc.getWind();
                            break;
                    }
                } else {
                    return 0.0d;
                }
            case 2:
            case 4:
                if (this._targetNpc != null) {
                    switch (attr) {
                        case 1:
                            resist = this._targetNpc.getEarth();
                            break;
                        case 2:
                            resist = this._targetNpc.getFire();
                            break;
                        case 4:
                            resist = this._targetNpc.getWater();
                            break;
                        case 8:
                            resist = this._targetNpc.getWind();
                            break;
                    }
                } else {
                    return 0.0d;
                }
        }
        int resistFloor2 = (int) (0.32d * ((double) Math.abs(resist)));
        if (resist >= 0) {
            resistFloor = resistFloor2 * 1;
        } else {
            resistFloor = resistFloor2 * -1;
        }
        return ((double) resistFloor) / 32.0d;
    }
}
