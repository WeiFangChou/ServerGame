package com.lineage.server.timecontroller.quest;

import com.lineage.data.quest.ADLv80_2;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldQuest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AD80_2_Timer extends TimerTask {
    private static final Log _log = LogFactory.getLog(AD80_2_Timer.class);
    private static Random _random = new Random();
    private int _qid = -1;
    private ScheduledFuture<?> _timer;

    public void start() {
        this._qid = ADLv80_2.QUEST.get_id();
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 5000L, 5000L);
    }

    public void run() {
        try {
            ArrayList<L1QuestUser> questList = WorldQuest.get().getQuests(this._qid);
            if (!questList.isEmpty()) {
                for (Object object : questList.toArray()) {
                    Iterator<L1PcInstance> it = ((L1QuestUser) object).pcList().iterator();
                    while (it.hasNext()) {
                        L1PcInstance tgpc = it.next();
                        if (!tgpc.isDead() && checkLoc(tgpc) && _random.nextBoolean()) {
                            int skillid = -1;
                            int gfxid = -1;
                            switch (_random.nextInt(3)) {
                                case 0:
                                    skillid = L1SkillId.ADLV80_2_1;
                                    gfxid = 7781;
                                    break;
                                case 1:
                                    skillid = L1SkillId.ADLV80_2_2;
                                    gfxid = 7782;
                                    break;
                                case 2:
                                    skillid = L1SkillId.ADLV80_2_3;
                                    gfxid = 7780;
                                    break;
                            }
                            if (skillid != -1 && !tgpc.hasSkillEffect(skillid)) {
                                tgpc.sendPacketsX8(new S_SkillSound(tgpc.getId(), gfxid));
                                tgpc.setSkillEffect(skillid, 12000);
                            }
                            Thread.sleep(50);
                        }
                    }
                }
                questList.clear();
            }
        } catch (Exception e) {
            _log.error("水龍副本 傷害計能施放 時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new AD80_2_Timer().start();
        }
    }

    private static boolean checkLoc(L1PcInstance tgpc) {
        if (!tgpc.hasSkillEffect(L1SkillId.ADLV80_2_1) && !tgpc.hasSkillEffect(L1SkillId.ADLV80_2_2) && !tgpc.hasSkillEffect(L1SkillId.ADLV80_2_3) && tgpc.getX() > 32942 && tgpc.getX() < 32980 && tgpc.getY() > 32810 && tgpc.getY() < 32871) {
            return true;
        }
        return false;
    }
}
