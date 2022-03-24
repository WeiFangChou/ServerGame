package com.lineage.server.timecontroller.server;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_PacketBoxGame;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerUseMapTimer extends TimerTask {
    public static final Map<L1PcInstance, Integer> MAP = new ConcurrentHashMap();
    private static final Log _log = LogFactory.getLog(ServerUseMapTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 1100L, 1100L);
    }

    public static void put(L1PcInstance pc, int time) {
        pc.sendPackets(new S_PacketBoxGame(71, time));
        pc.get_other().set_usemapTime(time);
        MAP.put(pc, new Integer(time));
    }

    public void run() {
        try {
            if (!MAP.isEmpty()) {
                for (L1PcInstance key : MAP.keySet()) {
                    Integer value = Integer.valueOf(MAP.get(key).intValue() - 1);
                    if (value.intValue() <= 0) {
                        teleport(key);
                        Thread.sleep(1);
                    } else {
                        key.get_other().set_usemapTime(value.intValue());
                        MAP.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("計時地圖時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new ServerUseMapTimer().start();
        }
    }

    public static void teleport(L1PcInstance tgpc) {
        MAP.remove(tgpc);
        if (World.get().getPlayer(tgpc.getName()) != null) {
            if (tgpc.getMapId() == tgpc.get_other().get_usemap()) {
                L1Teleport.teleport(tgpc, 33080, 33392, (short) 4, 5, true);
            }
            tgpc.get_other().set_usemapTime(0);
            tgpc.get_other().set_usemap(-1);
            tgpc.sendPackets(new S_PacketBoxGame(72));
        }
    }
}
