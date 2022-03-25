package com.lineage.server.timecontroller.pc;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.world.World;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* renamed from: com.lineage.server.timecontroller.pc.PcGhostTimer */
public class PcGhostTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(PcGhostTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, 1100, 1100);
    }

    public void run() {
        try {
            Collection<L1PcInstance> all = World.get().getAllPlayers();
            if (!all.isEmpty()) {
                for (L1PcInstance tgpc : all) {
                    if (tgpc.isGhost()) {
                        check(tgpc, Integer.valueOf(tgpc.get_ghostTime() - 1));
                        Thread.sleep(1);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("PC 鬼魂模式處理時間軸異常重啟", e);
            PcOtherThreadPool.get().cancel(this._timer, false);
            new PcGhostTimer().start();
        }
    }

    private static void check(L1PcInstance tgpc, Integer time) {
        if (time.intValue() <= 0) {
            tgpc.set_ghostTime(-1);
            if (tgpc.getNetConnection() != null) {
                outPc(tgpc);
            }
        } else if (!tgpc.isDead()) {
            tgpc.set_ghostTime(-1);
        } else {
            tgpc.set_ghostTime(time.intValue());
        }
    }

    private static void outPc(L1PcInstance tgpc) {
        if (tgpc != null) {
            try {
                tgpc.makeReadyEndGhost();
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
