package com.lineage.server.timecontroller.pet;

import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldDoll;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DollAidTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(DollAidTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 10000L, 10000L);
    }

    public void run() {
        try {
            Collection<L1DollInstance> allDoll = WorldDoll.get().all();
            if (!allDoll.isEmpty()) {
                for (L1DollInstance doll : allDoll) {
                    if (doll.is_power_doll()) {
                        doll.startDollSkill();
                    }
                    Thread.sleep(50);
                }
            }
        } catch (Exception e) {
            _log.error("魔法娃娃處理時間軸(輔助技能)異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new DollAidTimer().start();
        }
    }
}
