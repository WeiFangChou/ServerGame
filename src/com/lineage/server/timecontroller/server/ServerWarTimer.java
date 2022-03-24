package com.lineage.server.timecontroller.server;

import com.lineage.server.thread.GeneralThreadPool;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerWarTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(ServerWarTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 60000L, 60000L);
    }

    public void run() {
        try {
            ServerWarExecutor.get().checkWarTime();
        } catch (Exception e) {
            _log.error("城戰計時時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new ServerWarTimer().start();
        }
    }
}
