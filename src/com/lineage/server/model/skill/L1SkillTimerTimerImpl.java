package com.lineage.server.model.skill;

import com.lineage.server.model.L1Character;
import com.lineage.server.thread.GeneralThreadPool;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1SkillTimerTimerImpl implements L1SkillTimer, Runnable {
    private static final Log _log = LogFactory.getLog(L1SkillTimerTimerImpl.class);
    private final L1Character _cha;
    private ScheduledFuture<?> _future = null;
    private int _remainingTime;
    private final int _skillId;
    private final int _timeMillis;

    public L1SkillTimerTimerImpl(L1Character cha, int skillId, int timeMillis) {
        this._cha = cha;
        this._skillId = skillId;
        this._timeMillis = timeMillis;
        this._remainingTime = this._timeMillis / L1SkillId.STATUS_BRAVE;
    }

    public void run() {
        this._remainingTime--;
        if (this._remainingTime <= 0) {
            this._cha.removeSkillEffect(this._skillId);
        }
    }

    @Override // com.lineage.server.model.skill.L1SkillTimer
    public void begin() {
        this._future = GeneralThreadPool.get().scheduleAtFixedRate(this, 1000, 1000);
    }

    @Override // com.lineage.server.model.skill.L1SkillTimer
    public void end() {
        kill();
        try {
            L1SkillStop.stopSkill(this._cha, this._skillId);
        } catch (Throwable e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.skill.L1SkillTimer
    public void kill() {
        try {
            if (this._future != null) {
                this._future.cancel(false);
            }
        } catch (Throwable e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.skill.L1SkillTimer
    public int getRemainingTime() {
        return this._remainingTime;
    }
}
