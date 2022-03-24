package com.lineage.server.timecontroller.event;

import com.lineage.server.datatables.UBTable;
import com.lineage.server.model.L1UltimateBattle;
import com.lineage.server.serverpackets.S_PacketBoxGree1;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UbTime extends TimerTask {
    private static final Log _log = LogFactory.getLog(UbTime.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 16500L, 16500L);
    }

    public void run() {
        try {
            checkUbTime();
        } catch (Exception e) {
            _log.error("無線大賽時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new UbTime().start();
        }
    }

    private static void checkUbTime() {
        for (L1UltimateBattle ub : UBTable.getInstance().getAllUb()) {
            if (ub.checkUbTime() && !ub.isActive()) {
                ub.start();
                World.get().broadcastPacketToAll(new S_PacketBoxGree1(2, "【" + ub.getName() + "】５分鐘後即將開始，想參賽者請趕快入場。"));
                World.get().broadcastPacketToAll(new S_SystemMessage("【" + ub.getName() + "】５分鐘後即將開始，想參賽者請趕快入場。"));
            }
        }
    }
}
