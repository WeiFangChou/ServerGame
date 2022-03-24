package com.lineage.server.timecontroller.pet;

import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldSummons;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SummonTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(SummonTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 60000L, 60000L);
    }

    public void run() {
        try {
            Collection<L1SummonInstance> allPet = WorldSummons.get().all();
            if (!allPet.isEmpty()) {
                for (L1SummonInstance summon : allPet) {
                    int time = summon.get_time() - 60;
                    if (time <= 0) {
                        outSummon(summon);
                    } else {
                        summon.set_time(time);
                    }
                    Thread.sleep(50);
                }
            }
        } catch (Exception e) {
            _log.error("召喚獸處理時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new SummonTimer().start();
        }
    }

    private static void outSummon(L1SummonInstance summon) {
        if (summon != null) {
            try {
                if (!summon.destroyed()) {
                    if (summon.tamed()) {
                        summon.deleteMe();
                    } else {
                        summon.Death(null);
                    }
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
