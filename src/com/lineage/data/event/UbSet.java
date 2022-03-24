package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.UBSpawnTable;
import com.lineage.server.datatables.UBTable;
import com.lineage.server.templates.L1Event;
import com.lineage.server.timecontroller.event.UbTime;

public class UbSet extends EventExecutor {
    private UbSet() {
    }

    public static EventExecutor get() {
        return new UbSet();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
        UBTable.getInstance().load();
        UBSpawnTable.getInstance().load();
        new UbTime().start();
    }
}
