package com.lineage.server.timecontroller.quest;

import com.lineage.data.quest.DarkElfLv50_1;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Location;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.WorldQuest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DE50A_Timer extends TimerTask {
    private static final Log _log = LogFactory.getLog(DE50A_Timer.class);
    private static Random _random = new Random();
    private int _qid = -1;
    private ScheduledFuture<?> _timer;

    public void start() {
        this._qid = DarkElfLv50_1.QUEST.get_id();
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 1500L, 1500L);
    }

    public void run() {
        try {
            ArrayList<L1QuestUser> questList = WorldQuest.get().getQuests(this._qid);
            if (!questList.isEmpty()) {
                for (Object object : questList.toArray()) {
                    L1QuestUser quest = (L1QuestUser) object;
                    Iterator<L1PcInstance> it = quest.pcList().iterator();
                    while (it.hasNext()) {
                        L1PcInstance tgpc = it.next();
                        if (!tgpc.isDead() && _random.nextInt(100) < 10) {
                            quest.addNpc(spawn(tgpc));
                            Thread.sleep(50);
                        }
                    }
                }
                questList.clear();
            }
        } catch (Exception e) {
            _log.error("尋找黑暗之星 (黑暗妖精50級以上官方任務)A時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new DE50A_Timer().start();
        }
    }

    private static L1MonsterInstance spawn(L1PcInstance tgpc) {
        L1MonsterInstance mob;
        try {
            L1Location loc = tgpc.getLocation().randomLocation(4, false);
            tgpc.sendPackets(new S_EffectLocation(loc, 3992));
            if (_random.nextBoolean()) {
                mob = L1SpawnUtil.spawnX(45582, loc, tgpc.get_showId());
            } else {
                mob = L1SpawnUtil.spawnX(45587, loc, tgpc.get_showId());
            }
            mob.setExp(1);
            return mob;
        } catch (Exception e) {
            return null;
        }
    }
}
