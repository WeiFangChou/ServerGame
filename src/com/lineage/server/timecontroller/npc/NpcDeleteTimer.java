package com.lineage.server.timecontroller.npc;

import com.lineage.server.model.Instance.L1IllusoryInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldNpc;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NpcDeleteTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(NpcDeleteTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 2000L, 2000L);
    }

    public void run() {
        try {
            Collection<L1NpcInstance> allNpc = WorldNpc.get().all();
            if (!allNpc.isEmpty()) {
                for (L1NpcInstance npc : allNpc) {
                    if (npc.is_spawnTime()) {
                        int time = npc.get_spawnTime() - 2;
                        if (time > 0) {
                            npc.set_spawnTime(time);
                        } else {
                            remove(npc);
                        }
                        Thread.sleep(50);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("NPC存在時間時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new NpcDeleteTimer().start();
        }
    }

    private static void remove(L1NpcInstance tgnpc) {
        boolean isRemove = false;
        try {
            if (tgnpc instanceof L1MonsterInstance) {
                if (tgnpc.getNpcId() == 80034) {
                    tgnpc.outParty(tgnpc);
                }
                isRemove = true;
            }
            if (tgnpc instanceof L1IllusoryInstance) {
                isRemove = true;
            }
            if (isRemove) {
                tgnpc.setCurrentHpDirect(0);
                tgnpc.setDead(true);
                tgnpc.getMap().setPassable(tgnpc.getLocation(), true);
                tgnpc.setExp(0);
                tgnpc.setKarma(0);
                tgnpc.allTargetClear();
            }
            tgnpc.deleteMe();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
