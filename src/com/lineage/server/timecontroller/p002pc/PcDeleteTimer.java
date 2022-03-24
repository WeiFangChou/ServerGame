package com.lineage.server.timecontroller.p002pc;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeName;
import com.lineage.server.serverpackets.S_PacketBoxSelect;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.world.World;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* renamed from: com.lineage.server.timecontroller.pc.PcDeleteTimer */
public class PcDeleteTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(PcDeleteTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, 1100, 1100);
    }

    public void run() {
        try {
            Collection<L1PcInstance> all = World.get().getAllPlayers();
            if (!all.isEmpty()) {
                for (L1PcInstance tgpc : all) {
                    if (tgpc.isDead() && tgpc.get_delete_time() > 0) {
                        tgpc.set_delete_time(tgpc.get_delete_time() - 1);
                        if (tgpc.get_delete_time() <= 0) {
                            outPc(tgpc);
                        }
                    }
                }
            }
        } catch (Exception e) {
            _log.error("PC 死亡刪除處理時間軸異常重啟", e);
            PcOtherThreadPool.get().cancel(this._timer, false);
            new PcDeleteTimer().start();
        }
    }

    private static void outPc(L1PcInstance tgpc) {
        try {
            if (tgpc.isDead()) {
                ClientExecutor client = tgpc.getNetConnection();
                tgpc.sendPacketsAll(new S_ChangeName(tgpc, false));
                client.quitGame();
                _log.info("角色死亡登出: " + tgpc.getName());
                client.out().encrypt(new S_PacketBoxSelect());
            }
        } catch (Exception e) {
            _log.error("登出死亡角色時發生異常!", e);
        }
    }
}
