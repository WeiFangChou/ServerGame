package com.lineage.server.model;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigElfSkill;
import com.lineage.config.ConfigKnightSkill;
import com.lineage.config.ConfigWizardSkill;
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

public class L1MagicPc extends L1MagicMode {
    private static final Log _log = LogFactory.getLog(L1MagicPc.class);

    public L1MagicPc(L1PcInstance attacker, L1Character target) {
        if (attacker != null) {
            this._pc = attacker;
            if (target instanceof L1PcInstance) {
                this._calcType = 1;
                this._targetPc = (L1PcInstance) target;
                return;
            }
            this._calcType = 2;
            this._targetNpc = (L1NpcInstance) target;
        }
    }

    private int getMagicLevel() {
        return this._pc.getMagicLevel();
    }

    private int getMagicBonus() {
        return this._pc.getMagicBonus();
    }

    private int getLawful() {
        return this._pc.getLawful();
    }

    @Override // com.lineage.server.model.L1MagicMode
    public boolean calcProbabilityMagic(int skillId) {
        boolean isSuccess;
        switch (this._calcType) {
            case 1:
                if (!(skillId != 44 || this._pc == null || this._targetPc == null)) {
                    if (this._pc.getId() == this._targetPc.getId()) {
                        return true;
                    }
                    if (this._pc.getClanid() > 0 && this._pc.getClanid() == this._targetPc.getClanid()) {
                        return true;
                    }
                    if (this._pc.isInParty() && this._pc.getParty().isMember(this._targetPc)) {
                        return true;
                    }
                }
                if (!checkZone(skillId)) {
                    return false;
                }
                if (!(!this._targetPc.hasSkillEffect(157) || skillId == 27 || skillId == 44)) {
                    return false;
                }
                if (calcEvasion()) {
                    return false;
                }
                break;
            case 2:
                if (this._targetNpc != null) {
                    switch (this._targetNpc.getNpcTemplate().get_gfxid()) {
                        case 2412:
                            if (!this._pc.getInventory().checkEquipped(20046)) {
                                return false;
                            }
                            break;
                    }
                    int npcId = this._targetNpc.getNpcTemplate().get_npcId();
                    Integer tgskill = L1AttackList.SKNPC.get(Integer.valueOf(npcId));
                    if (!(tgskill == null || this._pc.hasSkillEffect(tgskill.intValue()))) {
                        return false;
                    }
                    Integer tgpoly = L1AttackList.PLNPC.get(Integer.valueOf(npcId));
                    if (tgpoly != null && tgpoly.equals(Integer.valueOf(this._pc.getTempCharGfx()))) {
                        return false;
                    }
                    if (L1AttackList.DNNPC.containsKey(Integer.valueOf(npcId))) {
                        for (Integer dgskillid : L1AttackList.DNNPC.get(Integer.valueOf(npcId))) {
                            if (dgskillid.equals(Integer.valueOf(skillId))) {
                                return false;
                            }
                        }
                    }
                }
                if (skillId == 44) {
                    return true;
                }
                if (!(!this._targetNpc.hasSkillEffect(157) || skillId == 27 || skillId == 44)) {
                    return false;
                }
                break;
        }
        int probability = calcProbability(skillId);
        int rnd = _random.nextInt(100) + 1;
        int probability2 = Math.max(Math.min(probability, 90), 1);
        if (probability2 >= rnd) {
            isSuccess = true;
        } else {
            isSuccess = false;
        }
        if (!ConfigAlt.ALT_ATKMSG) {
            return isSuccess;
        }
        switch (this._calcType) {
            case 1:
                if (!this._pc.isGm() && !this._targetPc.isGm()) {
                    return isSuccess;
                }
            case 2:
                if (!this._pc.isGm()) {
                    return isSuccess;
                }
                break;
        }
        switch (this._calcType) {
            case 1:
                if (this._pc.isGm()) {
                    StringBuilder atkMsg = new StringBuilder();
                    atkMsg.append("對PC送出技能: ");
                    atkMsg.append(String.valueOf(this._pc.getName()) + ">");
                    atkMsg.append(String.valueOf(this._targetPc.getName()) + " ");
                    atkMsg.append(isSuccess ? "成功" : "失敗");
                    atkMsg.append(" 成功機率:" + probability2 + "%");
                    this._pc.sendPackets(new S_ServerMessage(166, atkMsg.toString()));
                }
                if (this._targetPc.isGm()) {
                    StringBuilder atkMsg2 = new StringBuilder();
                    atkMsg2.append("受到PC技能: ");
                    atkMsg2.append(String.valueOf(this._pc.getName()) + ">");
                    atkMsg2.append(String.valueOf(this._targetPc.getName()) + " ");
                    atkMsg2.append(isSuccess ? "成功" : "失敗");
                    atkMsg2.append(" 成功機率:" + probability2 + "%");
                    this._targetPc.sendPackets(new S_ServerMessage(166, atkMsg2.toString()));
                    break;
                }
                break;
            case 2:
                if (this._pc.isGm()) {
                    StringBuilder atkMsg3 = new StringBuilder();
                    atkMsg3.append("對NPC送出技能: ");
                    atkMsg3.append(String.valueOf(this._pc.getName()) + ">");
                    atkMsg3.append(String.valueOf(this._targetNpc.getName()) + " ");
                    atkMsg3.append(isSuccess ? "成功" : "失敗");
                    atkMsg3.append(" 成功機率:" + probability2 + "%");
                    this._pc.sendPackets(new S_ServerMessage(166, atkMsg3.toString()));
                    break;
                }
                break;
        }
        return isSuccess;
    }

