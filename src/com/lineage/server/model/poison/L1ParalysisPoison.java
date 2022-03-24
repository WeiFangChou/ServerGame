package com.lineage.server.model.poison;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.ModelError;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_SkillIconPoison;
import com.lineage.server.thread.GeneralThreadPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ParalysisPoison extends L1Poison {
    private static final Log _log = LogFactory.getLog(L1ParalysisPoison.class);
    private final int _delay;
    private int _effectId = 1;
    private final L1Character _target;
    private final int _time;
    private Thread _timer;

    /* access modifiers changed from: private */
    public class ParalysisPoisonTimer extends Thread {
        private ParalysisPoisonTimer() {
        }

        /* synthetic */ ParalysisPoisonTimer(L1ParalysisPoison l1ParalysisPoison, ParalysisPoisonTimer paralysisPoisonTimer) {
            this();
        }

        public void run() {
            L1ParalysisPoison.this._target.setSkillEffect(L1SkillId.STATUS_POISON_PARALYZING, 0);
            try {
                Thread.sleep((long) L1ParalysisPoison.this._delay);
                L1ParalysisPoison.this._effectId = 2;
                L1ParalysisPoison.this._target.setPoisonEffect(2);
                if (L1ParalysisPoison.this._target instanceof L1PcInstance) {
                    L1PcInstance player = (L1PcInstance) L1ParalysisPoison.this._target;
                    if (!player.isDead()) {
                        player.sendPackets(new S_Paralysis(1, true));
                        L1ParalysisPoison.this._timer = new ParalysisTimer(L1ParalysisPoison.this, null);
                        GeneralThreadPool.get().execute(L1ParalysisPoison.this._timer);
                        if (isInterrupted()) {
                            L1ParalysisPoison.this._timer.interrupt();
                        }
                    }
                }
            } catch (InterruptedException e) {
                ModelError.isError(L1ParalysisPoison._log, e.getLocalizedMessage(), e);
            }
        }
    }

    private class ParalysisTimer extends Thread {
        private ParalysisTimer() {
        }

        /* synthetic */ ParalysisTimer(L1ParalysisPoison l1ParalysisPoison, ParalysisTimer paralysisTimer) {
            this();
        }

        public void run() {
            L1ParalysisPoison.this._target.killSkillEffectTimer(L1SkillId.STATUS_POISON_PARALYZING);
            L1ParalysisPoison.this._target.setSkillEffect(L1SkillId.STATUS_POISON_PARALYZED, 0);
            try {
                Thread.sleep((long) L1ParalysisPoison.this._time);
            } catch (InterruptedException e) {
                ModelError.isError(L1ParalysisPoison._log, e.getLocalizedMessage(), e);
            }
            L1ParalysisPoison.this._target.killSkillEffectTimer(L1SkillId.STATUS_POISON_PARALYZED);
            if (L1ParalysisPoison.this._target instanceof L1PcInstance) {
                L1PcInstance player = (L1PcInstance) L1ParalysisPoison.this._target;
                if (!player.isDead()) {
                    player.sendPackets(new S_Paralysis(1, false));
                    L1ParalysisPoison.this.cure();
                }
            }
        }
    }

    private L1ParalysisPoison(L1Character cha, int delay, int time) {
        this._target = cha;
        this._delay = delay;
        this._time = time;
        doInfection();
    }

    public static boolean doInfection(L1Character cha, int delay, int time) {
        if (!L1Poison.isValidTarget(cha)) {
            return false;
        }
        cha.setPoison(new L1ParalysisPoison(cha, delay, time));
        return true;
    }

    private void doInfection() {
        sendMessageIfPlayer(this._target, 212);
        this._target.setPoisonEffect(1);
        if (this._target instanceof L1PcInstance) {
            this._timer = new ParalysisPoisonTimer(this, null);
            GeneralThreadPool.get().execute(this._timer);
            ((L1PcInstance) this._target).sendPackets(new S_SkillIconPoison(2, 3));
        }
    }

    @Override // com.lineage.server.model.poison.L1Poison
    public int getEffectId() {
        return this._effectId;
    }

    @Override // com.lineage.server.model.poison.L1Poison
    public void cure() {
        this._target.setPoisonEffect(0);
        this._target.setPoison(null);
        if (this._target instanceof L1PcInstance) {
            ((L1PcInstance) this._target).sendPackets(new S_SkillIconPoison(0, 0));
        }
        if (this._timer != null) {
            this._timer.interrupt();
        }
    }
}
