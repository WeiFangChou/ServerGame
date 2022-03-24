package com.lineage.server.timecontroller.npc;

import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldMob;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NpcHprTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(NpcHprTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 5000L, 5000L);
    }

    public void run() {
        try {
            Collection<L1MonsterInstance> allMob = WorldMob.get().all();
            if (allMob.isEmpty()) {
                _log.error("Npc HP自然回復時間軸異常重啟 allMob.isEmpty()");
                return;
            }
            for (L1MonsterInstance mob : allMob) {
                if (mob.isHpR()) {
                    hpUpdate(mob);
                }
            }
        } catch (Exception e) {
            _log.error("Npc HP自然回復時間軸異常重啟", e);
            if (this._timer != null) {
                GeneralThreadPool.get().cancel(this._timer, false);
            }
            new NpcHprTimer().start();
        }
    }

    private static void hpUpdate(L1MonsterInstance mob) {
        int hprInterval = mob.getNpcTemplate().get_hprinterval();
        if (hprInterval <= 0) {
            hprInterval = 10;
        }
        long nowtime = System.currentTimeMillis() / 1000;
        if (nowtime - mob.getLastHprTime() >= ((long) hprInterval)) {
            int hpr = mob.getNpcTemplate().get_hpr();
            if (hpr <= 0) {
                hpr = 2;
            }
            hprInterval(mob, hpr);
            mob.setLastHprTime(nowtime);
        }
    }

    private static void hprInterval(L1MonsterInstance mob, int hpr) {
        try {
            if (mob.isHpRegenerationX()) {
                mob.setCurrentHp(mob.getCurrentHp() + hpr);
            }
        } catch (Exception e) {
            _log.error("Npc 執行回復HP發生異常", e);
            mob.deleteMe();
        }
    }
}
