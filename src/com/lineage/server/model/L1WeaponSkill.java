package com.lineage.server.model;

import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.WeaponSkillTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_UseAttackSkill;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;
import java.util.Iterator;
import java.util.Random;

public class L1WeaponSkill {
    private static Random _random = new Random();
    private int _area;
    private int _attr;
    private int _effectId;
    private int _effectTarget;
    private int _fixDamage;
    private boolean _isArrowType;
    private int _probability;
    private int _randomDamage;
    private int _skillId;
    private int _skillTime;
    private int _weaponId;

    public L1WeaponSkill(int weaponId, int probability, int fixDamage, int randomDamage, int area, int skillId, int skillTime, int effectId, int effectTarget, boolean isArrowType, int attr) {
        this._weaponId = weaponId;
        this._probability = probability;
        this._fixDamage = fixDamage;
        this._randomDamage = randomDamage;
        this._area = area;
        this._skillId = skillId;
        this._skillTime = skillTime;
        this._effectId = effectId;
        this._effectTarget = effectTarget;
        this._isArrowType = isArrowType;
        this._attr = attr;
    }

    public int getWeaponId() {
        return this._weaponId;
    }

    public int getProbability() {
        return this._probability;
    }

    public int getFixDamage() {
        return this._fixDamage;
    }

    public int getRandomDamage() {
        return this._randomDamage;
    }

    public int getArea() {
        return this._area;
    }

    public int getSkillId() {
        return this._skillId;
    }

    public int getSkillTime() {
        return this._skillTime;
    }

    public int getEffectId() {
        return this._effectId;
    }

    public int getEffectTarget() {
        return this._effectTarget;
    }

    public boolean isArrowType() {
        return this._isArrowType;
    }

    public int getAttr() {
        return this._attr;
    }

    public static double getWeaponSkillDamage(L1PcInstance pc, L1Character cha, int weaponId) throws Exception {
        int chaId;
        L1Skills skill;
        L1WeaponSkill weaponSkill = WeaponSkillTable.get().getTemplate(weaponId);
        if (pc == null || cha == null || weaponSkill == null || weaponSkill.getProbability() < _random.nextInt(100) + 1) {
            return 0.0d;
        }
        int skillId = weaponSkill.getSkillId();
        if (skillId != 0 && (skill = SkillsTable.get().getTemplate(skillId)) != null && skill.getTarget().equals("buff") && !isFreeze(cha)) {
            cha.setSkillEffect(skillId, weaponSkill.getSkillTime() * L1SkillId.STATUS_BRAVE);
        }
        int effectId = weaponSkill.getEffectId();
        if (effectId > 0) {
            if (weaponSkill.getEffectTarget() == 0) {
                chaId = cha.getId();
            } else {
                chaId = pc.getId();
            }
            if (!weaponSkill.isArrowType()) {
                pc.sendPacketsAll(new S_SkillSound(chaId, effectId));
            } else {
                pc.sendPacketsAll(new S_UseAttackSkill((L1Character) pc, cha.getId(), effectId, cha.getX(), cha.getY(), 1, false));
            }
        }
        double damage = 0.0d;
        int randomDamage = weaponSkill.getRandomDamage();
        if (randomDamage != 0) {
            damage = (double) _random.nextInt(randomDamage);
        }
        double damage2 = damage + ((double) weaponSkill.getFixDamage());
        int area = weaponSkill.getArea();
        if (area > 0 || area == -1) {
            Iterator<L1Object> it = World.get().getVisibleObjects(cha, area).iterator();
            while (it.hasNext()) {
                L1Object object = it.next();
                if (!(object == null || !(object instanceof L1Character) || object.getId() == pc.getId() || object.getId() == cha.getId())) {
                    if ((!(cha instanceof L1MonsterInstance) || (object instanceof L1MonsterInstance)) && ((!(cha instanceof L1PcInstance) && !(cha instanceof L1SummonInstance) && !(cha instanceof L1PetInstance)) || (object instanceof L1PcInstance) || (object instanceof L1SummonInstance) || (object instanceof L1PetInstance) || (object instanceof L1MonsterInstance))) {
                        damage2 = calcDamageReduction(pc, (L1Character) object, damage2, weaponSkill.getAttr());
                        if (damage2 > 0.0d) {
                            if (object instanceof L1PcInstance) {
                                L1PcInstance targetPc = (L1PcInstance) object;
                                targetPc.sendPacketsX8(new S_DoActionGFX(targetPc.getId(), 2));
                                targetPc.receiveDamage(pc, (double) ((int) damage2), false, false);
                            } else if ((object instanceof L1SummonInstance) || (object instanceof L1PetInstance) || (object instanceof L1MonsterInstance)) {
                                L1NpcInstance targetNpc = (L1NpcInstance) object;
                                targetNpc.broadcastPacketX8(new S_DoActionGFX(targetNpc.getId(), 2));
                                targetNpc.receiveDamage(pc, (int) damage2);
                            }
                        }
                    }
                }
            }
        }
        return calcDamageReduction(pc, cha, damage2, weaponSkill.getAttr());
    }

