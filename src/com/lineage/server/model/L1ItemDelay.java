package com.lineage.server.model;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1EtcItem;
import com.lineage.server.thread.GeneralThreadPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ItemDelay {
    public static final int ARMOR = 501;
    public static final int ITEM = 502;
    public static final int POLY = 503;
    public static final int WEAPON = 500;
    private static final Log _log = LogFactory.getLog(L1ItemDelay.class);

    private L1ItemDelay() {
    }

    /* access modifiers changed from: package-private */
    public static class ItemDelayTimer implements Runnable {
        private L1Character _cha;
        private int _delayId;
        private int _delayTime;

        public ItemDelayTimer(L1Character cha, int id, int time) {
            this._cha = cha;
            this._delayId = id;
            this._delayTime = time;
        }

        public void run() {
            stopDelayTimer(this._delayId);
        }

        public int get_delayTime() {
            return this._delayTime;
        }

        public void stopDelayTimer(int delayId) {
            this._cha.removeItemDelay(delayId);
        }
    }

    public static void onItemUse(L1PcInstance pc, int delayId, int delayTime) {
        if (delayId != 0 && delayTime != 0) {
            try {
                ItemDelayTimer timer = new ItemDelayTimer(pc, delayId, delayTime);
                pc.addItemDelay(delayId, timer);
                GeneralThreadPool.get().schedule(timer, (long) delayTime);
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public static void onItemUse(ClientExecutor client, L1ItemInstance item) {
        try {
            L1PcInstance pc = client.getActiveChar();
            if (pc != null) {
                onItemUse(pc, item);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static void onItemUse(L1PcInstance pc, L1ItemInstance item) {
        int delayId = 0;
        int delayTime = 0;
        try {
            switch (item.getItem().getType2()) {
                case 0:
                    delayId = ((L1EtcItem) item.getItem()).get_delayid();
                    delayTime = ((L1EtcItem) item.getItem()).get_delaytime();
                    break;
                case 1:
                    return;
                case 2:
                    switch (item.getItemId()) {
                        default:
                            return;
                        case 20062:
                        case 20077:
                        case 120077:
                            if (item.isEquipped() && !pc.isInvisble()) {
                                pc.beginInvisTimer();
                                break;
                            }
                    }
            }
            if (delayId != 0 && delayTime != 0) {
                ItemDelayTimer timer = new ItemDelayTimer(pc, delayId, delayTime);
                pc.addItemDelay(delayId, timer);
                GeneralThreadPool.get().schedule(timer, (long) delayTime);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
