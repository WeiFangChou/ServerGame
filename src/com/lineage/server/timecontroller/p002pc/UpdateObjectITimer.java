package com.lineage.server.timecontroller.p002pc;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.world.WorldIllusionist;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* renamed from: com.lineage.server.timecontroller.pc.UpdateObjectITimer */
public class UpdateObjectITimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(UpdateObjectITimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, 350, 350);
    }

    public void run() {
        try {
            Collection<L1PcInstance> allPc = WorldIllusionist.get().all();
            if (!allPc.isEmpty()) {
                for (L1PcInstance tgpc : allPc) {
                    if (UpdateObjectCheck.check(tgpc)) {
                        tgpc.updateObject();
                    }
                }
            }
        } catch (Exception e) {
            _log.error("Pc 可見物更新處理時間軸(幻術)異常重啟", e);
            PcOtherThreadPool.get().cancel(this._timer, false);
            new UpdateObjectITimer().start();
        }
    }
}
