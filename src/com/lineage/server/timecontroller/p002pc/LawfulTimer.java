package com.lineage.server.timecontroller.p002pc;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* renamed from: com.lineage.server.timecontroller.pc.LawfulTimer */
public class LawfulTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(LawfulTimer.class);
    private static int _time = 0;
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, 650, 650);
    }

    public void run() {
        try {
            Collection<L1PcInstance> all = World.get().getAllPlayers();
            if (!all.isEmpty()) {
                for (L1PcInstance tgpc : all) {
                    if (check(tgpc)) {
                        tgpc.onChangeLawful();
                        if (_time % 20 == 0) {
                            switch (tgpc.getMapId()) {
                                case 340:
                                case 350:
                                case 360:
                                case 370:
                                    break;
                                default:
                                    if (ServerWarExecutor.get().checkCastleWar() <= 0 && checkC(tgpc)) {
                                        showClan(tgpc);
                                        break;
                                    }
                            }
                        }
                        Thread.sleep(1);
                    }
                }
                _time++;
            }
        } catch (Exception e) {
            _log.error("Lawful更新處理時間軸異常重啟", e);
            PcOtherThreadPool.get().cancel(this._timer, false);
            new LawfulTimer().start();
        }
    }

    private static boolean check(L1PcInstance tgpc) {
        if (tgpc == null) {
            return false;
        }
        try {
            if (tgpc.getOnlineStatus() == 0 || tgpc.getNetConnection() == null || tgpc.isTeleport() || tgpc.isDead() || tgpc.getCurrentHp() <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean checkC(L1PcInstance tgpc) {
        try {
            if (tgpc.getClan() != null && tgpc.getClan().isClanskill() && tgpc.getClan().getOnlineClanMemberSize() >= 16 && tgpc.get_other().get_clanskill() != 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    private static void showClan(L1PcInstance tgpc) {
        try {
            if (tgpc.getClan().getOnlineClanMemberSize() >= 30) {
                tgpc.sendPacketsX8(new S_SkillSound(tgpc.getId(), 5201));
            } else {
                tgpc.sendPacketsX8(new S_SkillSound(tgpc.getId(), 5263));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