    private boolean checkZone(int skillId) {
        if (this._pc == null || this._targetPc == null || ((!this._pc.isSafetyZone() && !this._targetPc.isSafetyZone()) || L1AttackList.NZONE.get(Integer.valueOf(skillId)) == null)) {
            return true;
        }
        return false;
    }

    private int calcProbability(int skillId) {
        int diceCount2;
        L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
        int attackLevel = this._pc.getLevel();
        int defenseLevel = 0;
        int probability = 0;
        switch (this._calcType) {
            case 1:
                if (!this._targetPc.isGm()) {
                    defenseLevel = this._targetPc.getLevel();
                    break;
                } else {
                    return -1;
                }
            case 2:
                defenseLevel = this._targetNpc.getLevel();
                if (skillId == 145 && (this._targetNpc instanceof L1SummonInstance)) {
                    defenseLevel = ((L1SummonInstance) this._targetNpc).getMaster().getLevel();
                    break;
                }
        }
        switch (skillId) {
            case 27:
            case 29:
                int dice3 = l1skills.getProbabilityDice();
                int diceCount3 = Math.max(this._pc.isWizard() ? getMagicBonus() + getMagicLevel() + 1 : (getMagicBonus() + getMagicLevel()) - 1, 1);
                for (int i = 0; i < diceCount3; i++) {
                    probability += _random.nextInt(dice3) + 1;
                }
                probability = ((((int) (((double) probability) * (((double) getLeverage()) / 10.0d))) + (this._pc.getOriginalMagicHit() * 2)) - getTargetMr()) / Math.max(defenseLevel / 24, 1);
                break;
            case 33:
                if (attackLevel > defenseLevel) {
                    probability = ConfigWizardSkill.CURSE_PARALYZE_1;
                } else if (attackLevel == defenseLevel) {
                    probability = ConfigWizardSkill.CURSE_PARALYZE_2;
                } else if (attackLevel < defenseLevel) {
                    probability = ConfigWizardSkill.CURSE_PARALYZE_3;
                }
                if (ConfigWizardSkill.CURSE_PARALYZE_INT > 0.0d) {
                    probability = (int) (((double) probability) + (ConfigWizardSkill.CURSE_PARALYZE_INT * ((double) this._pc.getInt())));
                }
                if (ConfigWizardSkill.CURSE_PARALYZE_MR > 0.0d) {
                    probability = (int) (((double) probability) - (ConfigWizardSkill.CURSE_PARALYZE_MR * ((double) getTargetMr())));
                }
                probability += this._pc.getOriginalMagicHit();
                break;
            case 40:
                if (attackLevel > defenseLevel) {
                    probability = ConfigWizardSkill.DARKNESS_1;
                } else if (attackLevel == defenseLevel) {
                    probability = ConfigWizardSkill.DARKNESS_2;
                } else if (attackLevel < defenseLevel) {
                    probability = ConfigWizardSkill.DARKNESS_3;
                }
                if (ConfigWizardSkill.DARKNESS_INT > 0.0d) {
                    probability = (int) (((double) probability) + (ConfigWizardSkill.DARKNESS_INT * ((double) this._pc.getInt())));
                }
                if (ConfigWizardSkill.DARKNESS_MR > 0.0d) {
                    probability = (int) (((double) probability) - (ConfigWizardSkill.DARKNESS_MR * ((double) getTargetMr())));
                }
                probability += this._pc.getOriginalMagicHit();
                break;
            case 44:
                if (attackLevel > defenseLevel) {
                    probability = ConfigWizardSkill.CANCELLATION_1;
                } else if (attackLevel == defenseLevel) {
                    probability = ConfigWizardSkill.CANCELLATION_2;
                } else if (attackLevel < defenseLevel) {
                    probability = ConfigWizardSkill.CANCELLATION_3;
                }
                if (ConfigWizardSkill.CANCELLATION_INT > 0.0d) {
                    probability = (int) (((double) probability) + (ConfigWizardSkill.CANCELLATION_INT * ((double) this._pc.getInt())));
                }
                if (ConfigWizardSkill.CANCELLATION_MR > 0.0d) {
                    probability = (int) (((double) probability) - (ConfigWizardSkill.CANCELLATION_MR * ((double) getTargetMr())));
                }
                probability += this._pc.getOriginalMagicHit();
                break;
            case 50:
                if (attackLevel > defenseLevel) {
                    probability = ConfigWizardSkill.ICE_LANCE_1;
                } else if (attackLevel == defenseLevel) {
                    probability = ConfigWizardSkill.ICE_LANCE_2;
                } else if (attackLevel < defenseLevel) {
                    probability = ConfigWizardSkill.ICE_LANCE_3;
                }
                if (ConfigWizardSkill.ICE_LANCE_INT > 0.0d) {
                    probability = (int) (((double) probability) + (ConfigWizardSkill.ICE_LANCE_INT * ((double) this._pc.getInt())));
                }
                if (ConfigWizardSkill.ICE_LANCE_MR > 0.0d) {
                    probability = (int) (((double) probability) - (ConfigWizardSkill.ICE_LANCE_MR * ((double) getTargetMr())));
                }
                probability += this._pc.getOriginalMagicHit();
                break;
            case 64:
                if (attackLevel > defenseLevel) {
                    probability = ConfigWizardSkill.SILENCE_1;
                } else if (attackLevel == defenseLevel) {
                    probability = ConfigWizardSkill.SILENCE_2;
                } else if (attackLevel < defenseLevel) {
                    probability = ConfigWizardSkill.SILENCE_3;
                }
                if (ConfigWizardSkill.SILENCE_INT > 0.0d) {
                    probability = (int) (((double) probability) + (ConfigWizardSkill.SILENCE_INT * ((double) this._pc.getInt())));
                }
                if (ConfigWizardSkill.SILENCE_MR > 0.0d) {
                    probability = (int) (((double) probability) - (ConfigWizardSkill.SILENCE_MR * ((double) getTargetMr())));
                    break;
                }
                break;
            case 66:
                if (attackLevel > defenseLevel) {
                    probability = ConfigWizardSkill.FOG_OF_SLEEPING_1;
                } else if (attackLevel == defenseLevel) {
                    probability = ConfigWizardSkill.FOG_OF_SLEEPING_2;
                } else if (attackLevel < defenseLevel) {
                    probability = ConfigWizardSkill.FOG_OF_SLEEPING_3;
                }
                if (ConfigWizardSkill.FOG_OF_SLEEPING_INT > 0.0d) {
                    probability = (int) (((double) probability) + (ConfigWizardSkill.FOG_OF_SLEEPING_INT * ((double) this._pc.getInt())));
                }
                if (ConfigWizardSkill.FOG_OF_SLEEPING_MR > 0.0d) {
                    probability = (int) (((double) probability) - (ConfigWizardSkill.FOG_OF_SLEEPING_MR * ((double) getTargetMr())));
                }
                probability += this._pc.getOriginalMagicHit();
                break;
            case 71:
                if (attackLevel > defenseLevel) {
                    probability = ConfigWizardSkill.DECAY_POTION_1;
                } else if (attackLevel == defenseLevel) {
                    probability = ConfigWizardSkill.DECAY_POTION_2;
                } else if (attackLevel < defenseLevel) {
                    probability = ConfigWizardSkill.DECAY_POTION_3;
                }
                if (ConfigWizardSkill.DECAY_POTION_INT > 0.0d) {
                    probability = (int) (((double) probability) + (ConfigWizardSkill.DECAY_POTION_INT * ((double) this._pc.getInt())));
                }
                if (ConfigWizardSkill.DECAY_POTION_MR > 0.0d) {
                    probability = (int) (((double) probability) - (ConfigWizardSkill.DECAY_POTION_MR * ((double) getTargetMr())));
                }
                probability += this._pc.getOriginalMagicHit();
                break;
            case L1SkillId.SHOCK_STUN /*{ENCODED_INT: 87}*/:
                if (attackLevel >= defenseLevel) {
                    if (attackLevel == defenseLevel) {
                        probability = ConfigKnightSkill.ImpactHalo2;
                        break;
                    } else {
                        probability = ConfigKnightSkill.ImpactHalo3;
                        break;
                    }
                } else {
                    probability = ConfigKnightSkill.ImpactHalo1;
                    break;
                }
            case L1SkillId.COUNTER_BARRIER /*{ENCODED_INT: 91}*/:
                probability = ((l1skills.getProbabilityValue() + attackLevel) - defenseLevel) + (this._pc.getOriginalMagicHit() << 1);
                break;
            case 133:
            case 145:
                probability = ((((int) ((((double) l1skills.getProbabilityDice()) / 10.0d) * ((double) (attackLevel - defenseLevel)))) + l1skills.getProbabilityValue()) + (this._pc.getOriginalMagicHit() << 1)) - (getTargetMr() / 80);
                break;
            case L1SkillId.ENTANGLE /*{ENCODED_INT: 152}*/:
                if (attackLevel > defenseLevel) {
                    probability = ConfigElfSkill.ENTANGLE_1;
                } else if (attackLevel == defenseLevel) {
                    probability = ConfigElfSkill.ENTANGLE_2;
                } else if (attackLevel < defenseLevel) {
                    probability = ConfigElfSkill.ENTANGLE_3;
                }
                if (ConfigElfSkill.ENTANGLE_INT > 0.0d) {
                    probability = (int) (((double) probability) + (ConfigElfSkill.ENTANGLE_INT * ((double) this._pc.getInt())));
                }
                if (ConfigElfSkill.ENTANGLE_MR > 0.0d) {
                    probability = (int) (((double) probability) - (ConfigElfSkill.ENTANGLE_MR * ((double) getTargetMr())));
                }
                probability += this._pc.getOriginalMagicHit();
                break;
            case 153:
                if (attackLevel > defenseLevel) {
                    probability = ConfigElfSkill.ERASE_MAGIC_1;
                } else if (attackLevel == defenseLevel) {
                    probability = ConfigElfSkill.ERASE_MAGIC_2;
                } else if (attackLevel < defenseLevel) {
                    probability = ConfigElfSkill.ERASE_MAGIC_3;
                }
                if (ConfigElfSkill.ERASE_MAGIC_INT > 0.0d) {
                    probability = (int) (((double) probability) + (ConfigElfSkill.ERASE_MAGIC_INT * ((double) this._pc.getInt())));
                }
                if (ConfigElfSkill.ERASE_MAGIC_MR > 0.0d) {
                    probability = (int) (((double) probability) - (ConfigElfSkill.ERASE_MAGIC_MR * ((double) getTargetMr())));
                }
                probability += this._pc.getOriginalMagicHit();
                break;
            case 157:
                if (attackLevel > defenseLevel) {
                    probability = ConfigElfSkill.EARTH_BIND_1;
                } else if (attackLevel == defenseLevel) {
                    probability = ConfigElfSkill.EARTH_BIND_2;
                } else if (attackLevel < defenseLevel) {
                    probability = ConfigElfSkill.EARTH_BIND_3;
                }
                if (ConfigElfSkill.EARTH_BIND_INT > 0.0d) {
                    probability = (int) (((double) probability) + (ConfigElfSkill.EARTH_BIND_INT * ((double) this._pc.getInt())));
                }
                if (ConfigElfSkill.EARTH_BIND_MR > 0.0d) {
                    probability = (int) (((double) probability) - (ConfigElfSkill.EARTH_BIND_MR * ((double) getTargetMr())));
                }
                probability += this._pc.getOriginalMagicHit();
                break;
            case 161:
                if (attackLevel > defenseLevel) {
                    probability = ConfigElfSkill.AREA_OF_SILENCE_1;
                } else if (attackLevel == defenseLevel) {
                    probability = ConfigElfSkill.AREA_OF_SILENCE_2;
                } else if (attackLevel < defenseLevel) {
                    probability = ConfigElfSkill.AREA_OF_SILENCE_3;
                }
                if (ConfigElfSkill.AREA_OF_SILENCE_INT > 0.0d) {
                    probability = (int) (((double) probability) + (ConfigElfSkill.AREA_OF_SILENCE_INT * ((double) this._pc.getInt())));
                }
                if (ConfigElfSkill.AREA_OF_SILENCE_MR > 0.0d) {
                    probability = (int) (((double) probability) - (ConfigElfSkill.AREA_OF_SILENCE_MR * ((double) getTargetMr())));
                }
                probability += this._pc.getOriginalMagicHit();
                break;
            case 167:
                if (attackLevel > defenseLevel) {
                    probability = ConfigElfSkill.WIND_SHACKLE_1;
                } else if (attackLevel == defenseLevel) {
                    probability = ConfigElfSkill.WIND_SHACKLE_2;
                } else if (attackLevel < defenseLevel) {
                    probability = ConfigElfSkill.WIND_SHACKLE_3;
                }
                if (ConfigElfSkill.WIND_SHACKLE_INT > 0.0d) {
                    probability = (int) (((double) probability) + (ConfigElfSkill.WIND_SHACKLE_INT * ((double) this._pc.getInt())));
                }
                if (ConfigElfSkill.WIND_SHACKLE_MR > 0.0d) {
                    probability = (int) (((double) probability) - (ConfigElfSkill.WIND_SHACKLE_MR * ((double) getTargetMr())));
                }
                probability += this._pc.getOriginalMagicHit();
                break;
            case 173:
                if (attackLevel > defenseLevel) {
                    probability = ConfigElfSkill.POLLUTE_WATER_RND_1;
                } else if (attackLevel == defenseLevel) {
                    probability = ConfigElfSkill.POLLUTE_WATER_RND_2;
                } else if (attackLevel < defenseLevel) {
                    probability = ConfigElfSkill.POLLUTE_WATER_RND_3;
                }
                if (ConfigElfSkill.POLLUTE_WATER_INT > 0.0d) {
                    probability = (int) (((double) probability) + (ConfigElfSkill.POLLUTE_WATER_INT * ((double) this._pc.getInt())));
                }
                if (ConfigElfSkill.POLLUTE_WATER_MR > 0.0d) {
                    probability = (int) (((double) probability) - (ConfigElfSkill.POLLUTE_WATER_MR * ((double) getTargetMr())));
                }
                probability += this._pc.getOriginalMagicHit();
                break;
            case 174:
                if (attackLevel > defenseLevel) {
                    probability = ConfigElfSkill.STRIKER_GALE_1;
                } else if (attackLevel == defenseLevel) {
                    probability = ConfigElfSkill.STRIKER_GALE_2;
                } else if (attackLevel < defenseLevel) {
                    probability = ConfigElfSkill.STRIKER_GALE_3;
                }
                if (ConfigElfSkill.STRIKER_GALE_INT > 0.0d) {
                    probability = (int) (((double) probability) + (ConfigElfSkill.STRIKER_GALE_INT * ((double) this._pc.getInt())));
                }
                if (ConfigElfSkill.STRIKER_GALE_MR > 0.0d) {
                    probability = (int) (((double) probability) - (ConfigElfSkill.STRIKER_GALE_MR * ((double) getTargetMr())));
                }
                probability += this._pc.getOriginalMagicHit();
                break;
            case L1SkillId.GUARD_BRAKE /*{ENCODED_INT: 183}*/:
            case 193:
                int dice1 = l1skills.getProbabilityDice();
                int value = l1skills.getProbabilityValue();
                int diceCount1 = Math.max(getMagicBonus() + getMagicLevel(), 1);
                for (int i2 = 0; i2 < diceCount1; i2++) {
                    probability += _random.nextInt(dice1) + 1 + value;
                }
                if (((int) (((double) probability) * (((double) getLeverage()) / 10.0d))) + (this._pc.getOriginalMagicHit() << 1) >= getTargetMr()) {
                    probability = 100;
                    break;
                } else {
                    probability = 0;
                    break;
                }
            case 188:
                int dice4 = l1skills.getProbabilityDice();
                int diceCount4 = Math.max(getMagicBonus() + getMagicLevel() + 1, 1);
                for (int i3 = 0; i3 < diceCount4; i3++) {
                    probability += _random.nextInt(dice4) + 1;
                }
                probability = ((((int) (((double) probability) * (((double) getLeverage()) / 10.0d))) + (this._pc.getOriginalMagicHit() * 2)) - getTargetMr()) / Math.max(defenseLevel / 20, 1);
                break;
            case 202:
                if (_random.nextInt(100) + 1 <= _random.nextInt(10) + 20) {
                    probability = 100;
                    break;
                } else {
                    probability = 0;
                    break;
                }
            case 212:
                if (_random.nextInt(100) + 1 <= 30) {
                    probability = 100;
                    break;
                } else {
                    probability = 0;
                    break;
                }
            case 217:
                probability = ((l1skills.getProbabilityValue() + attackLevel) - defenseLevel) + this._pc.getOriginalMagicHit();
                break;
            default:
                int dice2 = l1skills.getProbabilityDice();
                if (this._pc.isWizard()) {
                    diceCount2 = getMagicBonus() + getMagicLevel() + 1;
                } else if (this._pc.isElf()) {
                    diceCount2 = (getMagicBonus() + getMagicLevel()) - 1;
                } else {
                    diceCount2 = (getMagicBonus() + getMagicLevel()) - 1;
                }
                int diceCount22 = Math.max(diceCount2, 1);
                for (int i4 = 0; i4 < diceCount22; i4++) {
                    if (dice2 > 0) {
                        probability += _random.nextInt(dice2) + 1;
                    }
                }
                probability = (((int) (((double) probability) * (((double) getLeverage()) / 10.0d))) + (this._pc.getOriginalMagicHit() << 1)) - getTargetMr();
                if (skillId == 36) {
                    double probabilityRevision = 1.0d;
                    if ((this._targetNpc.getMaxHp() >> 2) > this._targetNpc.getCurrentHp()) {
                        probabilityRevision = 1.3d;
                    } else if (((this._targetNpc.getMaxHp() << 2) >> 2) > this._targetNpc.getCurrentHp()) {
                        probabilityRevision = 1.2d;
                    } else if (((this._targetNpc.getMaxHp() * 3) >> 2) > this._targetNpc.getCurrentHp()) {
                        probabilityRevision = 1.1d;
                    }
                    probability = (int) (((double) probability) * probabilityRevision);
                    break;
                }
                break;
        }
        switch (this._calcType) {
            case 1:
                switch (skillId) {
                    case 20:
                    case 40:
                    case 103:
                        probability -= this._targetPc.getRegistBlind() >> 1;
                        break;
                    case 33:
                    case 212:
                        probability -= this._targetPc.getRegistStone() >> 1;
                        break;
                    case 50:
                    case L1SkillId.FREEZING_BLIZZARD /*{ENCODED_INT: 80}*/:
                    case 194:
                        probability -= this._targetPc.getRegistFreeze() >> 1;
                        break;
                    case 66:
                        probability -= this._targetPc.getRegistSleep() >> 1;
                        break;
                    case L1SkillId.SHOCK_STUN /*{ENCODED_INT: 87}*/:
                    case 208:
                        probability -= this._targetPc.getRegistStun() >> 1;
                        break;
                    case 157:
                        probability -= this._targetPc.getRegistSustain() >> 1;
                        break;
                }
        }
        return probability;
    }

