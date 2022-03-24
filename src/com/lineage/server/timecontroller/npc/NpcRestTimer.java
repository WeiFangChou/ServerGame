package com.lineage.server.timecontroller.npc;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldNpc;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NpcRestTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(NpcRestTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 5000L, 5000L);
    }

    public void run() {
        try {
            Collection<L1NpcInstance> allMob = WorldNpc.get().all();
            if (!allMob.isEmpty()) {
                for (L1NpcInstance npc : allMob) {
                    if (npc != null && npc.get_stop_time() >= 0) {
                        npc.set_stop_time(npc.get_stop_time() - 5);
                        if (npc.get_stop_time() <= 0) {
                            npc.set_stop_time(-1);
                            npc.setRest(false);
                        }
                        Thread.sleep(50);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("NPC動作暫停時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new NpcRestTimer().start();
        }
    }
}
