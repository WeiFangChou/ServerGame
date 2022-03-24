package com.lineage.server.model;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_PinkName;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.types.Point;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1PinkName {
    public static final Log _log = LogFactory.getLog(L1PinkName.class);

    private L1PinkName() {
    }

    /* access modifiers changed from: private */
    public static class PinkNameTimer implements Runnable {
        private L1PcInstance _attacker = null;
        private int _mapid = -1;
        private Point _point = null;

        public PinkNameTimer(L1PcInstance attacker) {
            this._attacker = attacker;
            this._point = new Point(attacker.getX(), attacker.getY());
            this._mapid = attacker.getMapId();
        }

        public void run() {
            for (int i = 0; i < 180; i++) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    L1PinkName._log.error(e.getLocalizedMessage(), e);
                }
                if (!(this._mapid == this._attacker.getMapId() && this._attacker.getLocation().isInScreen(this._point) && !this._attacker.isDead())) {
                    break;
                }
            }
            stopPinkName();
        }

        private void stopPinkName() {
            this._attacker.sendPacketsAll(new S_PinkName(this._attacker.getId(), 0));
            this._attacker.setPinkName(false);
        }
    }

    public static void onAction(L1PcInstance tgpc, L1Character atk) {
        if (tgpc != null && atk != null && (atk instanceof L1PcInstance)) {
            L1PcInstance attacker = (L1PcInstance) atk;
            if (tgpc.getId() != attacker.getId() && attacker.getFightId() != tgpc.getId() && tgpc.getZoneType() == 0 && attacker.getZoneType() == 0) {
                boolean isNowWar = false;
                int castleId = L1CastleLocation.getCastleIdByArea(tgpc);
                if (castleId != 0) {
                    isNowWar = ServerWarExecutor.get().isNowWar(castleId);
                }
                if (!isNowWar && tgpc.getLawful() >= 0 && !tgpc.isPinkName()) {
                    attacker.sendPacketsAll(new S_PinkName(attacker.getId(), OpcodesServer.S_OPCODE_INVLIST));
                    if (!attacker.isPinkName()) {
                        attacker.setPinkName(true);
                        GeneralThreadPool.get().execute(new PinkNameTimer(attacker));
                    }
                }
            }
        }
    }
}
