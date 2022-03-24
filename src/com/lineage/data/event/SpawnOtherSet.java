package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.templates.L1Event;

public class SpawnOtherSet extends EventExecutor {
    private SpawnOtherSet() {
    }

    public static EventExecutor get() {
        return new SpawnOtherSet();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
    }
}
