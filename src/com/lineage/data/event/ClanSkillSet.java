package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.templates.L1Event;
import com.lineage.server.timecontroller.event.ClanSkillTimer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClanSkillSet extends EventExecutor {
    private static final Log _log = LogFactory.getLog(ClanSkillSet.class);

    private ClanSkillSet() {
    }

    public static EventExecutor get() {
        return new ClanSkillSet();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
        try {
            new ClanSkillTimer().start();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
