package com.lineage.server.timecontroller.event;

import com.lineage.config.ConfigDescs;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_HelpMessage;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NewServerTime extends TimerTask {
    private static final Log _log = LogFactory.getLog(NewServerTime.class);
    private int _count = 1;
    private int _time = 0;
    private ScheduledFuture<?> _timer;

    public void start(int time) {
        this._time = time;
        int timeMillis = time * 60 * L1SkillId.STATUS_BRAVE;
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, (long) timeMillis, (long) timeMillis);
    }

    public void run() {
        try {
            World.get().broadcastPacketToAll(new S_HelpMessage(ConfigDescs.getShow(this._count)));
            this._count++;
            if (this._count >= ConfigDescs.get_show_size()) {
                this._count = 1;
            }
        } catch (Exception e) {
            _log.error("服務器介紹與教學時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new NewServerTime().start(this._time);
        }
    }
}
