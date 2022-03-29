package com.lineage.server.model;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigDarkElfSkill;
import com.lineage.data.event.FeatureItemSet;
import com.lineage.data.event.TypeId_Orginal;
import com.lineage.server.datatables.GfxIdOrginal;
import com.lineage.server.datatables.TypeIdOrginal;
import com.lineage.server.datatables.WeaponTypeSystem;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.poison.L1DamagePoison;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_AttackPacketPc;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_PacketBoxDk;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.serverpackets.S_UseArrowSkill;
import com.lineage.server.templates.L1WeaponTypeSystem;
import com.lineage.server.templates.L1WilliamGfxIdOrginal;
import com.lineage.server.templates.L1WilliamTypeIdOrginal;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1AttackPc extends L1AttackMode {
    private static final Log _log = LogFactory.getLog(L1AttackPc.class);
    private byte _attackType = 0;

    public L1AttackPc(L1PcInstance attacker, L1Character target) {
        if (target != null && !target.isDead()) {
            this._pc = attacker;
            if (target instanceof L1PcInstance) {
                this._targetPc = (L1PcInstance) target;
                this._calcType = 1;
            } else if (target instanceof L1NpcInstance) {
                this._targetNpc = (L1NpcInstance) target;
                this._calcType = 2;
            }
            this._weapon = this._pc.getWeapon();
            if (this._weapon != null) {
                this._weaponId = this._weapon.getItem().getItemId();
                this._weaponType = this._weapon.getItem().getType1();
                this._weaponType2 = this._weapon.getItem().getType();
                this._weaponAddHit = this._weapon.getItem().getHitModifier() + this._weapon.getHitByMagic();
                this._weaponAddDmg = this._weapon.getItem().getDmgModifier() + this._weapon.getDmgByMagic();
                this._weaponSmall = this._weapon.getItem().getDmgSmall();
                this._weaponLarge = this._weapon.getItem().getDmgLarge();
                this._weaponRange = this._weapon.getItem().getRange();
                this._weaponBless = this._weapon.getItem().getBless();
                if (this._weaponType == 20 || this._weaponType == 62) {
                    this._weaponEnchant = this._weapon.getEnchantLevel();
                } else {
                    this._weaponEnchant = this._weapon.getEnchantLevel() - this._weapon.get_durability();
                }
                this._weaponMaterial = this._weapon.getItem().getMaterial();
                if (this._weaponType == 20) {
                    this._arrow = this._pc.getInventory().getArrow();
                    if (this._arrow != null) {
                        this._weaponBless = this._arrow.getItem().getBless();
                        this._weaponMaterial = this._arrow.getItem().getMaterial();
                    }
                }
                if (this._weaponType == 62) {
                    this._sting = this._pc.getInventory().getSting();
                    if (this._sting != null) {
                        this._weaponBless = this._sting.getItem().getBless();
                        this._weaponMaterial = this._sting.getItem().getMaterial();
                    }
                }
                this._weaponDoubleDmgChance = this._weapon.getItem().getDoubleDmgChance();
                this._weaponAttrEnchantKind = this._weapon.getAttrEnchantKind();
                this._weaponAttrEnchantLevel = this._weapon.getAttrEnchantLevel();
            }
            if (this._weaponType == 20) {
                Integer dmg = L1AttackList.DEXD.get(Integer.valueOf(this._pc.getDex()));
                if (dmg != null) {
                    this._statusDamage = dmg.intValue();
                }
            } else {
                Integer dmg2 = L1AttackList.STRD.get(Integer.valueOf(this._pc.getStr()));
                if (dmg2 != null) {
                    this._statusDamage = dmg2.intValue();
                }
            }
            this._target = target;
            this._targetId = target.getId();
            this._targetX = target.getX();
            this._targetY = target.getY();
        }
    }

    @Override // com.lineage.server.model.L1AttackMode
    public boolean calcHit() {
        if (this._target == null) {
            this._isHit = false;
            return this._isHit;
        }
        if (this._weaponRange != -1) {
            if (this._pc.getLocation().getTileLineDistance(this._target.getLocation()) > this._weaponRange + 1) {
                this._isHit = false;
                return this._isHit;
            }
        } else if (!this._pc.getLocation().isInScreen(this._target.getLocation())) {
            this._isHit = false;
            return this._isHit;
        }
        if (this._weaponType == 20 && this._weaponId != 190 && this._weaponId != 100190 && this._arrow == null) {
            this._isHit = false;
        } else if (this._weaponType == 62 && this._sting == null) {
            this._isHit = false;
        } else if (!this._pc.glanceCheck(this._targetX, this._targetY)) {
            this._isHit = false;
        } else if (this._weaponId == 247 || this._weaponId == 248 || this._weaponId == 249) {
            this._isHit = false;
        } else if (this._calcType == 1) {
            this._isHit = calcPcHit();
        } else if (this._calcType == 2) {
            this._isHit = calcNpcHit();
        }
        return this._isHit;
    }

    private int str_dex_Hit() {
        int hitRate;
        Integer hitStr = L1AttackList.STRH.get(Integer.valueOf(this._pc.getStr() - 1));
        if (hitStr != null) {
            hitRate = 0 + hitStr.intValue();
        } else {
            hitRate = 0 + 19;
        }
        Integer hitDex = L1AttackList.DEXH.get(Integer.valueOf(this._pc.getDex() - 1));
        if (hitDex != null) {
            return hitRate + hitDex.intValue();
        }
        return hitRate + 29;
    }

    private boolean calcPcHit() {
        if (this._targetPc == null || dmg0(this._targetPc) || calcEvasion()) {
            return false;
        }
        if (this._weaponType2 == 17) {
            return true;
        }
        this._hitRate = this._pc.getLevel();
        this._hitRate += str_dex_Hit();
        if (this._weaponType == 20 || this._weaponType == 62) {
            this._hitRate = (int) (((double) this._hitRate) + ((double) (this._weaponAddHit + this._pc.getBowHitup() + this._pc.getOriginalBowHitup())) + (((double) this._weaponEnchant) * 0.6d));
        } else {
            this._hitRate = (int) (((double) this._hitRate) + ((double) (this._weaponAddHit + this._pc.getHitup() + this._pc.getOriginalHitup())) + (((double) this._weaponEnchant) * 0.6d));
        }
        if (this._weaponType == 20 || this._weaponType == 62) {
            this._hitRate += this._pc.getBowHitModifierByArmor();
        } else {
            this._hitRate += this._pc.getHitModifierByArmor();
        }
        int weight240 = this._pc.getInventory().getWeight240();
        if (weight240 > 80) {
            if (80 < weight240 && 120 >= weight240) {
                this._hitRate--;
            } else if (121 <= weight240 && 160 >= weight240) {
                this._hitRate -= 3;
            } else if (161 <= weight240 && 200 >= weight240) {
                this._hitRate -= 5;
            }
        }
        this._hitRate += hitUp();
        int attackerDice = (((_random.nextInt(20) + 2) + this._hitRate) - 10) + attackerDice(this._targetPc);
        int defenderDice = 0;
        int defenderValue = ((int) (((double) this._targetPc.getAc()) * 1.5d)) * -1;
        if (this._targetPc.getAc() >= 0) {
            defenderDice = 10 - this._targetPc.getAc();
        } else if (this._targetPc.getAc() < 0) {
            defenderDice = _random.nextInt(defenderValue) + 10 + 1;
        }
        int fumble = this._hitRate - 9;
        int critical = this._hitRate + 10;
        if (this._pc.isDragonKnight()) {
            attackerDice = (int) (((double) attackerDice) * 1.01d);
        }
        if (this._pc.isElf() && this._pc.getElfAttr() == 2) {
            attackerDice = (int) (((double) attackerDice) * 1.02d);
        }
        if (attackerDice <= fumble) {
            this._hitRate = 15;
        } else if (attackerDice >= critical) {
            this._hitRate = 100;
        } else if (attackerDice > defenderDice) {
            this._hitRate = 100;
        } else if (attackerDice <= defenderDice) {
            this._hitRate = 15;
        }
        int rnd = _random.nextInt(100) + 1;
        if (this._weaponType != 20 || this._hitRate <= rnd) {
            return this._hitRate >= rnd;
        }
        return calcErEvasion();
    }

    private boolean calcNpcHit() {
        switch (this._targetNpc.getNpcTemplate().get_gfxid()) {
            case 2412:
                if (!this._pc.getInventory().checkEquipped(20046)) {
                    return false;
                }
                break;
        }
        if (dmg0(this._targetNpc)) {
            return false;
        }
        if (this._weaponType2 == 17) {
            return true;
        }
        this._hitRate = this._pc.getLevel();
        this._hitRate += str_dex_Hit();
        if (this._weaponType == 20 || this._weaponType == 62) {
            this._hitRate = (int) (((double) this._hitRate) + ((double) (this._weaponAddHit + this._pc.getBowHitup() + this._pc.getOriginalBowHitup())) + (((double) this._weaponEnchant) * 0.6d));
        } else {
            this._hitRate = (int) (((double) this._hitRate) + ((double) (this._weaponAddHit + this._pc.getHitup() + this._pc.getOriginalHitup())) + (((double) this._weaponEnchant) * 0.6d));
        }
        if (this._weaponType == 20 || this._weaponType == 62) {
            this._hitRate += this._pc.getBowHitModifierByArmor();
        } else {
            this._hitRate += this._pc.getHitModifierByArmor();
        }
        int weight240 = this._pc.getInventory().getWeight240();
        if (weight240 > 80) {
            if (80 < weight240 && 120 >= weight240) {
                this._hitRate--;
            } else if (121 <= weight240 && 160 >= weight240) {
                this._hitRate -= 3;
            } else if (161 <= weight240 && 200 >= weight240) {
                this._hitRate -= 5;
            }
        }
        this._hitRate += hitUp();
        int attackerDice = (((_random.nextInt(20) + 2) + this._hitRate) - 10) + attackerDice(this._targetNpc);
        int defenderDice = 10 - this._targetNpc.getAc();
        int fumble = this._hitRate - 9;
        int critical = this._hitRate + 10;
        if (this._pc.isDragonKnight()) {
            attackerDice = (int) (((double) attackerDice) * 1.01d);
        }
        if (this._pc.isElf() && this._pc.getElfAttr() == 2) {
            attackerDice = (int) (((double) attackerDice) * 1.02d);
        }
        if (attackerDice <= fumble) {
            this._hitRate = 15;
        } else if (attackerDice >= critical) {
            this._hitRate = 100;
        } else if (attackerDice > defenderDice) {
            this._hitRate = 100;
        } else if (attackerDice <= defenderDice) {
            this._hitRate = 15;
        }
        int npcId = this._targetNpc.getNpcTemplate().get_npcId();
        Integer tgskill = L1AttackList.SKNPC.get(Integer.valueOf(npcId));
        if (tgskill != null && !this._pc.hasSkillEffect(tgskill.intValue())) {
            this._hitRate = 0;
        }
        Integer tgpoly = L1AttackList.PLNPC.get(Integer.valueOf(npcId));
        if (tgpoly != null && tgpoly.equals(Integer.valueOf(this._pc.getTempCharGfx()))) {
            this._hitRate = 0;
        }
        if (this._hitRate >= _random.nextInt(100) + 1) {
            return true;
        }
        return false;
    }

    private int hitUp() {
        int hitUp = 0;
        if (this._pc.getSkillEffect().size() <= 0) {
            return 0;
        }
        if (!this._pc.getSkillisEmpty()) {
            try {
                if (this._weaponType == 20 || this._weaponType == 62) {
                    for (Integer key : this._pc.getSkillEffect()) {
                        Integer integer = L1AttackList.SKU2.get(key);
                        if (integer != null) {
                            hitUp += integer.intValue();
                        }
                    }
                } else {
                    for (Integer key2 : this._pc.getSkillEffect()) {
                        Integer integer2 = L1AttackList.SKU1.get(key2);
                        if (integer2 != null) {
                            hitUp += integer2.intValue();
                        }
                    }
                }
            } catch (ConcurrentModificationException ignored) {
            } catch (Exception e2) {
                _log.error(e2.getLocalizedMessage(), e2);
            }
        }
        return hitUp;
    }

    @Override // com.lineage.server.model.L1AttackMode
    public int calcDamage() throws Exception {
        switch (this._calcType) {
            case 1:
                this._damage = calcPcDamage();
                break;
            case 2:
                this._damage = calcNpcDamage();
                break;
        }
        return this._damage;
    }

    private int weaponDamage1(int weaponMaxDamage) {
        int weaponDamage = 0;
        switch (this._weaponType2) {
            case 1:
            case 2:
            case 3:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 12:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
                weaponDamage = _random.nextInt(weaponMaxDamage) + 1;
                break;
            case 11:
                if (_random.nextInt(100) + 1 <= this._weaponDoubleDmgChance) {
                    weaponDamage = weaponMaxDamage;
                    this._attackType = 2;
                    break;
                }
                break;
        }
        if (this._pc.getClanid() != 0) {
            weaponDamage = (int) (((double) weaponDamage) + getDamageUpByClan(this._pc));
        }
        switch (this._pc.guardianEncounter()) {
            case 3:
                return weaponDamage + 1;
            case 4:
                return weaponDamage + 3;
            case 5:
                return weaponDamage + 5;
            default:
                return weaponDamage;
        }
    }

    private double weaponDamage2(double weaponTotalDamage) {
        int add_dmg;
        double dmg = 0.0d;
        boolean doubleBrake = false;
        switch (this._weaponType2) {
            case 0:
                dmg = (double) ((_random.nextInt(5) + 4) >> 2);
                break;
            case 1:
            case 2:
            case 3:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 14:
            case 15:
            case 16:
            case 18:
                dmg = ((double) this._statusDamage) + weaponTotalDamage + ((double) this._pc.getDmgup()) + ((double) this._pc.getOriginalDmgup());
                break;
            case 4:
            case 13:
                double add = (double) this._statusDamage;
                switch (this._calcType) {
                    case 1:
                        add *= 2.5d;
                        break;
                    case 2:
                        add *= 1.5d;
                        break;
                }
                dmg = weaponTotalDamage + add + ((double) this._pc.getBowDmgup()) + ((double) this._pc.getOriginalBowDmgup());
                if (this._arrow == null) {
                    if (this._weaponId == 190 || this._weaponId == 100190) {
                        dmg = ((double) _random.nextInt(15)) + dmg + 1.0d;
                        break;
                    }
                } else {
                    dmg = ((double) _random.nextInt(Math.max(this._arrow.getItem().getDmgSmall(), 1))) + dmg + 1.0d;
                    break;
                }
            case 10:
                dmg = ((double) this._statusDamage) + weaponTotalDamage + ((double) this._pc.getBowDmgup()) + ((double) this._pc.getOriginalBowDmgup());
                if (this._sting != null) {
                    dmg += (double) (_random.nextInt(Math.max(this._sting.getItem().getDmgSmall(), 1)) + 1);
                    break;
                }
                break;
            case 11:
                if (this._pc.hasSkillEffect(105)) {
                    doubleBrake = true;
                }
                dmg = ((double) this._statusDamage) + weaponTotalDamage + ((double) this._pc.getDmgup()) + ((double) this._pc.getOriginalDmgup()) + 5.0d;
                break;
            case 12:
                if (_random.nextInt(100) + 1 <= this._weaponDoubleDmgChance) {
                    this._attackType = 4;
                }
                if (this._pc.hasSkillEffect(105)) {
                    doubleBrake = true;
                }
                dmg = ((double) this._statusDamage) + weaponTotalDamage + ((double) this._pc.getDmgup()) + ((double) this._pc.getOriginalDmgup());
                break;
            case 17:
                dmg = 0.0d + weaponTotalDamage + L1WeaponSkill.getKiringkuDamage(this._pc, this._target);
                break;
        }
        if (doubleBrake && _random.nextInt(100) + 1 <= 23) {
            dmg *= 1.8d;
        }
        if (this._weaponType2 == 0 || (add_dmg = this._weapon.getItem().get_add_dmg()) == 0) {
            return dmg;
        }
        return dmg + ((double) add_dmg);
    }

    private double pcDmgMode(double dmg, double weaponTotalDamage) throws Exception {
        double dmg2;
        double dmg3 = calcBuffDamage(dmg) + weaponSkill(this._pc, this._target, weaponTotalDamage);
        addPcPoisonAttack(this._target);
        if (this._weaponType == 20 || this._weaponType == 62) {
            dmg2 = dmg3 + ((double) this._pc.getBowDmgModifierByArmor());
        } else {
            dmg2 = dmg3 + ((double) this._pc.getDmgModifierByArmor());
        }
        double dmg4 = dmg2 + dmgUp() + ((double) dk_dmgUp());
        switch (this._pc.get_weaknss()) {
            case 1:
            case 2:
                if (!this._pc.isFoeSlayer()) {
                    return dmg4;
                }
                this._pc.set_weaknss(0, 0);
                this._pc.sendPackets(new S_PacketBoxDk(0));
                return dmg4;
            case 3:
                if (!this._pc.isFoeSlayer()) {
                    return dmg4;
                }
                this._pc.set_weaknss(0, 0);
                this._pc.sendPackets(new S_PacketBoxDk(0));
                return dmg4 * 1.3d;
            default:
                return dmg4;
        }
    }

    public int calcPcDamage() throws Exception {
        int p;
        if (this._targetPc == null) {
            return 0;
        }
        if (dmg0(this._targetPc)) {
            this._isHit = false;
            this._drainHp = 0;
            return 0;
        } else if (!this._isHit) {
            return 0;
        } else {
            double weaponTotalDamage = ((double) (this._weaponAddDmg + weaponDamage1(this._weaponSmall) + this._weaponEnchant)) + ((double) calcAttrEnchantDmg());
            double dmg = ((pcDmgMode(weaponDamage2(weaponTotalDamage), weaponTotalDamage) - ((double) calcPcDefense())) - ((double) this._targetPc.getDamageReductionByArmor())) - ((double) this._targetPc.dmgDowe());
            if (this._weapon != null && FeatureItemSet.POWER_START) {
                dmg = (double) new L1AttackPower(this._pc, this._target, this._weaponAttrEnchantKind, this._weaponAttrEnchantLevel).set_item_power((int) dmg);
            }
            if (this._targetPc.getClanid() != 0) {
                dmg -= getDamageReductionByClan(this._targetPc);
            }
            if (this._targetPc.hasSkillEffect(88)) {
                dmg -= (double) (((Math.max(this._targetPc.getLevel(), 50) - 50) / 5) + 10);
            }
            L1WeaponTypeSystem Enchant = WeaponTypeSystem.get(this._weaponType2);
            if (Enchant != null) {
                if (Enchant.getProbability() <= _random.nextInt(L1SkillId.STATUS_BRAVE)) {
                    return (int) dmg;
                }
                if (Enchant.getDmg() > 0.0d) {
                    dmg *= Enchant.getDmg();
                }
                if (this._targetPc != null) {
                    this._pc.sendPacketsAll(new S_SkillSound(this._targetPc.getId(), Enchant.getgfxid()));
                }
            }
            if (this._targetPc.hasSkillEffect(112)) {
                int n = ConfigDarkElfSkill.ARMOR_BRAKE0;
                int rnd = _random.nextInt(100) + 1;
                if (this._pc.getLevel() < this._targetPc.getLevel()) {
                    p = n - ((this._targetPc.getLevel() - this._pc.getLevel()) * ConfigDarkElfSkill.ARMOR_BRAKE1);
                } else if (this._pc.getLevel() > this._targetPc.getLevel()) {
                    p = n + ((this._pc.getLevel() - this._targetPc.getLevel()) * ConfigDarkElfSkill.ARMOR_BRAKE2);
                } else {
                    p = ConfigDarkElfSkill.ARMOR_BRAKE3;
                }
                if (p < 0) {
                    p = 0;
                }
                if (rnd <= p) {
                    dmg *= ConfigDarkElfSkill.ARMOR_BRAKE_DMG;
                }
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
                                dmg += (double) integer.intValue();
                            }
                        }
                    }
                } catch (ConcurrentModificationException ignored) {
                } catch (Exception e2) {
                    _log.error(e2.getLocalizedMessage(), e2);
                }
            }
            if (dmgX2) {
                dmg /= 2.0d;
            }
            if (!this._pc.getDolls().isEmpty()) {
                for (L1DollInstance doll : this._pc.getDolls().values()) {
                    doll.startDollSkill(this._targetPc, dmg);
                }
            }
            if (this._attackType == 4) {
                dmg *= 1.8d;
            }
            double dmg2 = dmg * coatArms();
            if (!this._isHit) {
                dmg2 = 0.0d;
            }
            if (dmg2 <= 0.0d) {
                this._isHit = false;
                this._drainHp = 0;
            }
            return (int) dmg2;
        }
    }

    private int calcNpcDamage() throws Exception {
        L1WilliamTypeIdOrginal gfxIdOrginal;
        if (this._targetNpc == null) {
            return 0;
        }
        if (dmg0(this._targetNpc)) {
            this._isHit = false;
            this._drainHp = 0;
            return 0;
        } else if (!this._isHit) {
            return 0;
        } else {
            int weaponMaxDamage = 0;
            if (!this._targetNpc.getNpcTemplate().isSmall() || this._weaponSmall <= 0) {
                if (!this._targetNpc.getNpcTemplate().isLarge() || this._weaponLarge <= 0) {
                    if (this._weaponSmall > 0) {
                        weaponMaxDamage = this._weaponSmall;
                    }
                } else if (this._weaponLarge > 0) {
                    weaponMaxDamage = this._weaponLarge;
                }
            } else if (this._weaponSmall > 0) {
                weaponMaxDamage = this._weaponSmall;
            }
            int weaponDamage = weaponDamage1(weaponMaxDamage);
            if (this._targetNpc.hasSkillEffect(L1SkillId.MAZU_SKILL)) {
                weaponDamage++;
            }
            double weaponTotalDamage = ((double) (this._weaponAddDmg + weaponDamage + this._weaponEnchant)) + ((double) calcMaterialBlessDmg()) + ((double) calcAttrEnchantDmg());
            double dmg = pcDmgMode(weaponDamage2(weaponTotalDamage), weaponTotalDamage) - ((double) calcNpcDamageReduction());
            if (this._weapon != null && FeatureItemSet.POWER_START) {
                dmg = (double) new L1AttackPower(this._pc, this._target, this._weaponAttrEnchantKind, this._weaponAttrEnchantLevel).set_item_power((int) dmg);
            }
            boolean isNowWar = false;
            int castleId = L1CastleLocation.getCastleIdByArea(this._targetNpc);
            if (castleId > 0) {
                isNowWar = ServerWarExecutor.get().isNowWar(castleId);
            }
            if (!isNowWar) {
                if (this._targetNpc instanceof L1PetInstance) {
                    dmg /= 8.0d;
                }
                if ((this._targetNpc instanceof L1SummonInstance) && ((L1SummonInstance) this._targetNpc).isExsistMaster()) {
                    dmg /= 8.0d;
                }
            }
            if (TypeId_Orginal.MODE && (gfxIdOrginal = TypeIdOrginal.get().getTemplate(this._pc.getType())) != null) {
                dmg *= gfxIdOrginal.getDmg();
            }
            L1WeaponTypeSystem Enchant = WeaponTypeSystem.get(this._weaponType2);
            if (Enchant != null) {
                if (Enchant.getProbability() <= _random.nextInt(L1SkillId.STATUS_BRAVE)) {
                    return (int) dmg;
                }
                if (Enchant.getDmg() > 0.0d) {
                    dmg *= Enchant.getDmg();
                }
                if (this._targetNpc != null) {
                    this._pc.sendPacketsAll(new S_SkillSound(this._targetNpc.getId(), Enchant.getgfxid()));
                }
            }
            if (!this._pc.getDolls().isEmpty()) {
                for (L1DollInstance doll : this._pc.getDolls().values()) {
                    doll.startDollSkill(this._targetNpc, dmg);
                }
            }
            if (this._attackType == 4) {
                dmg *= 1.8d;
            }
            if (!this._isHit) {
                dmg = 0.0d;
            }
            if (dmg <= 0.0d) {
                this._isHit = false;
                this._drainHp = 0;
            }
            if (((double) this._targetNpc.getCurrentHp()) <= dmg && this._pc.getInventory().checkItem(L1ItemId.ADENA, 500000000)) {
                this._pc.getInventory().storeItem(51005, 1);
                this._pc.sendPackets(new S_SystemMessage("金幣太多系統幫助【" + this._pc.getName() + "】銀行支票。"));
                this._pc.getInventory().consumeItem(L1ItemId.ADENA, 500000000);
            }
            return (int) dmg;
        }
    }

    private int dk_dmgUp() {
        int dmg = 0;
        if (this._pc.isDragonKnight() && this._weaponType2 == 18) {
            long h_time = Calendar.getInstance().getTimeInMillis() / 1000;
            switch (this._pc.get_weaknss()) {
                case 0:
                    if (!this._pc.isFoeSlayer()) {
                        if (_random.nextInt(100) < 20) {
                            this._pc.set_weaknss(1, h_time);
                            this._pc.sendPackets(new S_PacketBoxDk(1));
                            dmg = 0 + 20;
                            break;
                        }
                    } else {
                        return 0;
                    }
                    break;
                case 1:
                    if (!this._pc.isFoeSlayer()) {
                        if (_random.nextInt(100) >= 60) {
                            this._pc.set_weaknss(2, h_time);
                            this._pc.sendPackets(new S_PacketBoxDk(2));
                            dmg = 0 + 40;
                            break;
                        } else {
                            this._pc.set_weaknss(1, h_time);
                            this._pc.sendPackets(new S_PacketBoxDk(1));
                            dmg = 0 + 20;
                            break;
                        }
                    } else {
                        return 0;
                    }
                case 2:
                    if (!this._pc.isFoeSlayer()) {
                        if (_random.nextInt(100) >= 60) {
                            this._pc.set_weaknss(3, h_time);
                            this._pc.sendPackets(new S_PacketBoxDk(3));
                            dmg = 0 + 60;
                            break;
                        } else {
                            this._pc.set_weaknss(2, h_time);
                            this._pc.sendPackets(new S_PacketBoxDk(2));
                            dmg = 0 + 40;
                            break;
                        }
                    } else {
                        return 0;
                    }
                case 3:
                    if (!this._pc.isFoeSlayer()) {
                        if (_random.nextInt(100) < 20) {
                            this._pc.set_weaknss(3, h_time);
                            this._pc.sendPackets(new S_PacketBoxDk(3));
                            dmg = 0 + 60;
                            break;
                        }
                    } else {
                        return 0;
                    }
                    break;
            }
        }
        return dmg;
    }

    private double dmgUp() {
        HashMap<Integer, Integer> skills;
        double dmg = 0.0d;
        if (this._pc.getSkillEffect().size() <= 0) {
            return 0.0d;
        }
        if (!this._pc.getSkillisEmpty()) {
            try {
                switch (this._weaponType) {
                    case 20:
                    case 62:
                        skills = L1AttackList.SKD2;
                        break;
                    default:
                        skills = L1AttackList.SKD1;
                        break;
                }
                if (skills != null) {
                    for (Integer key : this._pc.getSkillEffect()) {
                        Integer integer = L1AttackList.SKD2.get(key);
                        if (integer != null) {
                            dmg += (double) integer.intValue();
                        }
                    }
                }
            } catch (ConcurrentModificationException ignored) {
            } catch (Exception e2) {
                _log.error(e2.getLocalizedMessage(), e2);
            }
        }
        return dmg + ((double) this._pc.dmgAdd());
    }

    private double weaponSkill(L1PcInstance pcInstance, L1Character character, double weaponTotalDamage) throws Exception {
        switch (this._weaponId) {
            case 2:
            case 200002:
                if (this._targetPc != null) {
                    return L1WeaponSkill.getDiceDaggerDamage(this._pc, this._targetPc, this._weapon);
                }
                return 0.0d;
            case 124:
            case 100124:
                return L1WeaponSkill.getBaphometStaffDamage(this._pc, this._target);
            case 204:
            case 100204:
                L1WeaponSkill.giveFettersEffect(this._pc, this._target);
                return 0.0d;
            case 260:
            case 263:
            case 100260:
            case 100263:
                return L1WeaponSkill.getAreaSkillWeaponDamage(this._pc, this._target, this._weaponId);
            case 261:
            case 100261:
                L1WeaponSkill.giveArkMageDiseaseEffect(this._pc, this._target);
                return 0.0d;
            case 262:
            case 100262:
                if (_random.nextInt(110) + 1 <= 75) {
                    return calcDestruction(weaponTotalDamage);
                }
                return 0.0d;
            case 264:
            case 100264:
                return L1WeaponSkill.getLightningEdgeDamage(this._pc, this._target);
            case 265:
            case 266:
            case 267:
            case 268:
            case 100265:
            case 100266:
            case 100267:
            case 100268:
                return 0.0d;
            default:
                return L1WeaponSkill.getWeaponSkillDamage(this._pc, this._target, this._weaponId);
        }
    }

    private double calcBuffDamage(double dmg) {
        if (this._weaponType == 20) {
            return dmg;
        }
        if (this._weaponType == 62) {
            return dmg;
        }
        if (this._weaponType2 == 17) {
            return dmg;
        }
        boolean isDmgUp = false;
        double dmgDouble = 1.0d;
        int random = _random.nextInt(100) + 1;
        if (this._pc.hasSkillEffect(L1SkillId.ELEMENTAL_FIRE) && random <= 33) {
            isDmgUp = true;
            dmgDouble = 1.0d + 0.6d;
        }
        if (this._pc.hasSkillEffect(L1SkillId.BURNING_SPIRIT) && random <= 33) {
            isDmgUp = true;
            dmgDouble += 0.4d;
        }
        if (this._pc.hasSkillEffect(L1SkillId.SOUL_OF_FLAME) && random <= 33) {
            isDmgUp = true;
            dmgDouble += 0.7d;
        }
        if (isDmgUp) {
            double tempDmg = dmg;
            if (this._pc.hasSkillEffect(148)) {
                tempDmg -= 4.0d;
            }
            if (this._pc.hasSkillEffect(155)) {
                tempDmg -= 4.0d;
            }
            if (this._pc.hasSkillEffect(L1SkillId.BURNING_WEAPON)) {
                tempDmg -= 6.0d;
            }
            if (this._pc.hasSkillEffect(55)) {
                tempDmg -= 5.0d;
            }
            dmg = (tempDmg * dmgDouble) + (dmg - tempDmg);
        }
        return dmg;
    }

    private int calcMaterialBlessDmg() {
        int damage = 0;
        if (this._pc.getWeapon() == null) {
            return 0;
        }
        switch (this._targetNpc.getNpcTemplate().get_undead()) {
            case 1:
                if (this._weaponMaterial == 14 || this._weaponMaterial == 17 || this._weaponMaterial == 22) {
                    damage = 0 + _random.nextInt(20) + 1;
                }
                if (this._weaponBless == 0) {
                    damage += _random.nextInt(4) + 1;
                }
                switch (this._weaponType) {
                    case 20:
                    case 62:
                        return damage;
                    default:
                        if (this._weapon.getHolyDmgByMagic() != 0) {
                            return damage + this._weapon.getHolyDmgByMagic();
                        }
                        return damage;
                }
            case 2:
                if (this._weaponMaterial == 17 || this._weaponMaterial == 22) {
                    damage = 0 + _random.nextInt(3) + 1;
                }
                if (this._weaponBless == 0) {
                    return damage + _random.nextInt(4) + 1;
                }
                return damage;
            case 3:
                if (this._weaponMaterial == 14 || this._weaponMaterial == 17 || this._weaponMaterial == 22) {
                    damage = 0 + _random.nextInt(20) + 1;
                }
                if (this._weaponBless == 0) {
                    damage += _random.nextInt(4) + 1;
                }
                switch (this._weaponType) {
                    case 20:
                    case 62:
                        return damage;
                    default:
                        if (this._weapon.getHolyDmgByMagic() != 0) {
                            return damage + this._weapon.getHolyDmgByMagic();
                        }
                        return damage;
                }
            case 4:
            default:
                return 0;
            case 5:
                if (this._weaponMaterial == 14 || this._weaponMaterial == 17 || this._weaponMaterial == 22) {
                    return 0 + _random.nextInt(20) + 1;
                }
                return 0;
        }
    }

    private int calcAttrEnchantDmg() {
        int damage = 0;
        switch (this._weaponAttrEnchantLevel) {
            case 1:
                damage = 1;
                break;
            case 2:
                damage = 3;
                break;
            case 3:
                damage = 5;
                break;
        }
        int resist = 0;
        switch (this._calcType) {
            case 1:
                switch (this._weaponAttrEnchantKind) {
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
                    case 16:
                        resist = this._targetPc.getEarth();
                        break;
                    case 32:
                        resist = this._targetPc.getFire();
                        break;
                    case 64:
                        resist = this._targetPc.getWater();
                        break;
                    case 128:
                        resist = this._targetPc.getWind();
                        break;
                }
            case 2:
                switch (this._weaponAttrEnchantKind) {
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
                    case 16:
                        resist = this._targetNpc.getEarth();
                        break;
                    case 32:
                        resist = this._targetNpc.getFire();
                        break;
                    case 64:
                        resist = this._targetNpc.getWater();
                        break;
                    case 128:
                        resist = this._targetNpc.getWind();
                        break;
                }
        }
        int resistFloor = (int) (0.32d * ((double) Math.abs(resist)));
        if (resist < 0) {
            resistFloor *= -1;
        }
        return (int) (((double) damage) * (1.0d - (((double) resistFloor) / 32.0d)));
    }

    @Override // com.lineage.server.model.L1AttackMode
    public void calcStaffOfMana() {
        switch (this._weaponId) {
            case 126:
            case 127:
            case 100126:
            case 100127:
                int som_lvl = this._weaponEnchant + 3;
                if (som_lvl < 0) {
                    som_lvl = 0;
                }
                this._drainMana = Math.min(_random.nextInt(som_lvl) + 1, 9);
                return;
            case 259:
            case 100259:
                switch (this._calcType) {
                    case 1:
                        if (this._targetPc.getMr() <= _random.nextInt(100) + 1) {
                            this._drainMana = 1;
                            return;
                        }
                        return;
                    case 2:
                        if (this._targetNpc.getMr() <= _random.nextInt(100) + 1) {
                            this._drainMana = 1;
                            return;
                        }
                        return;
                    default:
                        return;
                }
            default:
                return;
        }
    }

    private double calcDestruction(double dmg) {
        this._drainHp = Math.max((int) ((dmg / 8.0d) + 1.0d), 1);
        return (double) this._drainHp;
    }

    private void addPcPoisonAttack(L1Character target) {
        boolean isCheck = false;
        switch (this._weaponId) {
            case 0:
                break;
            case 13:
            case 14:
                isCheck = true;
                break;
            default:
                if (this._pc.hasSkillEffect(98)) {
                    isCheck = true;
                    break;
                }
                break;
        }
        if (isCheck && _random.nextInt(100) + 1 <= 10) {
            L1DamagePoison.doInfection(this._pc, target, 3000, 5);
        }
    }

    @Override // com.lineage.server.model.L1AttackMode
    public void addChaserAttack() {
        int mr = 0;
        switch (this._calcType) {
            case 1:
                mr = this._targetPc.getMr() - (this._pc.getOriginalMagicHit() * 2);
                break;
            case 2:
                mr = this._targetNpc.getMr() - (this._pc.getOriginalMagicHit() * 2);
                break;
        }
        double probability = (3.0d + (((double) this._pc.getTrueSp()) * 0.25d)) - (((double) (mr / 10)) * 0.1d);
        switch (this._weaponId) {
            case 265:
            case 266:
            case 267:
            case 268:
            case 100265:
            case 100266:
            case 100267:
            case 100268:
                if (probability / 1.1d > ((double) (_random.nextInt(100) + 1))) {
                    new L1Chaser(this._pc, this._target).begin();
                    return;
                }
                return;
            default:
                return;
        }
    }

    @Override // com.lineage.server.model.L1AttackMode
    public void action() {
        try {
            if (this._pc != null && this._target != null) {
                this._pc.setHeading(this._pc.targetDirection(this._targetX, this._targetY));
                if (this._weaponRange == -1) {
                    actionX1();
                } else {
                    actionX2();
                }
                int i = ((int) ((((double) this._damage) / Math.pow(10.0d, 0.0d)) % 10.0d)) + 11821;
                int k = ((int) ((((double) this._damage) / Math.pow(10.0d, 1.0d)) % 10.0d)) + 11831;
                int h = ((int) ((((double) this._damage) / Math.pow(10.0d, 2.0d)) % 10.0d)) + 11841;
                int s = ((int) ((((double) this._damage) / Math.pow(10.0d, 3.0d)) % 10.0d)) + 11851;
                int m = ((int) ((((double) this._damage) / Math.pow(10.0d, 4.0d)) % 10.0d)) + 11861;
                if (!this._pc.is_send_weapon_dmg_gfxid()) {
                    return;
                }
                if (this._damage <= 0) {
                    this._pc.sendPackets(new S_SkillSound(this._target.getId(), 11871));
                } else if (this._damage > 0 && this._damage < 10) {
                    this._pc.sendPackets(new S_SkillSound(this._target.getId(), i));
                } else if (this._damage >= 10 && this._damage < 100) {
                    this._pc.sendPackets(new S_SkillSound(this._target.getId(), i));
                    this._pc.sendPackets(new S_SkillSound(this._target.getId(), k));
                } else if (this._damage >= 100 && this._damage < 1000) {
                    this._pc.sendPackets(new S_SkillSound(this._target.getId(), i));
                    this._pc.sendPackets(new S_SkillSound(this._target.getId(), k));
                    this._pc.sendPackets(new S_SkillSound(this._target.getId(), h));
                } else if (this._damage >= 1000 && this._damage < 10000) {
                    this._pc.sendPackets(new S_SkillSound(this._target.getId(), i));
                    this._pc.sendPackets(new S_SkillSound(this._target.getId(), k));
                    this._pc.sendPackets(new S_SkillSound(this._target.getId(), h));
                    this._pc.sendPackets(new S_SkillSound(this._target.getId(), s));
                } else if (this._damage >= 10000) {
                    this._pc.sendPackets(new S_SkillSound(this._target.getId(), i));
                    this._pc.sendPackets(new S_SkillSound(this._target.getId(), k));
                    this._pc.sendPackets(new S_SkillSound(this._target.getId(), h));
                    this._pc.sendPackets(new S_SkillSound(this._target.getId(), s));
                    this._pc.sendPackets(new S_SkillSound(this._target.getId(), m));
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void actionX2() {
        try {
            if (this._isHit) {
                this._pc.sendPacketsX10(new S_AttackPacketPc(this._pc, this._target, this._attackType, this._damage));
            } else if (this._targetId > 0) {
                this._pc.sendPacketsX10(new S_AttackPacketPc(this._pc, this._target));
            } else {
                this._pc.sendPacketsAll(new S_AttackPacketPc(this._pc));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void actionX1() {
        try {
            switch (this._weaponType) {
                case 20:
                    switch (this._weaponId) {
                        case 190:
                        case 100190:
                            if (this._arrow != null) {
                                this._pc.getInventory().removeItem(this._arrow, 1);
                            }
                            this._pc.sendPacketsX10(new S_UseArrowSkill(this._pc, this._targetId, 2349, this._targetX, this._targetY, this._damage));
                            return;
                        default:
                            if (this._arrow != null) {
                                L1WilliamGfxIdOrginal gfxIdOrginal = GfxIdOrginal.get().getTemplate(this._pc.getTempCharGfx());
                                if (gfxIdOrginal != null) {
                                    this._arrowGfxid = gfxIdOrginal.getarrowGfxid();
                                }
                                this._pc.sendPacketsX10(new S_UseArrowSkill(this._pc, this._targetId, this._arrowGfxid, this._targetX, this._targetY, this._damage));
                                this._pc.getInventory().removeItem(this._arrow, 1);
                                return;
                            }
                            int aid = 1;
                            if (this._pc.getTempCharGfx() == 3860) {
                                aid = 21;
                            }
                            this._pc.sendPacketsAll(new S_ChangeHeading(this._pc));
                            this._pc.sendPacketsAll(new S_DoActionGFX(this._pc.getId(), aid));
                            return;
                    }
                case 62:
                    if (this._sting != null) {
                        L1WilliamGfxIdOrginal gfxIdOrginal2 = GfxIdOrginal.get().getTemplate(this._pc.getTempCharGfx());
                        if (gfxIdOrginal2 != null) {
                            this._stingGfxid = gfxIdOrginal2.getstingGfxid();
                        }
                        this._pc.sendPacketsX10(new S_UseArrowSkill(this._pc, this._targetId, this._stingGfxid, this._targetX, this._targetY, this._damage));
                        this._pc.getInventory().removeItem(this._sting, 1);
                        return;
                    }
                    int aid2 = 1;
                    if (this._pc.getTempCharGfx() == 3860) {
                        aid2 = 21;
                    }
                    this._pc.sendPacketsAll(new S_ChangeHeading(this._pc));
                    this._pc.sendPacketsAll(new S_DoActionGFX(this._pc.getId(), aid2));
                    return;
                default:
                    return;
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.L1AttackMode
    public void commit() throws Exception {
        if (this._isHit) {
            switch (this._calcType) {
                case 1:
                    commitPc();
                    break;
                case 2:
                    commitNpc();
                    break;
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
            String srcatk = this._pc.getName();
            switch (this._calcType) {
                case 1:
                    String x = String.valueOf(srcatk) + ">" + this._targetPc.getName() + " " + (this._isHit ? "傷害:" + this._damage : "失敗") + (" 命中:" + this._hitRate + "% 剩餘hp:" + this._targetPc.getCurrentHp());
                    if (this._pc.isGm()) {
                        this._pc.sendPackets(new S_ServerMessage(166, "對PC送出攻擊: " + x));
                    }
                    if (this._targetPc.isGm()) {
                        this._targetPc.sendPackets(new S_ServerMessage(166, "受到PC攻擊: " + x));
                        return;
                    }
                    return;
                case 2:
                    String x2 = String.valueOf(srcatk) + ">" + this._targetNpc.getName() + " " + (this._isHit ? "傷害:" + this._damage : "失敗") + (" 命中:" + this._hitRate + "% 剩餘hp:" + this._targetNpc.getCurrentHp());
                    if (this._pc.isGm()) {
                        this._pc.sendPackets(new S_ServerMessage(166, "對NPC送出攻擊: " + x2));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private void commitPc() throws Exception {
        if (this._drainMana > 0 && this._targetPc.getCurrentMp() > 0) {
            if (this._drainMana > this._targetPc.getCurrentMp()) {
                this._drainMana = this._targetPc.getCurrentMp();
            }
            this._targetPc.setCurrentMp( (this._targetPc.getCurrentMp() - this._drainMana));
            this._pc.setCurrentMp( (this._pc.getCurrentMp() + this._drainMana));
        }
        if (this._drainHp > 0) {
            this._pc.setCurrentHp( (this._pc.getCurrentHp() + this._drainHp));
        }
        damagePcWeaponDurability();
        this._targetPc.receiveDamage(this._pc, (double) this._damage, false, false);
    }

    private void commitNpc() throws Exception {
        if (this._drainMana > 0) {
            int drainValue = this._targetNpc.drainMana(this._drainMana);
            this._pc.setCurrentMp(this._pc.getCurrentMp() + drainValue);
            if (drainValue > 0) {
                this._targetNpc.setCurrentMpDirect(this._targetNpc.getCurrentMp() - drainValue);
            }
        }
        if (this._drainHp > 0) {
            this._pc.setCurrentHp( (this._pc.getCurrentHp() + this._drainHp));
        }
        damageNpcWeaponDurability();
        this._targetNpc.receiveDamage(this._pc, this._damage);
    }

    @Override // com.lineage.server.model.L1AttackMode
    public boolean isShortDistance() {
        if (this._weaponType == 20 || this._weaponType == 62) {
            return false;
        }
        return true;
    }

    @Override // com.lineage.server.model.L1AttackMode
    public void commitCounterBarrier() throws Exception {
        int damage = calcCounterBarrierDamage();
        if (damage != 0) {
            this._pc.sendPacketsAll(new S_DoActionGFX(this._pc.getId(), 2));
            this._pc.receiveDamage(this._target, (double) damage, false, true);
        }
    }

    private void damageNpcWeaponDurability() {
        if (this._calcType == 2 && this._targetNpc.getNpcTemplate().is_hard() && this._weaponType != 0 && this._weapon.getItem().get_canbedmg() != 0 && !this._pc.hasSkillEffect(L1SkillId.SOUL_OF_FLAME)) {
            int random = _random.nextInt(100) + 1;
            switch (this._weaponBless) {
                case 0:
                    if (random < 3) {
                        this._pc.sendPackets(new S_ServerMessage(268, this._weapon.getLogName()));
                        this._pc.getInventory().receiveDamage(this._weapon);
                        return;
                    }
                    return;
                case 1:
                case 2:
                    if (random < 10) {
                        this._pc.sendPackets(new S_ServerMessage(268, this._weapon.getLogName()));
                        this._pc.getInventory().receiveDamage(this._weapon);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private void damagePcWeaponDurability() {
        if (this._calcType == 1 && this._weaponType != 0 && this._weaponType != 20 && this._weaponType != 62 && this._targetPc.hasSkillEffect(89) && !this._pc.hasSkillEffect(L1SkillId.SOUL_OF_FLAME) && _random.nextInt(100) + 1 <= 10) {
            this._pc.sendPackets(new S_ServerMessage(268, this._weapon.getLogName()));
            this._pc.getInventory().receiveDamage(this._weapon);
        }
    }
}
