package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.NewEnchantDystem;
import com.lineage.server.templates.L1Event;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NewEnchant_Dystem extends EventExecutor {
    public static boolean MODE = false;
    private static final Log _log = LogFactory.getLog(NewEnchant_Dystem.class);

    private NewEnchant_Dystem() {
    }

    public static EventExecutor get() {
        return new NewEnchant_Dystem();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
        try {
            MODE = Boolean.parseBoolean(event.get_eventother().split(",")[0]);
            NewEnchantDystem.get().load();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
