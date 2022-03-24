package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.templates.L1Event;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import william.Poly.NpcPower;
import william.Poly.Npc_PowerLog;

public class Item_Power extends EventExecutor {
    public static boolean MODE = false;
    private static final Log _log = LogFactory.getLog(Item_Power.class);

    private Item_Power() {
    }

    public static EventExecutor get() {
        return new Item_Power();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
        try {
            MODE = Boolean.parseBoolean(event.get_eventother().split(",")[0]);
            NpcPower.get().load();
            Npc_PowerLog.get().load();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
