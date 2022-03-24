package com.lineage.server.timecontroller.pet;

import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DollGetTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(DollGetTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 1000L, 1000L);
    }

    public void run() {
        try {
            Collection<L1PcInstance> all = World.get().getAllPlayers();
            if (!all.isEmpty()) {
                for (L1PcInstance tgpc : all) {
                    if (tgpc.get_doll_get_time_src() > 0 && checkErr(tgpc)) {
                        int itemid = tgpc.get_doll_get()[0];
                        int count = tgpc.get_doll_get()[1];
                        L1ItemInstance item = ItemTable.get().createItem(itemid);
                        if (item != null) {
                            item.setCount((long) count);
                            if (tgpc.getInventory().checkAddItem(item, (long) count) == 0) {
                                tgpc.getInventory().storeItem(item);
                                tgpc.sendPackets(new S_ServerMessage(403, item.getLogName()));
                            }
                        }
                        Thread.sleep(50);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("魔法娃娃處理時間軸(MPR)異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new DollGetTimer().start();
        }
    }

    private static boolean checkErr(L1PcInstance tgpc) {
        if (tgpc == null) {
            return false;
        }
        try {
            if (tgpc.getOnlineStatus() == 0 || tgpc.getNetConnection() == null || tgpc.isTeleport()) {
                return false;
            }
            int newtime = tgpc.get_doll_get_time() - 1;
            tgpc.set_doll_get_time(newtime);
            if (newtime > 0) {
                return false;
            }
            tgpc.set_doll_get_time(tgpc.get_doll_get_time_src());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
