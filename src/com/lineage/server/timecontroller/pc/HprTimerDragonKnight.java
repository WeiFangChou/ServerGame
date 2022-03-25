package com.lineage.server.timecontroller.pc;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.world.WorldDragonKnight;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* renamed from: com.lineage.server.timecontroller.pc.HprTimerDragonKnight */
public class HprTimerDragonKnight extends TimerTask {
    private static final Log _log = LogFactory.getLog(HprTimerDragonKnight.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, 1000, 1000);
    }

    public void run() {
        try {
            Collection<L1PcInstance> allPc = WorldDragonKnight.get().all();
            if (!allPc.isEmpty()) {
                for (L1PcInstance tgpc : allPc) {
                    HprExecutor hpr = HprExecutor.get();
                    if (hpr.check(tgpc)) {
                        hpr.checkRegenHp(tgpc);
                        Thread.sleep(1);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("Pc(龍騎) HP自然回復時間軸異常重啟", e);
            PcOtherThreadPool.get().cancel(this._timer, false);
            new HprTimerDragonKnight().start();
        }
    }
}
