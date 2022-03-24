package com.lineage.server.timecontroller.p002pc;

import com.lineage.server.datatables.lock.UpdateLocReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.world.World;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* renamed from: com.lineage.server.timecontroller.pc.UnfreezingTimer */
public class UnfreezingTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(UnfreezingTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, 1000, 1000);
    }

    public void run() {
        try {
            Collection<L1PcInstance> all = World.get().getAllPlayers();
            if (!all.isEmpty()) {
                for (L1PcInstance tgpc : all) {
                    if (tgpc.get_unfreezingTime() != 0) {
                        int time = tgpc.get_unfreezingTime() - 1;
                        tgpc.set_unfreezingTime(time);
                        if (time <= 0) {
                            UpdateLocReading.get().setPcLoc(tgpc.getAccountName());
                            tgpc.sendPackets(new S_ServerMessage("\\fR帳號內其他人物傳送回指定位置！"));
                            L1Teleport.teleport(tgpc, 32781, 32856, (short) 340, 5, true);
                        } else {
                            tgpc.sendPackets(new S_ServerMessage("\\fR" + time + "秒後傳送回指定位置！"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            _log.error("解除人物卡點時間軸異常重啟", e);
            PcOtherThreadPool.get().cancel(this._timer, false);
            new UnfreezingTimer().start();
        }
    }
}
