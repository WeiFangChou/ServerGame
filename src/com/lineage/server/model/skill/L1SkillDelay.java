package com.lineage.server.model.skill;

import com.lineage.server.model.L1Character;
import com.lineage.server.thread.GeneralThreadPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1SkillDelay {
    private static final Log _log = LogFactory.getLog(L1SkillDelay.class);

    private L1SkillDelay() {
    }

    /* access modifiers changed from: package-private */
    public static class SkillDelayTimer implements Runnable {
        private L1Character _cha;

        public SkillDelayTimer(L1Character cha) {
            this._cha = cha;
        }

        public void run() {
            stopDelayTimer();
        }

        public void stopDelayTimer() {
            this._cha.setSkillDelay(false);
        }
    }

    public static void onSkillUse(L1Character cha, int time) {
        try {
            cha.setSkillDelay(true);
            GeneralThreadPool.get().schedule(new SkillDelayTimer(cha), (long) time);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
