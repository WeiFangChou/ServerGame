package com.lineage.server.model;

import com.lineage.config.ConfigAlt;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.model.poison.L1DamagePoison;
import com.lineage.server.model.poison.L1ParalysisPoison;
import com.lineage.server.model.poison.L1SilencePoison;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_AttackPacketNpc;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillIconPoison;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_UseArrowSkill;
import com.lineage.server.serverpackets.S_UseAttackSkill;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.types.Point;
import java.util.ConcurrentModificationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1AttackNpc extends L1AttackMode {
    private static final Log _log = LogFactory.getLog(L1AttackNpc.class);

    public L1AttackNpc(L1NpcInstance attacker, L1Character target) {
        if (attacker != null && target != null && !target.isDead() && target.getCurrentHp() > 0) {
            this._npc = attacker;
            if (target instanceof L1PcInstance) {
                this._targetPc = (L1PcInstance) target;
                this._calcType = 3;
            } else if (target instanceof L1NpcInstance) {
                this._targetNpc = (L1NpcInstance) target;
                this._calcType = 4;
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
        switch (this._calcType) {
            case 3:
                this._isHit = calcPcHit();
                break;
            case 4:
                this._isHit = calcNpcHit();
                break;
        }
        return this._isHit;
    }

    private boolean calcPcHit() {
        if (dmg0(this._targetPc)) {
            return false;
        }
        if (calcEvasion()) {
            return false;
        }
        this._hitRate += this._npc.getLevel() + 5;
        if (this._npc instanceof L1PetInstance) {
            this._hitRate = ((L1PetInstance) this._npc).getHitByWeapon() + this._hitRate;
        }
        this._hitRate += this._npc.getHitup();
        int attackerDice = (((_random.nextInt(20) + 1) + this._hitRate) - 3) + attackerDice(this._targetPc);
        int defenderDice = 0;
        int defenderValue = this._targetPc.getAc() * -1;
        if (this._targetPc.getAc() >= 0) {
            defenderDice = 10 - this._targetPc.getAc();
        } else if (this._targetPc.getAc() < 0) {
            defenderDice = _random.nextInt(defenderValue) + 10 + 1;
        }
        int fumble = this._hitRate;
        int critical = this._hitRate + 19;
        if (attackerDice <= fumble) {
            this._hitRate = 15;
        } else if (attackerDice >= critical) {
            this._hitRate = 100;
        } else if (attackerDice > defenderDice) {
            this._hitRate = 100;
        } else if (attackerDice <= defenderDice) {
            this._hitRate = 15;
        }
        if (this._npc.getNpcTemplate().is_boss()) {
            int attackerDice2 = attackerDice + 30;
        }
        int rnd = _random.nextInt(100) + 1;
        if (this._npc.get_ranged() >= 10 && this._hitRate > rnd && this._npc.getLocation().getTileLineDistance(new Point(this._targetX, this._targetY)) >= 2) {
            return calcErEvasion();
        }
        if (this._hitRate >= rnd) {
            return true;
        }
        return false;
    }

    private boolean calcNpcHit() {
        if (dmg0(this._targetNpc)) {
            return false;
        }
        this._hitRate += this._npc.getLevel() + 3;
        if (this._npc instanceof L1PetInstance) {
            this._hitRate = ((L1PetInstance) this._npc).getHitByWeapon() + this._hitRate;
        }
        this._hitRate += this._npc.getHitup();
        int attackerDice = (((_random.nextInt(20) + 1) + this._hitRate) - 3) + attackerDice(this._targetNpc);
        if (this._npc.getNpcTemplate().is_boss()) {
            attackerDice += 30;
        }
        int defenderDice = 0;
        int defenderValue = this._targetNpc.getAc() * -1;
        if (this._targetNpc.getAc() >= 0) {
            defenderDice = 10 - this._targetNpc.getAc();
        } else if (this._targetNpc.getAc() < 0) {
            defenderDice = _random.nextInt(defenderValue) + 10 + 1;
        }
        int fumble = this._hitRate;
        int critical = this._hitRate + 19;
        if (attackerDice <= fumble) {
            this._hitRate = 15;
        } else if (attackerDice >= critical) {
            this._hitRate = 100;
        } else if (attackerDice > defenderDice) {
            this._hitRate = 100;
        } else if (attackerDice <= defenderDice) {
            this._hitRate = 15;
        }
        if (this._hitRate >= _random.nextInt(100) + 1) {
            return true;
        }
        return false;
    }

    @Override // com.lineage.server.model.L1AttackMode
    public int calcDamage() {
        switch (this._calcType) {
            case 3:
                this._damage = calcPcDamage();
                break;
            case 4:
                this._damage = calcNpcDamage();
                break;
        }
        return this._damage;
    }

    private double npcDmgMode(double dmg) {
        if (_random.nextInt(100) < 15) {
            dmg *= 1.8d;
        }
        double dmg2 = dmg + ((double) this._npc.getDmgup());
        if (isUndeadDamage()) {
            dmg2 *= 1.2d;
        }
        double dmg3 = (double) ((int) ((((double) getLeverage()) / 10.0d) * dmg2));
        if (this._npc.getNpcTemplate().is_boss()) {
            dmg3 *= 1.8d;
        }
        if (this._npc.isWeaponBreaked()) {
            return dmg3 / 2.0d;
        }
        return dmg3;
    }

    private int calcPcDamage() {
        if (this._targetPc == null) {
            return 0;
        }
        if (dmg0(this._targetPc)) {
            this._isHit = false;
            return 0;
        }
        int lvl = this._npc.getLevel();
        double dmg = ((double) _random.nextInt(lvl)) + (((double) this._npc.getStr()) * 0.8d) + ((double) L1AttackList.STRD.get(Integer.valueOf(this._npc.getStr())).intValue());
        if (this._npc instanceof L1PetInstance) {
            dmg = dmg + ((double) (lvl / 7)) + ((double) ((L1PetInstance) this._npc).getDamageByWeapon());
        }
        double dmg2 = ((npcDmgMode(dmg) - ((double) calcPcDefense())) - ((double) this._targetPc.getDamageReductionByArmor())) - ((double) this._targetPc.dmgDowe());
        if (this._targetPc.getClanid() != 0) {
            dmg2 -= getDamageReductionByClan(this._targetPc);
        }
        if (this._targetPc.hasSkillEffect(88)) {
            dmg2 -= (double) (((Math.max(this._targetPc.getLevel(), 50) - 50) / 5) + 1);
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
                            dmg2 += (double) integer.intValue();
                        }
                    }
                }
            } catch (ConcurrentModificationException ignored) {
            } catch (Exception e2) {
                _log.error(e2.getLocalizedMessage(), e2);
            }
        }
        if (dmgX2) {
            dmg2 /= 2.0d;
        }
        boolean isNowWar = false;
        int castleId = L1CastleLocation.getCastleIdByArea(this._targetPc);
        if (castleId > 0) {
            isNowWar = ServerWarExecutor.get().isNowWar(castleId);
        }
        if (!isNowWar) {
            if (this._npc instanceof L1PetInstance) {
                dmg2 /= 8.0d;
            }
            if ((this._npc instanceof L1SummonInstance) && ((L1SummonInstance) this._npc).isExsistMaster()) {
                dmg2 /= 8.0d;
            }
        }
        double dmg3 = dmg2 * coatArms();
        if (dmg3 <= 0.0d) {
            this._isHit = false;
        }
        addNpcPoisonAttack(this._targetPc);
        if (!this._isHit) {
            dmg3 = 0.0d;
        }
        return (int) dmg3;
    }

    private int calcNpcDamage() {
        double dmg;
        if (this._targetNpc == null) {
            return 0;
        }
        if (dmg0(this._targetNpc)) {
            this._isHit = false;
            return 0;
        }
        int lvl = this._npc.getLevel();
        if (this._npc instanceof L1PetInstance) {
            dmg = ((double) (_random.nextInt(this._npc.getNpcTemplate().get_level()) + (this._npc.getStr() / 2) + 1)) + ((double) (lvl / 14)) + ((double) ((L1PetInstance) this._npc).getDamageByWeapon());
        } else {
            dmg = (double) (_random.nextInt(lvl) + (this._npc.getStr() / 2) + L1AttackList.STRD.get(Integer.valueOf(this._npc.getStr())).intValue());
        }
        double dmg2 = npcDmgMode(dmg) - ((double) calcNpcDamageReduction());
        addNpcPoisonAttack(this._targetNpc);
        if (dmg2 <= 0.0d) {
            this._isHit = false;
        }
        if (!this._isHit) {
            dmg2 = 0.0d;
        }
        return (int) dmg2;
    }

    private boolean isUndeadDamage() {
        int undead = this._npc.getNpcTemplate().get_undead();
        if (!L1GameTimeClock.getInstance().currentTime().isNight()) {
            return false;
        }
        switch (undead) {
            case 1:
            case 3:
            case 4:
                return true;
            case 2:
            default:
                return false;
        }
    }

    private void addNpcPoisonAttack(L1Character target) {
        switch (this._npc.getNpcTemplate().get_poisonatk()) {
            case 1:
                if (15 >= _random.nextInt(100) + 1 && L1DamagePoison.doInfection(this._npc, target, 3000, 5) && (target instanceof L1PcInstance)) {
                    ((L1PcInstance) target).sendPackets(new S_SkillIconPoison(1, 30));
                    break;
                }
            case 2:
                if (15 >= _random.nextInt(100) + 1 && L1SilencePoison.doInfection(target) && (target instanceof L1PcInstance)) {
                    ((L1PcInstance) target).sendPackets(new S_SkillIconPoison(6, OpcodesServer.S_OPCODE_STRUP));
                    break;
                }
            case 4:
                if (15 >= _random.nextInt(100) + 1) {
                    L1ParalysisPoison.doInfection(target, 20000, 45000);
                    break;
                }
                break;
        }
        this._npc.getNpcTemplate().get_paralysisatk();
    }

    @Override // com.lineage.server.model.L1AttackMode
    public void action() {
        boolean isLongRange = true;
        try {
            if (this._npc != null && !this._npc.isDead()) {
                this._npc.setHeading(this._npc.targetDirection(this._targetX, this._targetY));
                if (this._npc.getLocation().getTileLineDistance(new Point(this._targetX, this._targetY)) <= 1) {
                    isLongRange = false;
                }
                int bowActId = this._npc.getBowActId();
                if (!isLongRange || bowActId <= 0) {
                    actionX2();
                } else {
                    actionX1();
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void actionX1() {
        try {
            this._npc.broadcastPacketX10(new S_UseArrowSkill(this._npc, this._targetId, this._npc.getBowActId(), this._targetX, this._targetY, this._damage));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void actionX2() {
        int actId;
        try {
            if (getActId() > 0) {
                actId = getActId();
            } else {
                actId = 1;
            }
            if (this._isHit) {
                if (getGfxId() > 0) {
                    this._npc.broadcastPacketX10(new S_UseAttackSkill(this._target, this._npc.getId(), getGfxId(), this._targetX, this._targetY, actId, this._damage));
                    return;
                }
                gfx7049();
                this._npc.broadcastPacketX10(new S_AttackPacketNpc(this._npc, this._target, actId, this._damage));
            } else if (getGfxId() > 0) {
                this._npc.broadcastPacketX10(new S_UseAttackSkill(this._target, this._npc.getId(), getGfxId(), this._targetX, this._targetY, actId, 0));
            } else {
                this._npc.broadcastPacketX10(new S_AttackPacketNpc(this._npc, this._target, actId));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void gfx7049() {
        if (this._npc.getStatus() == 58) {
            boolean is = false;
            if (this._npc.getTempCharGfx() == 6671 && this._npc.getGfxId() == 6671) {
                is = true;
            }
            if (this._npc.getTempCharGfx() == 6650 && this._npc.getGfxId() == 6650) {
                is = true;
            }
            if (is) {
                this._npc.broadcastPacketAll(new S_SkillSound(this._npc.getId(), 7049));
            }
        }
    }

    @Override // com.lineage.server.model.L1AttackMode
    public void commit() throws Exception {
        if (this._isHit) {
            switch (this._calcType) {
                case 3:
                    commitPc();
                    break;
                case 4:
                    commitNpc();
                    break;
            }
        }
        if (ConfigAlt.ALT_ATKMSG && this._calcType != 4 && this._targetPc.isGm()) {
            String srcatk = this._npc.getName();
            String tgatk = this._targetPc.getName();
            this._targetPc.sendPackets(new S_ServerMessage(166, "受到NPC攻擊: " + (String.valueOf(srcatk) + ">" + tgatk + " " + (this._isHit ? "傷害:" + this._damage : "失敗") + (" 命中:" + this._hitRate + "% 剩餘hp:" + this._targetPc.getCurrentHp()))));
        }
    }

    private void commitPc() throws Exception {
        this._targetPc.receiveDamage(this._npc, (double) this._damage, false, false);
    }

    private void commitNpc() throws Exception {
        this._targetNpc.receiveDamage(this._npc, this._damage);
    }

    @Override // com.lineage.server.model.L1AttackMode
    public boolean isShortDistance() {
        boolean isLongRange = true;
        if (this._npc.getLocation().getTileLineDistance(new Point(this._targetX, this._targetY)) <= 1) {
            isLongRange = false;
        }
        int bowActId = this._npc.getBowActId();
        if (!isLongRange || bowActId <= 0) {
            return true;
        }
        return false;
    }

    @Override // com.lineage.server.model.L1AttackMode
    public void commitCounterBarrier() throws Exception {
        int damage = calcCounterBarrierDamage();
        if (damage != 0) {
            this._npc.receiveDamage(this._target, damage);
            this._npc.broadcastPacketAll(new S_DoActionGFX(this._npc.getId(), 2));
        }
    }

    @Override // com.lineage.server.model.L1AttackMode
    public void addChaserAttack() {
    }

    @Override // com.lineage.server.model.L1AttackMode
    public void calcStaffOfMana() {
    }
}
