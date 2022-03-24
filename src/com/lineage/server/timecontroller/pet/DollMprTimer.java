package com.lineage.server.timecontroller.pet;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DollMprTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(DollMprTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 1000L, 1000L);
    }

    public void run() {
        try {
            Collection<L1PcInstance> all = World.get().getAllPlayers();
            if (!all.isEmpty()) {
                for (L1PcInstance tgpc : all) {
                    if (tgpc.get_doll_mpr_time_src() > 0 && checkErr(tgpc)) {
                        tgpc.setCurrentMp(tgpc.getCurrentMp() + tgpc.get_doll_mpr());
                        Thread.sleep(50);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("魔法娃娃處理時間軸(MPR)異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new DollMprTimer().start();
        }
    }

    private static boolean checkErr(L1PcInstance tgpc) {
        if (tgpc == null) {
            return false;
        }
        try {
            if (tgpc.getOnlineStatus() == 0 || tgpc.getNetConnection() == null || tgpc.isTeleport() || !tgpc.getMpRegeneration() || tgpc.getCurrentMp() >= tgpc.getMaxMp()) {
                return false;
            }
            int newtime = tgpc.get_doll_mpr_time() - 1;
            tgpc.set_doll_mpr_time(newtime);
            if (newtime > 0) {
                return false;
            }
            tgpc.set_doll_mpr_time(tgpc.get_doll_mpr_time_src());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
