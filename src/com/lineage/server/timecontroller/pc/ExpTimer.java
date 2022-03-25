package com.lineage.server.timecontroller.pc;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.world.World;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* renamed from: com.lineage.server.timecontroller.pc.ExpTimer */
public class ExpTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(ExpTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, 500, 500);
    }

    public void run() {
        try {
            Collection<L1PcInstance> all = World.get().getAllPlayers();
            if (!all.isEmpty()) {
                for (L1PcInstance tgpc : all) {
                    if (check(tgpc)) {
                        tgpc.onChangeExp();
                        Thread.sleep(1);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("EXP更新處理時間軸異常重啟", e);
            PcOtherThreadPool.get().cancel(this._timer, false);
            new ExpTimer().start();
        }
    }

    private static boolean check(L1PcInstance tgpc) {
        if (tgpc == null) {
            return false;
        }
        try {
            if (tgpc.getOnlineStatus() == 0 || tgpc.getNetConnection() == null || tgpc.isTeleport() || tgpc.getExp() == tgpc.getExpo()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
