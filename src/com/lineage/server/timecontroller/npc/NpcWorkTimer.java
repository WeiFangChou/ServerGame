package com.lineage.server.timecontroller.npc;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.thread.GeneralThreadPool;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NpcWorkTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(NpcWorkTimer.class);
    private static final Map<L1NpcInstance, Integer> _map = new HashMap();
    private ScheduledFuture<?> _timer;

    public static void put(L1NpcInstance npc, Integer time) {
        _map.put(npc, time);
    }

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 2000L, 2000L);
    }

    public void run() {
        try {
            if (!_map.isEmpty()) {
                for (L1NpcInstance npc : _map.keySet()) {
                    Integer time = Integer.valueOf(_map.get(npc).intValue() - 2);
                    if (time.intValue() > 0) {
                        _map.put(npc, time);
                    } else {
                        startWork(npc);
                    }
                    Thread.sleep(50);
                }
            }
        } catch (Exception e) {
            _log.error("NPC工作時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new NpcWorkTimer().start();
        }
    }

    private static void startWork(L1NpcInstance npc) {
        if (npc != null) {
            try {
                int time = npc.WORK.workTime();
                if (time != 0) {
                    npc.WORK.work(npc);
                    _map.put(npc, Integer.valueOf(time));
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
