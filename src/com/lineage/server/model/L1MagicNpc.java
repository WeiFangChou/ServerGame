package com.lineage.server.model;

import com.lineage.config.ConfigAlt;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import java.util.ConcurrentModificationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1MagicNpc extends L1MagicMode {
    private static final Log _log = LogFactory.getLog(L1MagicNpc.class);

    public L1MagicNpc(L1NpcInstance attacker, L1Character target) {
        if (attacker != null) {
            if (target instanceof L1PcInstance) {
                this._calcType = 3;
                this._npc = attacker;
                this._targetPc = (L1PcInstance) target;
                return;
            }
            this._calcType = 4;
            this._npc = attacker;
            this._targetNpc = (L1NpcInstance) target;
        }
    }

    private int getMagicLevel() {
        return this._npc.getMagicLevel();
    }

    private int getMagicBonus() {
        return this._npc.getMagicBonus();
    }

    private int getLawful() {
        return this._npc.getLawful();
    }

    @Override // com.lineage.server.model.L1MagicMode
    public boolean calcProbabilityMagic(int skillId) {
        boolean isSuccess;
        if (skillId == 44) {
            return true;
        }
        switch (this._calcType) {
            case 3:
                if (!(!this._targetPc.hasSkillEffect(157) || skillId == 27 || skillId == 44)) {
                    return false;
                }
            case 4:
                if (!(!this._targetNpc.hasSkillEffect(157) || skillId == 27 || skillId == 44)) {
                    return false;
                }
        }
        int probability = calcProbability(skillId);
        if (Math.min(probability, 90) >= _random.nextInt(100) + 1) {
            isSuccess = true;
        } else {
            isSuccess = false;
        }
        if (calcEvasion()) {
            return false;
        }
        return isSuccess;
    }

    private int calcProbability(int skillId) {
        int probability;
        L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
        int attackLevel = this._npc.getLevel();
        int defenseLevel = 0;
        int probability2 = 0;
        switch (this._calcType) {
            case 3:
                defenseLevel = this._targetPc.getLevel();
                break;
            case 4:
                defenseLevel = this._targetNpc.getLevel();
                if (skillId == 145 && (this._targetNpc instanceof L1SummonInstance)) {
                    defenseLevel = ((L1SummonInstance) this._targetNpc).getMaster().getLevel();
                    break;
                }
        }
        switch (skillId) {
            case L1SkillId.SHOCK_STUN /*{ENCODED_INT: 87}*/:
                if (attackLevel >= defenseLevel) {
                    if (attackLevel == defenseLevel) {
                        probability = 40;
                        break;
                    } else {
                        probability = 60;
                        break;
                    }
                } else {
                    probability = 22;
                    break;
                }
            case L1SkillId.COUNTER_BARRIER /*{ENCODED_INT: 91}*/:
                probability = (l1skills.getProbabilityValue() + attackLevel) - defenseLevel;
                break;
            case 133:
            case 145:
            case L1SkillId.ENTANGLE /*{ENCODED_INT: 152}*/:
            case 153:
            case 161:
            case 167:
            case 173:
            case 174:
                probability = ((int) ((((double) l1skills.getProbabilityDice()) / 10.0d) * ((double) (attackLevel - defenseLevel)))) + l1skills.getProbabilityValue();
                break;
            case 157:
                if (attackLevel >= defenseLevel) {
                    if (attackLevel == defenseLevel) {
                        probability = (this._pc.getInt() / 3) + 20;
                        break;
                    } else {
                        probability = (this._pc.getInt() / 3) + 40;
                        break;
                    }
                } else {
                    probability = (this._pc.getInt() / 3) + 3;
                    break;
                }
            case L1SkillId.GUARD_BRAKE /*{ENCODED_INT: 183}*/:
            case 193:
                int dice1 = l1skills.getProbabilityDice();
                int value = l1skills.getProbabilityValue();
                int diceCount1 = Math.max(getMagicBonus() + getMagicLevel(), 1);
                for (int i = 0; i < diceCount1; i++) {
                    probability2 += _random.nextInt(dice1) + 1 + value;
                }
                if (((int) (((double) probability2) * (((double) getLeverage()) / 10.0d))) >= getTargetMr()) {
                    probability = 100;
                    break;
                } else {
                    probability = 0;
                    break;
                }
            default:
                int dice2 = l1skills.getProbabilityDice();
                int diceCount2 = Math.max(getMagicBonus() + getMagicLevel(), 1);
                for (int i2 = 0; i2 < diceCount2; i2++) {
                    probability2 += _random.nextInt(dice2) + 1;
                }
                probability = ((int) (((double) probability2) * (((double) getLeverage()) / 10.0d))) - getTargetMr();
                break;
        }
        if (this._calcType != 3) {
            return probability;
        }
        switch (skillId) {
            case 20:
            case 40:
            case 103:
                return probability - (this._targetPc.getRegistBlind() >> 1);
            case 33:
            case 212:
                return probability - (this._targetPc.getRegistStone() >> 1);
            case 50:
            case L1SkillId.FREEZING_BLIZZARD /*{ENCODED_INT: 80}*/:
            case 194:
                return probability - (this._targetPc.getRegistFreeze() >> 1);
            case 66:
                return probability - (this._targetPc.getRegistSleep() >> 1);
            case L1SkillId.SHOCK_STUN /*{ENCODED_INT: 87}*/:
            case 208:
                return probability - (this._targetPc.getRegistStun() >> 1);
            case 157:
                return probability - (this._targetPc.getRegistSustain() >> 1);
            default:
                return probability;
        }
    }

    @Override // com.lineage.server.model.L1MagicMode
    public int calcMagicDamage(int skillId) throws Exception {
        int damage = 0;
        switch (this._calcType) {
            case 3:
                damage = calcPcMagicDamage(skillId);
                break;
            case 4:
                damage = calcNpcMagicDamage(skillId);
                break;
        }
        return calcMrDefense(damage);
    }

    private int calcPcMagicDamage(int skillId) throws Exception {
        int dmg;
        if (this._targetPc == null || dmg0(this._targetPc)) {
            return 0;
        }
        if (skillId == 108) {
            dmg = this._npc.getCurrentMp();
        } else {
            dmg = (int) (((double) calcMagicDiceDamage(skillId)) * (((double) getLeverage()) / 10.0d));
        }
        int dmg2 = (dmg - this._targetPc.getDamageReductionByArmor()) - this._targetPc.dmgDowe();
        if (this._targetPc.getClanid() != 0) {
            dmg2 = (int) (((double) dmg2) - getDamageReductionByClan(this._targetPc));
        }
        if (this._targetPc.hasSkillEffect(88)) {
            dmg2 -= ((Math.max(this._targetPc.getLevel(), 50) - 50) / 5) + 1;
        }
        boolean dmgX2 = false;
        if (!this._targetPc.getSkillisEmpty() && this._targetPc.getSkillEffect().size() > 0) {
            try {
                for (Integer key : this._targetPc.getSkillEffect()) {
                    Integer integer = L1AttackList.SKD3.get(key);
                    if (integer != null) {
                        if (integer.equals(key)) {
                            dmgX2 = true;
                        } else {
                            dmg2 += integer.intValue();
                        }
                    }
                }
            } catch (ConcurrentModificationException ignored) {
            } catch (Exception e2) {
                _log.error(e2.getLocalizedMessage(), e2);
            }
        }
        boolean isNowWar = false;
        int castleId = L1CastleLocation.getCastleIdByArea(this._targetPc);
        if (castleId > 0) {
            isNowWar = ServerWarExecutor.get().isNowWar(castleId);
        }
        if (!isNowWar) {
            if (this._npc instanceof L1PetInstance) {
                dmg2 >>= 3;
            }
            if ((this._npc instanceof L1SummonInstance) && ((L1SummonInstance) this._npc).isExsistMaster()) {
                dmg2 >>= 3;
            }
        }
        if (dmgX2) {
            dmg2 >>= 1;
        }
        if (this._targetPc.hasSkillEffect(134)) {
            switch (this._npc.getNpcTemplate().get_npcId()) {
                case 45681:
                case 45682:
                case 45683:
                case 45684:
                case 71014:
                case 71015:
                case 71016:
                case 71026:
                case 71027:
                case 71028:
                case 91159:
                case 91160:
                case 91161:
                case 91162:
                    break;
                default:
                    if (this._npc.getNpcTemplate().get_IsErase() && this._targetPc.getWis() >= _random.nextInt(100)) {
                        this._npc.broadcastPacketX10(new S_DoActionGFX(this._npc.getId(), 2));
                        this._targetPc.sendPacketsX8(new S_SkillSound(this._targetPc.getId(), 4395));
                        this._npc.receiveDamage(this._targetPc, dmg2);
                        dmg2 = 0;
                        this._targetPc.killSkillEffectTimer(134);
                        break;
                    }
            }
        }
        return Math.max(dmg2, 0);
    }

    private int calcNpcMagicDamage(int skillId) {
        if (this._targetNpc == null || dmg0(this._targetNpc)) {
            return 0;
        }
        if (skillId == 108) {
            return this._npc.getCurrentMp();
        }
        return (int) (((double) calcMagicDiceDamage(skillId)) * (((double) getLeverage()) / 10.0d));
    }

    private int calcMagicDiceDamage(int skillId) {
        L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
        int dice = l1skills.getDamageDice();
        int diceCount = l1skills.getDamageDiceCount();
        int value = l1skills.getDamageValue();
        int magicDamage = 0;
        for (int i = 0; i < diceCount; i++) {
            magicDamage += _random.nextInt(dice) + 1;
        }
        return (int) (((double) (magicDamage + value)) * Math.max((1.0d - calcAttrResistance(l1skills.getAttr())) + ((((double) Math.max((this._npc.getInt() + getTargetSp()) - 12, 1)) * 3.0d) / 32.0d), 0.0d));
    }

    @Override // com.lineage.server.model.L1MagicMode
    public int calcHealing(int skillId) {
        L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
        int dice = l1skills.getDamageDice();
        int magicDamage = 0;
        int diceCount = l1skills.getDamageValue() + Math.min(getMagicBonus(), 10);
        for (int i = 0; i < diceCount; i++) {
            magicDamage += _random.nextInt(dice) + 1;
        }
        double alignmentRevision = 1.0d;
        if (getLawful() > 0) {
            alignmentRevision = 1.0d + (((double) getLawful()) / 32768.0d);
        }
        return (int) (((double) ((int) (((double) magicDamage) * alignmentRevision))) * (((double) getLeverage()) / 10.0d));
    }

    private int calcMrDefense(int dmg) {
        int mr = getTargetMr();
        double mrFloor = 0.0d;
        double mrCoefficient = 0.0d;
        if (mr == 0) {
            mrFloor = 1.0d;
            mrCoefficient = 1.0d;
        } else if (mr > 0 && mr <= 50) {
            mrFloor = 2.0d;
            mrCoefficient = 1.0d;
        } else if (mr > 50 && mr <= 100) {
            mrFloor = 3.0d;
            mrCoefficient = 0.9d;
        } else if (mr > 100 && mr <= 120) {
            mrFloor = 4.0d;
            mrCoefficient = 0.9d;
        } else if (mr > 120 && mr <= 140) {
            mrFloor = 5.0d;
            mrCoefficient = 0.8d;
        } else if (mr > 140 && mr <= 160) {
            mrFloor = 6.0d;
            mrCoefficient = 0.8d;
        } else if (mr > 160 && mr <= 180) {
            mrFloor = 7.0d;
            mrCoefficient = 0.7d;
        } else if (mr > 180 && mr <= 200) {
            mrFloor = 8.0d;
            mrCoefficient = 0.7d;
        } else if (mr > 200 && mr <= 220) {
            mrFloor = 9.0d;
            mrCoefficient = 0.6d;
        } else if (mr > 220 && mr <= 240) {
            mrFloor = 10.0d;
            mrCoefficient = 0.6d;
        } else if (mr > 240) {
            mrFloor = 11.0d;
            mrCoefficient = 0.5d;
        }
        int originalInt = this._npc.getInt();
        int originalMagicHit = 1;
        if (originalInt < 18) {
            originalMagicHit = 1;
        } else if (originalInt >= 18 && originalInt < 36) {
            originalMagicHit = 2;
        } else if (originalInt >= 36 && originalInt < 72) {
            originalMagicHit = 3;
        } else if (originalInt >= 72) {
            originalMagicHit = 4;
        }
        return (int) (((double) dmg) * (mrCoefficient - (0.01d * Math.floor(((double) (mr - originalMagicHit)) / mrFloor))));
    }

    @Override // com.lineage.server.model.L1MagicMode
    public void commit(int damage, int drainMana) {
        if (this._npc.getNpcTemplate().is_boss()) {
            damage = (int) (((double) damage) * 1.25d);
        }
        switch (this._calcType) {
            case 3:
                commitPc(damage, drainMana);
                break;
            case 4:
                commitNpc(damage, drainMana);
                break;
        }
        if (ConfigAlt.ALT_ATKMSG && this._calcType != 4 && this._targetPc.isGm()) {
            StringBuilder atkMsg = new StringBuilder();
            atkMsg.append("受到NPC技能: ");
            atkMsg.append(String.valueOf(this._npc.getNameId()) + ">");
            atkMsg.append(String.valueOf(this._targetPc.getName()) + " ");
            atkMsg.append("傷害: " + damage);
            this._targetPc.sendPackets(new S_ServerMessage(166, atkMsg.toString()));
        }
    }

    private void commitPc(int damage, int drainMana) {
        try {
            this._targetPc.receiveDamage(this._npc, (double) damage, true, false);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void commitNpc(int damage, int drainMana) {
        try {
            this._targetNpc.receiveDamage(this._npc, damage);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