    public static double getBaphometStaffDamage(L1PcInstance pc, L1Character cha) {
        double dmg = 0.0d;
        if (14 >= _random.nextInt(100) + 1) {
            int locx = cha.getX();
            int locy = cha.getY();
            int sp = pc.getSp();
            int intel = pc.getInt();
            double bsk = 0.0d;
            if (pc.hasSkillEffect(55)) {
                bsk = 0.2d;
            }
            dmg = (((double) (intel + sp)) * (1.8d + bsk)) + (((double) _random.nextInt(intel + sp)) * 1.8d);
            pc.sendPacketsAll(new S_EffectLocation(locx, locy, 129));
        }
        return calcDamageReduction(pc, cha, dmg, 1);
    }

    public static double getDiceDaggerDamage(L1PcInstance pc, L1PcInstance targetPc, L1ItemInstance weapon) throws Exception {
        double dmg = 0.0d;
        if (3 >= _random.nextInt(100) + 1) {
            dmg = (double) ((targetPc.getCurrentHp() * 2) / 3);
            if (((double) targetPc.getCurrentHp()) - dmg < 0.0d) {
                dmg = 0.0d;
            }
            pc.sendPackets(new S_ServerMessage((int) L1SkillId.NATURES_TOUCH, weapon.getLogName()));
            pc.getInventory().removeItem(weapon, 1);
        }
        return dmg;
    }

    public static double getChaserDamage(L1PcInstance pc, L1Character cha) {
        if (8 < _random.nextInt(100) + 1) {
            return 0.0d;
        }
        pc.sendPacketsAll(new S_EffectLocation(cha.getX(), cha.getY(), 7025));
        return 8.0d;
    }

