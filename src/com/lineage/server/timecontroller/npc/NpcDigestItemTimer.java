package com.lineage.server.timecontroller.npc;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldMob;
import java.util.Collection;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NpcDigestItemTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(NpcDigestItemTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 5000L, 5000L);
    }

    public void run() {
        try {
            Collection<L1MonsterInstance> allMob = WorldMob.get().all();
            if (!allMob.isEmpty()) {
                for (L1MonsterInstance mob : allMob) {
                    if (mob != null && mob.getMaxHp() > 0 && !mob.isDead() && mob.getNpcTemplate().get_digestitem() > 0 && !mob.getDigestItemEmpty()) {
                        checkDelItem(mob);
                        Thread.sleep(50);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("NPC消化道具時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new NpcDigestItemTimer().start();
        }
    }

    private void checkDelItem(L1MonsterInstance mob) {
        if (mob != null) {
            try {
                if (!mob.isDead()) {
                    HashMap<L1ItemInstance, L1NpcInstance.DelItemTime> allItem = mob.getDigestItem();
                    if (!allItem.isEmpty()) {
                        int count = allItem.size();
                        for (L1ItemInstance key : allItem.keySet()) {
                            L1NpcInstance.DelItemTime value = allItem.get(key);
                            if (value._del_item_time <= 0) {
                                count--;
                            } else {
                                value._del_item_time -= 5;
                                if (value._del_item_time <= 0) {
                                    mob.getInventory().removeItem(key, key.getCount());
                                }
                            }
                        }
                        if (count <= 0) {
                            mob.getDigestItemClear();
                        }
                    }
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
