package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.templates.L1Event;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MazuSet extends EventExecutor {
    private static final Log _log = LogFactory.getLog(MazuSet.class);

    private MazuSet() {
    }

    public static EventExecutor get() {
        return new MazuSet();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
    }
}
