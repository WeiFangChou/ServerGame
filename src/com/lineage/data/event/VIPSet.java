package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.lock.VIPReading;
import com.lineage.server.templates.L1Event;
import com.lineage.server.timecontroller.event.VIPTimer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class VIPSet extends EventExecutor {
    public static int ADENA;
    public static int DATETIME;
    public static int ITEMID;
    private static final Log _log = LogFactory.getLog(VIPSet.class);

    private VIPSet() {
    }

    public static EventExecutor get() {
        return new VIPSet();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
        try {
            String[] set = event.get_eventother().split(",");
            ADENA = Integer.parseInt(set[0]);
            DATETIME = Integer.parseInt(set[1]);
            ITEMID = Integer.parseInt(set[2]);
            VIPReading.get().load();
            new VIPTimer().start();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
