package com.lineage.server.timecontroller.npc;

import com.lineage.config.Config;
import com.lineage.server.datatables.NpcChatTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.templates.L1NpcChat;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldNpc;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.TimeZone;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NpcChatTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(NpcChatTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 60000L, 60000L);
    }

    public void run() {
        try {
            Collection<L1NpcChat> allChat = NpcChatTable.get().all();
            if (!allChat.isEmpty()) {
                for (L1NpcChat npcChat : allChat) {
                    if (isChatTime(npcChat.getGameTime())) {
                        int npcId = npcChat.getNpcId();
                        for (L1NpcInstance npc : WorldNpc.get().all()) {
                            if (npc.getNpcTemplate().get_npcId() == npcId) {
                                npc.startChat(3);
                            }
                        }
                    }
                    Thread.sleep(50);
                }
            }
        } catch (Exception e) {
            _log.error("NPC對話時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new NpcChatTimer().start();
        }
    }

    private boolean isChatTime(int chatTime) {
        return Integer.valueOf(new SimpleDateFormat("HHmm").format(getRealTime().getTime())).intValue() == chatTime;
    }

    private static Calendar getRealTime() {
        return Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE));
    }
}
