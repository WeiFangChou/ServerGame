package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.templates.L1Event;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseResetSet extends EventExecutor {
    public static int RETAIN = 0;
    private static final Log _log = LogFactory.getLog(BaseResetSet.class);

    private BaseResetSet() {
    }

    public static EventExecutor get() {
        return new BaseResetSet();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
        try {
            try {
                RETAIN = Integer.parseInt(event.get_eventother().split(",")[0]);
            } catch (Exception ignored) {
            }
        } catch (Exception e2) {
            _log.error(e2.getLocalizedMessage(), e2);
        }
    }
}
