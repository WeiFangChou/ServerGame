package com.lineage.server.model;

import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.thread.GeneralThreadPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1CurseParalysis extends L1Paralysis {
    private static final Log _log = LogFactory.getLog(L1CurseParalysis.class);
    private final int _delay;
    private final L1Character _target;
    private final int _time;
    private Thread _timer;

    /* access modifiers changed from: private */
    public class ParalysisDelayTimer extends Thread {
        private ParalysisDelayTimer() {
        }

        /* synthetic */ ParalysisDelayTimer(L1CurseParalysis l1CurseParalysis, ParalysisDelayTimer paralysisDelayTimer) {
            this();
        }

        public void run() {
            L1CurseParalysis.this._target.setSkillEffect(L1SkillId.STATUS_CURSE_PARALYZING, 0);
            try {
                Thread.sleep((long) L1CurseParalysis.this._delay);
                if (L1CurseParalysis.this._target instanceof L1PcInstance) {
                    L1PcInstance player = (L1PcInstance) L1CurseParalysis.this._target;
                    if (!player.isDead()) {
                        player.sendPackets(new S_Paralysis(1, true));
                    }
                }
                L1CurseParalysis.this._target.setParalyzed(true);
                L1CurseParalysis.this._timer = new ParalysisTimer(L1CurseParalysis.this, null);
                GeneralThreadPool.get().execute(L1CurseParalysis.this._timer);
                if (isInterrupted()) {
                    L1CurseParalysis.this._timer.interrupt();
                }
            } catch (InterruptedException e) {
                L1CurseParalysis.this._target.killSkillEffectTimer(L1SkillId.STATUS_CURSE_PARALYZING);
                ModelError.isError(L1CurseParalysis._log, e.getLocalizedMessage(), e);
            }
        }
    }

    private class ParalysisTimer extends Thread {
        private ParalysisTimer() {
        }

        /* synthetic */ ParalysisTimer(L1CurseParalysis l1CurseParalysis, ParalysisTimer paralysisTimer) {
            this();
        }

        public void run() {
            L1CurseParalysis.this._target.killSkillEffectTimer(L1SkillId.STATUS_CURSE_PARALYZING);
            L1CurseParalysis.this._target.setSkillEffect(1011, 0);
            try {
                Thread.sleep((long) L1CurseParalysis.this._time);
            } catch (InterruptedException e) {
                ModelError.isError(L1CurseParalysis._log, e.getLocalizedMessage(), e);
            }
            L1CurseParalysis.this._target.killSkillEffectTimer(1011);
            if (L1CurseParalysis.this._target instanceof L1PcInstance) {
                L1PcInstance player = (L1PcInstance) L1CurseParalysis.this._target;
                if (!player.isDead()) {
                    player.sendPackets(new S_Paralysis(1, false));
                }
            }
            L1CurseParalysis.this._target.setParalyzed(false);
            L1CurseParalysis.this.cure();
        }
    }

    private L1CurseParalysis(L1Character cha, int delay, int time, int mode) {
        this._target = cha;
        this._delay = delay;
        this._time = time;
        curse(mode);
    }

    private void curse(int mode) {
        if (this._target instanceof L1PcInstance) {
            L1PcInstance player = (L1PcInstance) this._target;
            switch (mode) {
                case 1:
                    player.sendPackets(new S_ServerMessage(212));
                    break;
                case 2:
                    player.sendPackets(new S_ServerMessage(291));
                    break;
            }
        }
        this._target.setPoisonEffect(2);
        this._timer = new ParalysisDelayTimer(this, null);
        GeneralThreadPool.get().execute(this._timer);
    }

    public static boolean curse(L1Character cha, int delay, int time, int mode) {
        if ((!(cha instanceof L1PcInstance) && !(cha instanceof L1MonsterInstance)) || cha.hasSkillEffect(L1SkillId.STATUS_CURSE_PARALYZING) || cha.hasSkillEffect(1011)) {
            return false;
        }
        cha.setParalaysis(new L1CurseParalysis(cha, delay, time, mode));
        return true;
    }

    @Override // com.lineage.server.model.L1Paralysis
    public int getEffectId() {
        return 2;
    }

    @Override // com.lineage.server.model.L1Paralysis
    public void cure() {
        this._target.setPoisonEffect(0);
        this._target.setParalaysis(null);
        if (this._timer != null) {
            this._timer.interrupt();
        }
    }
}
