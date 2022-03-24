package com.lineage.server.timecontroller.npc;

import com.lineage.server.model.Instance.L1BowInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldBow;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NpcBowTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(NpcBowTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 1500L, 1500L);
    }

    public void run() {
        try {
            HashMap<Integer, L1BowInstance> allBow = WorldBow.get().map();
            if (!allBow.isEmpty()) {
                for (Object iter : allBow.values().toArray()) {
                    L1BowInstance bowNpc = (L1BowInstance) iter;
                    if (bowNpc != null && !bowNpc.isDead()) {
                        if (bowNpc.get_start() && bowNpc.checkPc()) {
                            bowNpc.atkTrag();
                        }
                        Thread.sleep(50);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("固定攻擊器時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new NpcBowTimer().start();
        }
    }
}
