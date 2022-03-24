package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.BlessDystem;
import com.lineage.server.templates.L1Event;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Bless_Dystem extends EventExecutor {
    public static boolean MODE = false;
    private static final Log _log = LogFactory.getLog(Bless_Dystem.class);

    private Bless_Dystem() {
    }

    public static EventExecutor get() {
        return new Bless_Dystem();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
        try {
            MODE = Boolean.parseBoolean(event.get_eventother().split(",")[0]);
            BlessDystem.get().load();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
