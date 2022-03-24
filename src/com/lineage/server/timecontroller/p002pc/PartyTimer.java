package com.lineage.server.timecontroller.p002pc;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxParty;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.world.World;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* renamed from: com.lineage.server.timecontroller.pc.PartyTimer */
public class PartyTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(PartyTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, 5000, 5000);
    }

    public void run() {
        try {
            Collection<L1PcInstance> all = World.get().getAllPlayers();
            if (!all.isEmpty()) {
                for (L1PcInstance tgpc : all) {
                    if (tgpc.getParty() != null) {
                        m12rp(tgpc);
                        Thread.sleep(1);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("隊伍更新時間軸異常重啟", e);
            PcOtherThreadPool.get().cancel(this._timer, false);
            new PartyTimer().start();
        }
    }

    /* renamed from: rp */
    private void m12rp(L1PcInstance pc) {
        try {
            pc.sendPackets(new S_PacketBoxParty(pc.getParty(), pc));
        } catch (Exception ignored) {
        }
    }
}
