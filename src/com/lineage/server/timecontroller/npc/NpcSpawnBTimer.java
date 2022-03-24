package com.lineage.server.timecontroller.npc;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.thread.GeneralThreadPool;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NpcSpawnBTimer extends TimerTask {
    public static final Map<L1NpcInstance, Long> MAP = new ConcurrentHashMap();
    private static final Log _log = LogFactory.getLog(NpcSpawnBTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 60000L, 60000L);
    }

    public void run() {
        try {
            if (!MAP.isEmpty()) {
                for (L1NpcInstance npc : MAP.keySet()) {
                    Long time = MAP.get(npc);
                    long t = time.longValue() - 60;
                    if (time.longValue() > 0) {
                        MAP.put(npc, Long.valueOf(t));
                    } else {
                        spawn(npc);
                        MAP.remove(npc);
                    }
                    Thread.sleep(50);
                }
            }
        } catch (Exception e) {
            _log.error("NPC(BOSS)召喚時間時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new NpcSpawnBTimer().start();
        }
    }

    private static void spawn(L1NpcInstance npc) {
        try {
            npc.getSpawn().executeSpawnTask(npc.getSpawnNumber(), npc.getId());
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