    @Override // com.lineage.server.model.L1MagicMode
    public int calcMagicDamage(int skillId) throws Exception {
        int damage = 0;
        switch (this._calcType) {
            case 1:
                damage = calcPcMagicDamage(skillId);
                break;
            case 2:
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
            dmg = this._pc.getCurrentMp();
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
        if (dmgX2) {
            dmg2 >>= 1;
        }
        if (this._targetPc.hasSkillEffect(134) && this._calcType == 1 && this._targetPc.getWis() >= _random.nextInt(100)) {
            this._pc.sendPacketsAll(new S_DoActionGFX(this._pc.getId(), 2));
            this._targetPc.sendPacketsX8(new S_SkillSound(this._targetPc.getId(), 4395));
            this._pc.receiveDamage(this._targetPc, (double) dmg2, false, false);
            dmg2 = 0;
            this._targetPc.killSkillEffectTimer(134);
        }
        return Math.max(dmg2, 0);
    }

    private int calcNpcMagicDamage(int skillId) {
        int dmg;
        if (this._targetNpc == null || dmg0(this._targetNpc)) {
            return 0;
        }
        int npcId = this._targetNpc.getNpcTemplate().get_npcId();
        Integer tgskill = L1AttackList.SKNPC.get(Integer.valueOf(npcId));
        if (tgskill != null && !this._pc.hasSkillEffect(tgskill.intValue())) {
            return 0;
        }
        Integer tgpoly = L1AttackList.PLNPC.get(Integer.valueOf(npcId));
        if (tgpoly != null && tgpoly.equals(Integer.valueOf(this._pc.getTempCharGfx()))) {
            return 0;
        }
        if (skillId == 108) {
            dmg = this._pc.getCurrentMp();
        } else {
            dmg = (int) (((double) calcMagicDiceDamage(skillId)) * (((double) getLeverage()) / 10.0d));
        }
        boolean isNowWar = false;
        int castleId = L1CastleLocation.getCastleIdByArea(this._targetNpc);
        if (castleId > 0) {
            isNowWar = ServerWarExecutor.get().isNowWar(castleId);
        }
        boolean isPet = false;
        if (this._targetNpc instanceof L1PetInstance) {
            isPet = true;
            if (this._targetNpc.getMaster().equals(this._pc)) {
                dmg = 0;
            }
        }
        if (this._targetNpc instanceof L1SummonInstance) {
            if (((L1SummonInstance) this._targetNpc).isExsistMaster()) {
                isPet = true;
            }
            if (this._targetNpc.getMaster().equals(this._pc)) {
                dmg = 0;
            }
        }
        if (isNowWar || !isPet || dmg == 0) {
            return dmg;
        }
        return dmg >> 3;
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
        int magicDamage2 = magicDamage + value;
        if (this._pc.getClanid() != 0) {
            magicDamage2 = (int) (((double) magicDamage2) + getDamageUpByClan(this._pc));
        }
        switch (this._pc.guardianEncounter()) {
            case 3:
                magicDamage2++;
                break;
            case 4:
                magicDamage2 += 2;
                break;
            case 5:
                magicDamage2 += 3;
                break;
        }
        double d = (double) magicDamage2;
        int magicDamage3 = (int) (d * Math.max((1.0d - calcAttrResistance(l1skills.getAttr())) + ((((double) Math.max((this._pc.getInt() + getTargetSp()) - 12, 1)) * 3.0d) / 32.0d), 0.0d));
        int rnd = _random.nextInt(100) + 1;
        if (l1skills.getSkillLevel() <= 6 && rnd <= this._pc.getOriginalMagicCritical() + 10) {
            magicDamage3 = (int) (((double) magicDamage3) * 1.5d);
        }
        return magicDamage3 + this._pc.getOriginalMagicDamage();
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
        double mrFloor;
        double mrCoefficient;
        int mr = getTargetMr();
        Double[] mrF = L1AttackList.MRDMG.get(new Integer(mr));
        if (mrF != null) {
            mrFloor = mrF[0].doubleValue();
            mrCoefficient = mrF[1].doubleValue();
        } else {
            mrFloor = 11.0d;
            mrCoefficient = 0.5d;
        }
        return (int) (((double) dmg) * (mrCoefficient - (0.01d * Math.floor(((double) (mr - this._pc.getOriginalMagicHit())) / mrFloor))));
    }

    @Override // com.lineage.server.model.L1MagicMode
    public void commit(int damage, int drainMana) throws Exception {
        L1Character _target = null;
        switch (this._calcType) {
            case 1:
                _target = this._targetPc;
                commitPc(damage, drainMana);
                break;
            case 2:
                _target = this._targetNpc;
                commitNpc(damage, drainMana);
                break;
        }
        int i = ((int) ((((double) damage) / Math.pow(10.0d, 0.0d)) % 10.0d)) + 11821;
        int k = ((int) ((((double) damage) / Math.pow(10.0d, 1.0d)) % 10.0d)) + 11831;
        int h = ((int) ((((double) damage) / Math.pow(10.0d, 2.0d)) % 10.0d)) + 11841;
        int s = ((int) ((((double) damage) / Math.pow(10.0d, 3.0d)) % 10.0d)) + 11851;
        int m = ((int) ((((double) damage) / Math.pow(10.0d, 4.0d)) % 10.0d)) + 11861;
        if (this._pc.is_send_weapon_dmg_gfxid()) {
            if (damage <= 0) {
                this._pc.sendPackets(new S_SkillSound(_target.getId(), 11871));
            } else if (damage > 0 && damage < 10) {
                this._pc.sendPackets(new S_SkillSound(_target.getId(), i));
            } else if (damage >= 10 && damage < 100) {
                this._pc.sendPackets(new S_SkillSound(_target.getId(), i));
                this._pc.sendPackets(new S_SkillSound(_target.getId(), k));
            } else if (damage >= 100 && damage < 1000) {
                this._pc.sendPackets(new S_SkillSound(_target.getId(), i));
                this._pc.sendPackets(new S_SkillSound(_target.getId(), k));
                this._pc.sendPackets(new S_SkillSound(_target.getId(), h));
            } else if (damage >= 1000 && damage < 10000) {
                this._pc.sendPackets(new S_SkillSound(_target.getId(), i));
                this._pc.sendPackets(new S_SkillSound(_target.getId(), k));
                this._pc.sendPackets(new S_SkillSound(_target.getId(), h));
                this._pc.sendPackets(new S_SkillSound(_target.getId(), s));
            } else if (damage >= 10000) {
                this._pc.sendPackets(new S_SkillSound(_target.getId(), i));
                this._pc.sendPackets(new S_SkillSound(_target.getId(), k));
                this._pc.sendPackets(new S_SkillSound(_target.getId(), h));
                this._pc.sendPackets(new S_SkillSound(_target.getId(), s));
                this._pc.sendPackets(new S_SkillSound(_target.getId(), m));
            }
        }
        if (ConfigAlt.ALT_ATKMSG) {
            switch (this._calcType) {
                case 1:
                    if (!this._pc.isGm() && !this._targetPc.isGm()) {
                        return;
                    }
                case 2:
                    if (!this._pc.isGm()) {
                        return;
                    }
                    break;
            }
            switch (this._calcType) {
                case 1:
                    if (this._pc.isGm()) {
                        StringBuilder atkMsg = new StringBuilder();
                        atkMsg.append("對PC送出技能: ");
                        atkMsg.append(String.valueOf(this._pc.getName()) + ">");
                        atkMsg.append(String.valueOf(this._targetPc.getName()) + " ");
                        atkMsg.append("傷害: " + damage);
                        atkMsg.append(" 目標HP:" + this._targetPc.getCurrentHp());
                        this._pc.sendPackets(new S_ServerMessage(166, atkMsg.toString()));
                    }
                    if (this._targetPc.isGm()) {
                        StringBuilder atkMsg2 = new StringBuilder();
                        atkMsg2.append("受到PC技能: ");
                        atkMsg2.append(String.valueOf(this._pc.getName()) + ">");
                        atkMsg2.append(String.valueOf(this._targetPc.getName()) + " ");
                        atkMsg2.append("傷害: " + damage);
                        atkMsg2.append(" 目標HP:" + this._targetPc.getCurrentHp());
                        this._targetPc.sendPackets(new S_ServerMessage(166, atkMsg2.toString()));
                        return;
                    }
                    return;
                case 2:
                    if (this._pc.isGm()) {
                        StringBuilder atkMsg3 = new StringBuilder();
                        atkMsg3.append("對NPC送出技能: ");
                        atkMsg3.append(String.valueOf(this._pc.getName()) + ">");
                        atkMsg3.append(String.valueOf(this._targetNpc.getNameId()) + " ");
                        atkMsg3.append("傷害: " + damage);
                        atkMsg3.append(" 目標HP:" + this._targetNpc.getCurrentHp());
                        this._pc.sendPackets(new S_ServerMessage(166, atkMsg3.toString()));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private void commitPc(int damage, int drainMana) throws Exception {
        if (drainMana > 0) {
            try {
                if (this._targetPc.getCurrentMp() > 0) {
                    drainMana = Math.min(drainMana, this._targetPc.getCurrentMp());
                    this._pc.setCurrentMp(this._pc.getCurrentMp() + drainMana);
                } else {
                    drainMana = 0;
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
                return;
            }
        }
        this._targetPc.receiveManaDamage(this._pc, drainMana);
        this._targetPc.receiveDamage(this._pc, (double) damage, true, false);
    }

    private void commitNpc(int damage, int drainMana) throws Exception {
        if (drainMana > 0) {
            try {
                if (this._targetNpc.getCurrentMp() > 0) {
                    this._pc.setCurrentMp(this._pc.getCurrentMp() + this._targetNpc.drainMana(drainMana));
                } else {
                    drainMana = 0;
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
                return;
            }
        }
        this._targetNpc.ReceiveManaDamage(this._pc, drainMana);
        this._targetNpc.receiveDamage(this._pc, damage);
    }
}
