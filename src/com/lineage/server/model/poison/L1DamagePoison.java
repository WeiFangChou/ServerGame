package com.lineage.server.model.poison;

import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.ModelError;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_SkillIconPoison;
import com.lineage.server.thread.GeneralThreadPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1DamagePoison extends L1Poison {
    private static final Log _log = LogFactory.getLog(L1DamagePoison.class);
    private final L1Character _attacker;
    private final int _damage;
    private final int _damageSpan;
    private final L1Character _target;
    private Thread _timer;

    private L1DamagePoison(L1Character attacker, L1Character cha, int damageSpan, int damage) {
        this._attacker = attacker;
        this._target = cha;
        this._damageSpan = damageSpan;
        this._damage = damage;
        doInfection();
    }

    /* access modifiers changed from: private */
    public class NormalPoisonTimer extends Thread {
        private NormalPoisonTimer() {
        }

        /* synthetic */ NormalPoisonTimer(L1DamagePoison l1DamagePoison, NormalPoisonTimer normalPoisonTimer) {
            this();
        }

        public void run() {
            while (L1DamagePoison.this._target.hasSkillEffect(L1SkillId.STATUS_POISON)) {
                try {
                    Thread.sleep((long) L1DamagePoison.this._damageSpan);
                    if (!L1DamagePoison.this._target.hasSkillEffect(L1SkillId.STATUS_POISON)) {
                        break;
                    } else if (L1DamagePoison.this._target instanceof L1PcInstance) {
                        L1PcInstance player = (L1PcInstance) L1DamagePoison.this._target;
                        player.receiveDamage(L1DamagePoison.this._attacker, (double) L1DamagePoison.this._damage, false, true);
                        if (player.isDead()) {
                            break;
                        }
                    } else if (L1DamagePoison.this._target instanceof L1MonsterInstance) {
                        L1MonsterInstance mob = (L1MonsterInstance) L1DamagePoison.this._target;
                        mob.receiveDamage(L1DamagePoison.this._attacker, L1DamagePoison.this._damage);
                        if (mob.isDead()) {
                            break;
                        }
                    } else {
                        continue;
                    }
                } catch (InterruptedException e) {
                    ModelError.isError(L1DamagePoison._log, e.getLocalizedMessage(), e);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            L1DamagePoison.this.cure();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isDamageTarget(L1Character cha) {
        return (cha instanceof L1PcInstance) || (cha instanceof L1MonsterInstance);
    }

    private void doInfection() {
        this._target.setSkillEffect(L1SkillId.STATUS_POISON, 30000);
        this._target.setPoisonEffect(1);
        if (isDamageTarget(this._target)) {
            this._timer = new NormalPoisonTimer(this, null);
            GeneralThreadPool.get().execute(this._timer);
        }
    }

    public static boolean doInfection(L1Character attacker, L1Character cha, int damageSpan, int damage) {
        if (!isValidTarget(cha)) {
            return false;
        }
        cha.setPoison(new L1DamagePoison(attacker, cha, damageSpan, damage));
        return true;
    }

    @Override // com.lineage.server.model.poison.L1Poison
    public int getEffectId() {
        return 1;
    }

    @Override // com.lineage.server.model.poison.L1Poison
    public void cure() {
        this._target.setPoisonEffect(0);
        this._target.killSkillEffectTimer(L1SkillId.STATUS_POISON);
        this._target.setPoison(null);
        if (this._target instanceof L1PcInstance) {
            ((L1PcInstance) this._target).sendPackets(new S_SkillIconPoison(0, 0));
        }
        if (this._timer != null) {
            this._timer.interrupt();
        }
    }
}
