package com.lineage.server.model;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import java.util.ConcurrentModificationException;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class L1AttackMode {
    protected static final int NPC_NPC = 4;
    protected static final int NPC_PC = 3;
    protected static final int PC_NPC = 2;
    protected static final int PC_PC = 1;
    private static final Log _log = LogFactory.getLog(L1AttackMode.class);
    protected static final Random _random = new Random();
    protected L1ItemInstance _arrow;
    protected int _arrowGfxid = 66;
    protected int _attckActId = 0;
    protected int _attckGrfxId = 0;
    protected int _calcType;
    protected int _damage = 0;
    protected int _drainHp = 0;
    protected int _drainMana = 0;
    protected int _hitRate = 0;
    protected boolean _isHit = false;
    protected int _leverage = 10;
    protected L1NpcInstance _npc = null;
    protected L1PcInstance _pc = null;
    protected int _statusDamage = 0;
    protected L1ItemInstance _sting;
    protected int _stingGfxid = 2989;
    protected L1Character _target = null;
    protected int _targetId;
    protected L1NpcInstance _targetNpc = null;
    protected L1PcInstance _targetPc = null;
    protected int _targetX;
    protected int _targetY;
    protected L1ItemInstance _weapon = null;
    protected int _weaponAddDmg = 0;
    protected int _weaponAddHit = 0;
    protected int _weaponAttrEnchantKind = 0;
    protected int _weaponAttrEnchantLevel = 0;
    protected int _weaponBless = 1;
    protected int _weaponDoubleDmgChance = 0;
    protected int _weaponEnchant = 0;
    protected int _weaponId = 0;
    protected int _weaponLarge = 0;
    protected int _weaponMaterial = 0;
    protected int _weaponRange = 1;
    protected int _weaponSmall = 0;
    protected int _weaponType = 0;
    protected int _weaponType2 = 0;

    public abstract void action();

    public abstract void addChaserAttack();

    public abstract int calcDamage() throws Exception;

    public abstract boolean calcHit();

    public abstract void calcStaffOfMana();

    public abstract void commit() throws Exception;

    public abstract void commitCounterBarrier() throws Exception;

    public abstract boolean isShortDistance();

    protected static double getDamageUpByClan(L1PcInstance pc) {
        double dmg = 0.0d;
        if (pc == null) {
            return 0.0d;
        }
        try {
            L1Clan clan = pc.getClan();
            if (clan == null) {
                return 0.0d;
            }
            if (clan.isClanskill() && pc.get_other().get_clanskill() == 1) {
                dmg = 0.0d + (0.25d * ((double) clan.getOnlineClanMemberSize()));
            }
            return dmg;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return 0.0d;
        }
    }

    protected static double getDamageReductionByClan(L1PcInstance targetPc) {
        double dmg = 0.0d;
        if (targetPc == null) {
            return 0.0d;
        }
        try {
            L1Clan clan = targetPc.getClan();
            if (clan == null) {
                return 0.0d;
            }
            if (clan.isClanskill() && targetPc.get_other().get_clanskill() == 2) {
                dmg = 0.0d + (0.25d * ((double) clan.getOnlineClanMemberSize()));
            }
            return dmg;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return 0.0d;
        }
    }

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

    protected static int attackerDice(L1Character character) {
        int attackerDice = 0;
        if (character.get_dodge() > 0) {
            attackerDice = 0 - character.get_dodge();
        }
        if (character.get_dodge_down() > 0) {
            return attackerDice + character.get_dodge_down();
        }
        return attackerDice;
    }

    public void setLeverage(int i) {
        this._leverage = i;
    }

    /* access modifiers changed from: protected */
    public int getLeverage() {
        return this._leverage;
    }

    public void setActId(int actId) {
        this._attckActId = actId;
    }

    public void setGfxId(int gfxId) {
        this._attckGrfxId = gfxId;
    }

    public int getActId() {
        return this._attckActId;
    }

    public int getGfxId() {
        return this._attckGrfxId;
    }

    /* access modifiers changed from: protected */
    public boolean calcErEvasion() {
        return this._targetPc.getEr() < _random.nextInt(100) + 1;
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
    public int calcPcDefense() {
        int acDefMax;
        try {
            if (this._targetPc == null || (acDefMax = this._targetPc.getClassFeature().getAcDefenseMax(Math.max(0, 10 - this._targetPc.getAc()))) == 0) {
                return 0;
            }
            return _random.nextInt(acDefMax) + Math.max(1, acDefMax >> 3);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return 0;
        }
    }

    /* access modifiers changed from: protected */
    public int calcNpcDamageReduction() {
        int damagereduction = this._targetNpc.getNpcTemplate().get_damagereduction();
        try {
            int acDefMax = Math.max(0, 10 - this._targetNpc.getAc()) / 7;
            if (acDefMax == 0) {
                return damagereduction;
            }
            return damagereduction + _random.nextInt(acDefMax) + Math.max(1, acDefMax);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return damagereduction;
        }
    }

    /* access modifiers changed from: protected */
    public int calcCounterBarrierDamage() {
        try {
            if (this._targetPc != null) {
                L1ItemInstance weapon = this._targetPc.getWeapon();
                if (weapon == null || weapon.getItem().getType() != 3) {
                    return 0;
                }
                return ((weapon.getItem().getDmgLarge() + weapon.getEnchantLevel()) + weapon.getItem().getDmgModifier()) << 1;
            } else if (this._targetNpc != null) {
                return (this._targetNpc.getStr() + this._targetNpc.getLevel()) << 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return 0;
        }
    }

    /* access modifiers changed from: protected */
    public double coatArms() {
        int damage = 100;
        try {
            if (this._targetPc != null) {
                damage = 100 - this._targetPc.get_dmgDown();
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return ((double) damage) / 100.0d;
    }
}
