package com.lineage.server.timecontroller.event;

import com.lineage.server.datatables.lock.VIPReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import java.sql.Timestamp;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class VIPTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(VIPTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 60000L, 60000L);
    }

    public void run() {
        try {
            Map<Integer, Timestamp> map = VIPReading.get().map();
            if (!map.isEmpty()) {
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                for (Integer objid : map.keySet()) {
                    if (map.get(objid).before(ts)) {
                        VIPReading.get().delOther(objid.intValue());
                        checkVIP(objid);
                    }
                    Thread.sleep(1);
                }
            }
        } catch (Exception e) {
            _log.error("VIP計時時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new VIPTimer().start();
        }
    }

    private static void checkVIP(Integer objid) {
        try {
            L1Object target = World.get().findObject(objid.intValue());
            if (target != null && 0 != 0 && (target instanceof L1PcInstance)) {
                L1PcInstance tgpc = (L1PcInstance) target;
                L1Teleport.teleport(tgpc, 33080, 33392, (short) 4, 5, true);
                tgpc.sendPackets(new S_ServerMessage("VIP時間終止"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
