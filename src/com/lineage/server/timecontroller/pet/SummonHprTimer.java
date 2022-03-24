package com.lineage.server.timecontroller.pet;

import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldSummons;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SummonHprTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(SummonHprTimer.class);
    private static int _time = 0;
    private ScheduledFuture<?> _timer;

    public void start() {
        _time = 0;
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 1000L, 1000L);
    }

    public void run() {
        try {
            _time++;
            Collection<L1SummonInstance> allPet = WorldSummons.get().all();
            if (!allPet.isEmpty()) {
                for (L1SummonInstance summon : allPet) {
                    if (HprPet.hpUpdate(summon, _time)) {
                        Thread.sleep(5);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("Summon HP自然回復時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new SummonHprTimer().start();
        }
    }
}
