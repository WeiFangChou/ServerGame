package com.lineage.server.timecontroller.pc;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.world.World;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* renamed from: com.lineage.server.timecontroller.pc.PcHellTimer */
public class PcHellTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(PcHellTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, 1100, 1100);
    }

    public void run() {
        try {
            Collection<L1PcInstance> all = World.get().getAllPlayers();
            if (!all.isEmpty()) {
                for (L1PcInstance tgpc : all) {
                    int time = tgpc.getHellTime();
                    if (time > 0) {
                        check(tgpc, Integer.valueOf(time - 1));
                        Thread.sleep(1);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("PC 地獄模式處理時間軸異常重啟", e);
            PcOtherThreadPool.get().cancel(this._timer, false);
            new PcHellTimer().start();
        }
    }

    private static void check(L1PcInstance tgpc, Integer time) {
        if (time.intValue() > 0) {
            tgpc.setHellTime(time.intValue());
            return;
        }
        tgpc.setHellTime(0);
        if (tgpc.getNetConnection() != null) {
            outPc(tgpc);
        }
    }

    private static void outPc(L1PcInstance tgpc) {
        if (tgpc != null) {
            try {
                tgpc.endHell();
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
