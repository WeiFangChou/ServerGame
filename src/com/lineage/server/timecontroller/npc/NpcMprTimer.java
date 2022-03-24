package com.lineage.server.timecontroller.npc;

import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldMob;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NpcMprTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(NpcMprTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 5000L, 5000L);
    }

    public void run() {
        try {
            Collection<L1MonsterInstance> allMob = WorldMob.get().all();
            if (!allMob.isEmpty()) {
                for (L1MonsterInstance mob : allMob) {
                    if (mob.isMpR()) {
                        mpUpdate(mob);
                        Thread.sleep(50);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("Npc MP自然回復時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new NpcMprTimer().start();
        }
    }

    private static void mpUpdate(L1MonsterInstance mob) {
        int mprInterval = mob.getNpcTemplate().get_mprinterval();
        if (mprInterval <= 0) {
            mprInterval = 10;
        }
        long nowtime = System.currentTimeMillis() / 1000;
        if (nowtime - mob.getLastMprTime() >= ((long) mprInterval)) {
            int mpr = mob.getNpcTemplate().get_mpr();
            if (mpr <= 0) {
                mpr = 2;
            }
            mprInterval(mob, mpr);
            mob.setLastMprTime(nowtime);
        }
    }

    private static void mprInterval(L1MonsterInstance mob, int mpr) {
        try {
            if (mob.isMpRegenerationX()) {
                mob.setCurrentMp(mob.getCurrentMp() + mpr);
            }
        } catch (Exception e) {
            _log.error("Npc 執行回復MP發生異常", e);
            mob.deleteMe();
        }
    }
}
