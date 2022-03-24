package com.lineage.server.timecontroller.pet;

import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldDoll;
import java.util.Collection;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DollTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(DollTimer.class);
    private static Random _random = new Random();
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 60000L, 60000L);
    }

    public void run() {
        try {
            Collection<L1DollInstance> allDoll = WorldDoll.get().all();
            if (!allDoll.isEmpty()) {
                for (L1DollInstance doll : allDoll) {
                    int time = doll.get_time() - 60;
                    if (time <= 0) {
                        outDoll(doll);
                    } else {
                        doll.set_time(time);
                        if (!doll.isDead()) {
                            checkAction(doll);
                        }
                    }
                    Thread.sleep(50);
                }
            }
        } catch (Exception e) {
            _log.error("魔法娃娃處理時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new DollTimer().start();
        }
    }

    private static void outDoll(L1DollInstance doll) {
        if (doll != null) {
            try {
                if (!doll.destroyed()) {
                    doll.deleteDoll();
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    private void checkAction(L1DollInstance doll) {
        try {
            if (doll.getX() == doll.get_olX() && doll.getY() == doll.get_olY() && _random.nextInt(100) + 1 <= 76) {
                int actionCode = 66;
                if (_random.nextInt(10) <= 3) {
                    actionCode = 67;
                }
                doll.broadcastPacketAll(new S_DoActionGFX(doll.getId(), actionCode));
            }
            doll.set_olX(doll.getX());
            doll.set_olY(doll.getY());
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
