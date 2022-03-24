package com.lineage.server.timecontroller.server;

import com.lineage.server.datatables.LightSpawnTable;
import com.lineage.server.model.Instance.L1FieldObjectInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerLightTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(ServerLightTimer.class);
    private static boolean isSpawn = false;
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 60000L, 60000L);
    }

    public void run() {
        try {
            checkLightTime();
        } catch (Exception e) {
            _log.error("照明物件召喚時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new ServerLightTimer().start();
        }
    }

    private static void checkLightTime() {
        try {
            int nowTime = L1GameTimeClock.getInstance().currentTime().getSeconds() % 86400;
            if (nowTime < 21300 || nowTime >= 64500) {
                if (((nowTime >= 64500 && nowTime <= 86400) || (nowTime >= 0 && nowTime < 21300)) && !isSpawn) {
                    isSpawn = true;
                    LightSpawnTable.getInstance();
                }
            } else if (isSpawn) {
                isSpawn = false;
                for (L1Object object : World.get().getObject()) {
                    if (object instanceof L1FieldObjectInstance) {
                        L1FieldObjectInstance npc = (L1FieldObjectInstance) object;
                        if ((npc.getNpcTemplate().get_npcId() == 81177 || npc.getNpcTemplate().get_npcId() == 81178 || npc.getNpcTemplate().get_npcId() == 81179 || npc.getNpcTemplate().get_npcId() == 81180 || npc.getNpcTemplate().get_npcId() == 81181) && (npc.getMapId() == 0 || npc.getMapId() == 4)) {
                            npc.deleteMe();
                        }
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
