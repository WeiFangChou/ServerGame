package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.templates.L1Event;
import com.lineage.server.timecontroller.event.NewServerTime;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NewServerSet extends EventExecutor {
    private static final Log _log = LogFactory.getLog(NewServerSet.class);

    private NewServerSet() {
    }

    public static EventExecutor get() {
        return new NewServerSet();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
        try {
            new NewServerTime().start(Integer.parseInt(event.get_eventother()));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
