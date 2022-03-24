package com.lineage.server.timecontroller.pet;

import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldPet;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PetHprTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(PetHprTimer.class);
    private static int _time = 0;
    private ScheduledFuture<?> _timer;

    public void start() {
        _time = 0;
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 1000L, 1000L);
    }

    public void run() {
        try {
            _time++;
            Collection<L1PetInstance> allPet = WorldPet.get().all();
            if (!allPet.isEmpty()) {
                for (L1PetInstance pet : allPet) {
                    if (HprPet.hpUpdate(pet, _time)) {
                        Thread.sleep(5);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("Pet HP自然回復時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new PetHprTimer().start();
        }
    }
}