    public static double getKiringkuDamage(L1PcInstance pc, L1Character cha) {
        int kiringkuDamage = 0;
        for (int i = 0; i < 2; i++) {
            kiringkuDamage += _random.nextInt(5) + 1;
        }
        int kiringkuDamage2 = kiringkuDamage + 14;
        int charaIntelligence = (pc.getInt() + (pc.getSp() - pc.getTrueSp())) - 12;
        if (charaIntelligence < 1) {
            charaIntelligence = 1;
        }
        int dmg = (int) (((double) 0) + ((double) pc.getWeapon().getEnchantLevel()) + Math.floor((double) ((int) (((double) kiringkuDamage2) * (1.0d + ((((double) charaIntelligence) * 3.0d) / 32.0d))))));
        switch (pc.getWeapon().getItem().getGfxId()) {
            case L1SkillId.COOKING_2_2_N /*{ENCODED_INT: 3018}*/:
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 6983));
                break;
            default:
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7049));
                break;
        }
        return calcDamageReduction(pc, cha, (double) dmg, 0);
    }

    public static double getAreaSkillWeaponDamage(L1PcInstance pc, L1Character cha, int weaponId) throws Exception {
        double dmg = 0.0d;
        int probability = 0;
        int attr = 0;
        int chance = _random.nextInt(100) + 1;
        if (weaponId == 263) {
            probability = 5;
            attr = 4;
        } else if (weaponId == 260) {
            probability = 4;
            attr = 8;
        }
        if (probability >= chance) {
            int sp = pc.getSp();
            int intel = pc.getInt();
            int area = 0;
            int effectTargetId = 0;
            int effectId = 0;
            L1Character areaBase = cha;
            double damageRate = 0.0d;
            if (weaponId == 263) {
                area = 3;
                damageRate = 1.4d;
                effectTargetId = cha.getId();
                effectId = 1804;
                areaBase = cha;
            } else if (weaponId == 260) {
                area = 4;
                damageRate = 1.5d;
                effectTargetId = pc.getId();
                effectId = 758;
                areaBase = pc;
            }
            double bsk = 0.0d;
            if (pc.hasSkillEffect(55)) {
                bsk = 0.2d;
            }
            dmg = (((double) (intel + sp)) * (damageRate + bsk)) + (((double) _random.nextInt(intel + sp)) * damageRate);
            pc.sendPacketsX8(new S_SkillSound(effectTargetId, effectId));
            Iterator<L1Object> it = World.get().getVisibleObjects(areaBase, area).iterator();
            while (it.hasNext()) {
                L1Object object = it.next();
                if (!(object == null || !(object instanceof L1Character) || object.getId() == pc.getId() || object.getId() == cha.getId())) {
                    if ((!(cha instanceof L1MonsterInstance) || (object instanceof L1MonsterInstance)) && ((!(cha instanceof L1PcInstance) && !(cha instanceof L1SummonInstance) && !(cha instanceof L1PetInstance)) || (object instanceof L1PcInstance) || (object instanceof L1SummonInstance) || (object instanceof L1PetInstance) || (object instanceof L1MonsterInstance))) {
                        dmg = calcDamageReduction(pc, (L1Character) object, dmg, attr);
                        if (dmg > 0.0d) {
                            if (object instanceof L1PcInstance) {
                                L1PcInstance targetPc = (L1PcInstance) object;
                                targetPc.sendPacketsX8(new S_DoActionGFX(targetPc.getId(), 2));
                                targetPc.receiveDamage(pc, (double) ((int) dmg), false, false);
                            } else if ((object instanceof L1SummonInstance) || (object instanceof L1PetInstance) || (object instanceof L1MonsterInstance)) {
                                L1NpcInstance targetNpc = (L1NpcInstance) object;
                                targetNpc.broadcastPacketX8(new S_DoActionGFX(targetNpc.getId(), 2));
                                targetNpc.receiveDamage(pc, (int) dmg);
                            }
                        }
                    }
                }
            }
        }
        return calcDamageReduction(pc, cha, dmg, attr);
    }

    public static double getLightningEdgeDamage(L1PcInstance pc, L1Character cha) {
        double dmg = 0.0d;
        if (4 >= _random.nextInt(100) + 1) {
            int sp = pc.getSp();
            int intel = pc.getInt();
            double bsk = 0.0d;
            if (pc.hasSkillEffect(55)) {
                bsk = 0.2d;
            }
            dmg = (((double) (intel + sp)) * (2.0d + bsk)) + ((double) (_random.nextInt(intel + sp) * 2));
            pc.sendPacketsX8(new S_SkillSound(cha.getId(), 10));
        }
        return calcDamageReduction(pc, cha, dmg, 8);
    }

    public static void giveArkMageDiseaseEffect(L1PcInstance pc, L1Character cha) {
        int chance = _random.nextInt(L1SkillId.STATUS_BRAVE) + 1;
        int probability = (5 - ((cha.getMr() / 10) * 5)) * 10;
        if (probability == 0) {
            probability = 10;
        }
        if (probability >= chance) {
            new L1SkillUse().handleCommands(pc, 56, cha.getId(), cha.getX(), cha.getY(), 0, 4);
        }
    }

    public static void giveFettersEffect(L1PcInstance pc, L1Character cha) {
        if (!isFreeze(cha) && _random.nextInt(100) + 1 <= 2) {
            L1SpawnUtil.spawnEffect(81182, 8, cha.getX(), cha.getY(), cha.getMapId(), cha, 0);
            if (cha instanceof L1PcInstance) {
                L1PcInstance targetPc = (L1PcInstance) cha;
                targetPc.setSkillEffect(L1SkillId.STATUS_FREEZE, 8);
                targetPc.sendPacketsX8(new S_SkillSound(targetPc.getId(), 4184));
                targetPc.sendPackets(new S_Paralysis(6, true));
            } else if ((cha instanceof L1MonsterInstance) || (cha instanceof L1SummonInstance) || (cha instanceof L1PetInstance)) {
                L1NpcInstance npc = (L1NpcInstance) cha;
                npc.setSkillEffect(L1SkillId.STATUS_FREEZE, 8);
                npc.broadcastPacketX8(new S_SkillSound(npc.getId(), 4184));
                npc.setParalyzed(true);
            }
        }
    }

    public static double calcDamageReduction(L1PcInstance pc, L1Character cha, double dmg, int attr) {
        int resistFloor;
        if (isFreeze(cha)) {
            return 0.0d;
        }
        int mr = cha.getMr();
        double mrFloor = 0.0d;
        if (mr <= 100) {
            mrFloor = Math.floor((double) ((mr - pc.getOriginalMagicHit()) / 2));
        } else if (mr >= 100) {
            mrFloor = Math.floor((double) ((mr - pc.getOriginalMagicHit()) / 10));
        }
        double mrCoefficient = 0.0d;
        if (mr <= 100) {
            mrCoefficient = 1.0d - (0.01d * mrFloor);
        } else if (mr >= 100) {
            mrCoefficient = 0.6d - (0.01d * mrFloor);
        }
        double dmg2 = dmg * mrCoefficient;
        int resist = 0;
        if (attr == 1) {
            resist = cha.getEarth();
        } else if (attr == 2) {
            resist = cha.getFire();
        } else if (attr == 4) {
            resist = cha.getWater();
        } else if (attr == 8) {
            resist = cha.getWind();
        }
        int resistFloor2 = (int) (0.32d * ((double) Math.abs(resist)));
        if (resist >= 0) {
            resistFloor = resistFloor2 * 1;
        } else {
            resistFloor = resistFloor2 * -1;
        }
        return dmg2 * (1.0d - (((double) resistFloor) / 32.0d));
    }

    public static boolean isFreeze(L1Character cha) {
        if (cha.hasSkillEffect(L1SkillId.STATUS_FREEZE) || cha.hasSkillEffect(78) || cha.hasSkillEffect(50) || cha.hasSkillEffect(80) || cha.hasSkillEffect(194) || cha.hasSkillEffect(157)) {
            return true;
        }
        if (!cha.hasSkillEffect(31)) {
            return false;
        }
        cha.removeSkillEffect(31);
        int castgfx = SkillsTable.get().getTemplate(31).getCastGfx();
        cha.broadcastPacketX8(new S_SkillSound(cha.getId(), castgfx));
        if (!(cha instanceof L1PcInstance)) {
            return true;
        }
        L1PcInstance pc = (L1PcInstance) cha;
        pc.sendPacketsX8(new S_SkillSound(pc.getId(), castgfx));
        return true;
    }
}
