package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.TypeIdOrginal;
import com.lineage.server.templates.L1Event;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TypeId_Orginal extends EventExecutor {
    public static boolean MODE = false;
    private static final Log _log = LogFactory.getLog(TypeId_Orginal.class);

    private TypeId_Orginal() {
    }

    public static EventExecutor get() {
        return new TypeId_Orginal();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
        try {
            MODE = Boolean.parseBoolean(event.get_eventother().split(",")[0]);
            TypeIdOrginal.get();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
