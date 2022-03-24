package com.lineage.server.timecontroller.quest;

import com.lineage.data.quest.WizardLv30_1;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1MerchantInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldQuest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class W30_Timer extends TimerTask {
    private static final Log _log = LogFactory.getLog(W30_Timer.class);
    private int _qid = -1;
    private ScheduledFuture<?> _timer;

    public void start() {
        this._qid = WizardLv30_1.QUEST.get_id();
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 1000L, 1000L);
    }

    public void run() {
        try {
            HashMap<Integer, L1Object> mapList = new HashMap<>();
            mapList.putAll(World.get().getVisibleObjects(201));
            ArrayList<L1QuestUser> questList = WorldQuest.get().getQuests(this._qid);
            Object[] array = questList.toArray();
            int length = array.length;
            for (int i = 0; i < length; i++) {
                L1QuestUser quest = (L1QuestUser) array[i];
                int i2 = 0;
                for (L1Object obj : mapList.values()) {
                    if (!(obj instanceof L1MerchantInstance) && obj.get_showId() == quest.get_id()) {
                        if (obj.getX() == 32867 && obj.getY() == 32912) {
                            i2++;
                        }
                        if (obj.getX() == 32867 && obj.getY() == 32927) {
                            i2++;
                        }
                        if (obj.getX() == 32860 && obj.getY() == 32920) {
                            i2++;
                        }
                        if (obj.getX() == 32875 && obj.getY() == 32920) {
                            i2++;
                        }
                    }
                }
                if (i2 >= 4) {
                    for (L1NpcInstance npc : quest.npcList()) {
                        if (npc instanceof L1DoorInstance) {
                            L1DoorInstance door = (L1DoorInstance) npc;
                            if (door.getDoorId() == 10003) {
                                door.open();
                                Thread.sleep(50);
                            }
                        }
                    }
                    Iterator<L1PcInstance> it = quest.pcList().iterator();
                    while (it.hasNext()) {
                        L1PcInstance pc = it.next();
                        if (pc.getX() == 32868 && pc.getY() == 32919) {
                            L1Teleport.teleport(pc, 32929, 32798, (short) 201, 5, true);
                            Thread.sleep(50);
                        }
                    }
                } else {
                    for (L1NpcInstance npc2 : quest.npcList()) {
                        if (npc2 instanceof L1DoorInstance) {
                            L1DoorInstance door2 = (L1DoorInstance) npc2;
                            if (door2.getDoorId() == 10003) {
                                door2.close();
                                Thread.sleep(50);
                            }
                        }
                    }
                }
            }
            mapList.clear();
            questList.clear();
        } catch (Exception e) {
            _log.error("不死族的叛徒 (法師30級以上官方任務)時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new W30_Timer().start();
        }
    }
}
