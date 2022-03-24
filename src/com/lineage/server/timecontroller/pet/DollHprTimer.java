package com.lineage.server.timecontroller.pet;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DollHprTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(DollHprTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 1000L, 1000L);
    }

    public void run() {
        try {
            Collection<L1PcInstance> all = World.get().getAllPlayers();
            if (!all.isEmpty()) {
                for (L1PcInstance tgpc : all) {
                    if (tgpc.get_doll_hpr_time_src() > 0 && checkErr(tgpc)) {
                        tgpc.setCurrentHp(tgpc.getCurrentHp() + tgpc.get_doll_hpr());
                        Thread.sleep(50);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("魔法娃娃處理時間軸(HPR)異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new DollHprTimer().start();
        }
    }

    private static boolean checkErr(L1PcInstance tgpc) {
        if (tgpc == null) {
            return false;
        }
        try {
            if (tgpc.getOnlineStatus() == 0 || tgpc.getNetConnection() == null || tgpc.isTeleport() || !tgpc.getHpRegeneration() || tgpc.getCurrentHp() >= tgpc.getMaxHp()) {
                return false;
            }
            int newtime = tgpc.get_doll_hpr_time() - 1;
            tgpc.set_doll_hpr_time(newtime);
            if (newtime > 0) {
                return false;
            }
            tgpc.set_doll_hpr_time(tgpc.get_doll_hpr_time_src());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
