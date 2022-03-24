package com.lineage.server.timecontroller.server;

import com.lineage.server.model.Instance.L1TrapInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldTrap;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerTrapTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(ServerTrapTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 5000L, 5000L);
    }

    public void run() {
        try {
            HashMap<Integer, L1TrapInstance> allTrap = WorldTrap.get().map();
            if (!allTrap.isEmpty()) {
                for (Object iter : allTrap.values().toArray()) {
                    L1TrapInstance trap = (L1TrapInstance) iter;
                    if (!trap.isEnable() && trap.getSpan() > 0) {
                        trap.set_stop(trap.get_stop() + 1);
                        if (trap.get_stop() >= trap.getSpan()) {
                            trap.resetLocation();
                            Thread.sleep(50);
                        }
                    }
                }
            }
        } catch (Exception io) {
            _log.error("陷阱召喚處理時間軸異常重啟", io);
            GeneralThreadPool.get().cancel(this._timer, false);
            new ServerTrapTimer().start();
        }
    }
}
