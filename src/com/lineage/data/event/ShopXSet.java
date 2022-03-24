package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.ShopXTable;
import com.lineage.server.datatables.lock.DwarfShopReading;
import com.lineage.server.templates.L1Event;
import com.lineage.server.timecontroller.event.ShopXTime;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ShopXSet extends EventExecutor {
    public static int ADENA;
    public static int DATE;
    public static int MAX;
    public static int MIN;
    private static final Log _log = LogFactory.getLog(ShopXSet.class);

    private ShopXSet() {
    }

    public static EventExecutor get() {
        return new ShopXSet();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
        try {
            String[] set = event.get_eventother().split(",");
            ADENA = Integer.parseInt(set[0]);
            DATE = Integer.parseInt(set[1]);
            MIN = Integer.parseInt(set[2]);
            MAX = Integer.parseInt(set[3]);
            DwarfShopReading.get().load();
            ShopXTable.get().load();
            new ShopXTime().start();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
