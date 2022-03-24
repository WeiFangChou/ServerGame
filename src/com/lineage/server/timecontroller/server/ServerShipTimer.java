package com.lineage.server.timecontroller.server;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.serverpackets.S_Teleport;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerShipTimer extends TimerTask {
    public static final Map<L1PcInstance, Long> MAP = new ConcurrentHashMap();
    private static final Log _log = LogFactory.getLog(ServerShipTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 5000L, 5000L);
    }

    public void run() {
        try {
            checkShipTime();
        } catch (Exception e) {
            _log.error("坐船系統時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new ServerShipTimer().start();
        }
    }

    private final void checkShipTime() throws Exception {
        int nowtime = L1GameTimeClock.getInstance().currentTime().getSeconds() % 86400;
        if ((nowtime >= 10800 && nowtime < 12600) || ((nowtime >= 21600 && nowtime < 23400) || ((nowtime >= 32400 && nowtime < 34200) || ((nowtime >= 43200 && nowtime < 45000) || ((nowtime >= 54000 && nowtime < 55800) || ((nowtime >= 64800 && nowtime < 66600) || ((nowtime >= 75600 && nowtime < 77400) || (nowtime >= 86400 && nowtime < 88200)))))))) {
            for (L1PcInstance pc : World.get().getAllPlayers()) {
                switch (pc.getMapId()) {
                    case 5:
                        pc.getInventory().consumeItem(40299, 1);
                        teleport(pc, 32540, 32728, (short) 4);
                        break;
                    case 84:
                        pc.getInventory().consumeItem(40301, 1);
                        teleport(pc, 33426, 33501, (short) 4);
                        break;
                    case 447:
                        pc.getInventory().consumeItem(40302, 1);
                        teleport(pc, 32297, 33086, (short) 440);
                        break;
                }
            }
        } else if ((nowtime >= 5400 && nowtime < 7200) || ((nowtime >= 16200 && nowtime < 18000) || ((nowtime >= 27000 && nowtime < 28800) || ((nowtime >= 37800 && nowtime < 39600) || ((nowtime >= 48600 && nowtime < 50400) || ((nowtime >= 59400 && nowtime < 61200) || ((nowtime >= 70200 && nowtime < 72000) || (nowtime >= 81000 && nowtime < 82800)))))))) {
            for (L1PcInstance pc2 : World.get().getAllPlayers()) {
                switch (pc2.getMapId()) {
                    case 6:
                        pc2.getInventory().consumeItem(40298, 1);
                        teleport(pc2, 32631, 32982, (short) 0);
                        break;
                    case 83:
                        pc2.getInventory().consumeItem(40300, 1);
                        teleport(pc2, 32936, 33057, (short) 70);
                        break;
                    case 446:
                        pc2.getInventory().consumeItem(40303, 1);
                        teleport(pc2, 32751, 32873, (short) 445);
                        break;
                }
            }
        }
    }

    private final void teleport(L1PcInstance tgpc, int locx, int locy, short mapid) {
        tgpc.setTeleportX(locx);
        tgpc.setTeleportY(locy);
        tgpc.setTeleportMapId(mapid);
        tgpc.sendPackets(new S_Teleport());
    }
}
