package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.ExtraAttrWeaponTable;
import com.lineage.server.templates.L1Event;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FeatureItemSet extends EventExecutor {
    public static boolean POWER_START = false;
    private static final Log _log = LogFactory.getLog(FeatureItemSet.class);

    private FeatureItemSet() {
    }

    public static EventExecutor get() {
        return new FeatureItemSet();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
        try {
            POWER_START = Boolean.parseBoolean(event.get_eventother().split(",")[0]);
            ExtraAttrWeaponTable.getInstance().load();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
