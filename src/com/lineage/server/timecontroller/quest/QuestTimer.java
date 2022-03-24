package com.lineage.server.timecontroller.quest;

import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_HelpMessage;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldQuest;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QuestTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(QuestTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 1000L, 1000L);
    }

    public void run() {
        try {
            Collection<L1QuestUser> allQuest = WorldQuest.get().all();
            if (!allQuest.isEmpty()) {
                for (L1QuestUser quest : allQuest) {
                    if (quest.get_time() > -1) {
                        setQuest(quest);
                        Thread.sleep(50);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("副本任務可執行時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new QuestTimer().start();
        }
    }

    private static void setQuest(L1QuestUser quest) {
        try {
            switch (quest.get_time()) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 10:
                case 15:
                case 30:
                    quest.sendPackets(new S_HelpMessage("\\fV副本任務-剩餘時間：" + quest.get_time()));
                    break;
                case 60:
                case OpcodesServer.S_OPCODE_STRUP:
                case OpcodesServer.S_OPCODE_INVLIST:
                case 240:
                case 300:
                case 600:
                case 900:
                case 1800:
                case 3600:
                    quest.sendPackets(new S_HelpMessage("\\fV副本任務-剩餘時間：" + (quest.get_time() / 60)));
                    break;
            }
            quest.set_time(quest.get_time() - 1);
            if (quest.get_time() == 0) {
                quest.sendPackets(new S_ServerMessage(3112));
                quest.set_time(-1);
                quest.endQuest();
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
