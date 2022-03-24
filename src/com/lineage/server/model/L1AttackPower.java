package com.lineage.server.model;

import com.lineage.server.datatables.ExtraAttrWeaponTable;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1AttrWeapon;
import com.lineage.server.world.World;
import java.util.Iterator;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1AttackPower {
    private static final Log _log = LogFactory.getLog(L1AttackPower.class);
    private static final Random _random = new Random();
    private final L1PcInstance _pc;
    private final L1Character _target;
    private L1NpcInstance _targetNpc;
    private L1PcInstance _targetPc;
    private int _weaponAttrEnchantKind = 0;
    private int _weaponAttrEnchantLevel = 0;

    public L1AttackPower(L1PcInstance attacker, L1Character target, int weaponAttrEnchantKind, int weaponAttrEnchantLevel) {
        this._pc = attacker;
        this._target = target;
        if (this._target instanceof L1NpcInstance) {
            this._targetNpc = (L1NpcInstance) this._target;
        } else if (this._target instanceof L1PcInstance) {
            this._targetPc = (L1PcInstance) this._target;
        }
        this._weaponAttrEnchantKind = weaponAttrEnchantKind;
        this._weaponAttrEnchantLevel = weaponAttrEnchantLevel;
    }

    public int set_item_power(int damage) {
        int reset_dmg = damage;
        try {
            if (this._weaponAttrEnchantKind > 0) {
                L1AttrWeapon attrWeapon = ExtraAttrWeaponTable.getInstance().get(this._weaponAttrEnchantKind, this._weaponAttrEnchantLevel);
                if (attrWeapon == null) {
                    return damage;
                }
                if (attrWeapon.getProbability() <= _random.nextInt(L1SkillId.STATUS_BRAVE)) {
                    return reset_dmg;
                }
                if (attrWeapon.getTypeBind() > 0.0d && !L1WeaponSkill.isFreeze(this._target)) {
                    int time = (int) (attrWeapon.getTypeBind() * 1000.0d);
                    if (this._targetPc != null) {
                        int t = time - this._targetPc.getEarth_Def();
                        if (t > 0) {
                            this._targetPc.setSkillEffect(L1SkillId.MOVE_STOP, t);
                            this._targetPc.sendPackets(new S_Paralysis(6, true));
                            this._pc.sendPacketsAll(new S_SkillSound(this._targetPc.getId(), attrWeapon.gfxid()));
                        }
                    } else if (this._targetNpc != null) {
                        this._targetNpc.setSkillEffect(L1SkillId.MOVE_STOP, time);
                        this._targetNpc.setParalyzed(true);
                        this._pc.sendPacketsAll(new S_SkillSound(this._targetNpc.getId(), attrWeapon.gfxid()));
                    }
                }
                if (attrWeapon.getTypeDmgup() > 0.0d) {
                    reset_dmg = (int) (((double) reset_dmg) * attrWeapon.getTypeDmgup());
                    if (this._targetPc != null) {
                        this._pc.sendPacketsAll(new S_SkillSound(this._targetPc.getId(), attrWeapon.gfxid()));
                    }
                    if (this._targetNpc != null) {
                        this._pc.sendPacketsAll(new S_SkillSound(this._targetNpc.getId(), attrWeapon.gfxid()));
                    }
                }
                if (attrWeapon.getTypeDrainHp() > 0) {
                    int drainHp = _random.nextInt(attrWeapon.getTypeDrainHp()) + 1;
                    this._pc.setCurrentHp((short) (this._pc.getCurrentHp() + drainHp));
                    if (this._targetPc != null) {
                        this._targetPc.setCurrentHp(Math.max(this._targetPc.getCurrentHp() - drainHp, 0));
                        this._pc.sendPacketsAll(new S_SkillSound(this._targetPc.getId(), attrWeapon.gfxid()));
                    } else if (this._targetNpc != null) {
                        this._targetNpc.setCurrentHp(Math.max(this._targetNpc.getCurrentHp() - drainHp, 0));
                        this._pc.sendPacketsAll(new S_SkillSound(this._targetNpc.getId(), attrWeapon.gfxid()));
                    }
                }
                if (attrWeapon.getTypeDrainMp() > 0) {
                    int drainMp = _random.nextInt(attrWeapon.getTypeDrainMp()) + 1;
                    this._pc.setCurrentMp((short) (this._pc.getCurrentMp() + drainMp));
                    if (this._targetPc != null) {
                        this._targetPc.setCurrentMp(Math.max(this._targetPc.getCurrentMp() - drainMp, 0));
                        this._pc.sendPacketsAll(new S_SkillSound(this._targetPc.getId(), attrWeapon.gfxid()));
                    } else if (this._targetNpc != null) {
                        this._targetNpc.setCurrentMp(Math.max(this._targetNpc.getCurrentMp() - drainMp, 0));
                        this._pc.sendPacketsAll(new S_SkillSound(this._targetNpc.getId(), attrWeapon.gfxid()));
                    }
                }
                if (attrWeapon.getTypeRange() > 0 && attrWeapon.getTypeRangeDmg() > 0) {
                    this._pc.sendPacketsAll(new S_SkillSound(this._pc.getId(), attrWeapon.gfxid()));
                    int dmg = attrWeapon.getTypeRangeDmg();
                    if (this._targetPc != null) {
                        this._targetPc.receiveDamage(this._pc, (double) dmg, false, false);
                        Iterator<?> localIterator1 = World.get().getVisibleObjects(this._targetPc, attrWeapon.getTypeRange()).iterator();
                        while (localIterator1.hasNext()) {
                            L1Object tgobj = (L1Object) localIterator1.next();
                            if (tgobj instanceof L1PcInstance) {
                                L1PcInstance tgpc = (L1PcInstance) tgobj;
                                if (!tgpc.isDead() && tgpc.getId() != this._pc.getId()) {
                                    if ((tgpc.getClanid() != this._pc.getClanid() || tgpc.getClanid() == 0) && !tgpc.getMap().isSafetyZone(tgpc.getLocation())) {
                                        tgpc.receiveDamage(this._pc, (double) dmg, false, false);
                                    }
                                }
                            }
                        }
                    } else if (this._targetNpc != null) {
                        this._targetNpc.receiveDamage(this._pc, dmg);
                        Iterator<?> localIterator12 = World.get().getVisibleObjects(this._targetNpc, attrWeapon.getTypeRange()).iterator();
                        while (localIterator12.hasNext()) {
                            L1Object tgobj2 = (L1Object) localIterator12.next();
                            if (tgobj2 instanceof L1MonsterInstance) {
                                L1MonsterInstance tgmob = (L1MonsterInstance) tgobj2;
                                if (!tgmob.isDead()) {
                                    tgmob.receiveDamage(this._pc, dmg);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return reset_dmg;
    }
}
