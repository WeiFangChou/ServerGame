package com.lineage.server.timecontroller.event;

import com.lineage.server.datatables.lock.DwarfShopReading;
import com.lineage.server.templates.L1ShopS;
import com.lineage.server.thread.GeneralThreadPool;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ShopXTime extends TimerTask {
    private static final Log _log = LogFactory.getLog(ShopXTime.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 3600000L, 3600000L);
    }

    public void run() {
        try {
            Timestamp overTime = new Timestamp(System.currentTimeMillis());
            HashMap<Integer, L1ShopS> allShopS = DwarfShopReading.get().allShopS();
            for (L1ShopS shopS : allShopS.values()) {
                if (shopS.get_end() == 0 && overTime.after(shopS.get_overtime())) {
                    shopS.set_end(3);
                    shopS.set_item(null);
                    DwarfShopReading.get().updateShopS(shopS);
                }
                Thread.sleep(1);
            }
            allShopS.clear();
        } catch (Exception e) {
            _log.error("託售物件時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new ShopXTime().start();
        }
    }
}
