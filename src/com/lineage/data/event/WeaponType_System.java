package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.WeaponTypeSystem;
import com.lineage.server.templates.L1Event;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WeaponType_System extends EventExecutor {
    public static boolean MODE = false;
    private static final Log _log = LogFactory.getLog(WeaponType_System.class);

    private WeaponType_System() {
    }

    public static EventExecutor get() {
        return new WeaponType_System();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
        try {
            MODE = Boolean.parseBoolean(event.get_eventother().split(",")[0]);
            WeaponTypeSystem.get().load();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
