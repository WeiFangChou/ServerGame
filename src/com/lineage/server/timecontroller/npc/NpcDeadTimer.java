package com.lineage.server.timecontroller.npc;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldNpc;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NpcDeadTimer extends TimerTask {
    private static NpcDeadTimer _instance;
    private static final Log _log = LogFactory.getLog(NpcDeadTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 5000L, 5000L);
    }

    public static NpcDeadTimer get() {
        if (_instance == null) {
            _instance = new NpcDeadTimer();
        }
        return _instance;
    }

    public void run() {
        try {
            Collection<L1NpcInstance> allMob = WorldNpc.get().all();
            if (!allMob.isEmpty()) {
                for (L1NpcInstance npc : allMob) {
                    if (npc != null && npc.getMaxHp() > 0 && npc.isDead() && npc.get_deadTimerTemp() != -1) {
                        npc.set_deadTimerTemp(npc.get_deadTimerTemp() - 5);
                        if (npc.get_deadTimerTemp() <= 0) {
                            npc.deleteMe();
                        }
                        Thread.sleep(50);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("NPC死亡時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new NpcDeadTimer().start();
        }
    }
}
