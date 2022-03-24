package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.QuestTable;
import com.lineage.server.datatables.QuesttSpawnTable;
import com.lineage.server.templates.L1Event;
import com.lineage.server.timecontroller.quest.QuestTimer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QuestSet extends EventExecutor {
    public static boolean ISQUEST = false;
    private static final Log _log = LogFactory.getLog(QuestSet.class);

    private QuestSet() {
    }

    public static EventExecutor get() {
        return new QuestSet();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
        try {
            ISQUEST = true;
            QuestTable.get().load();
            if (QuestTable.get().size() > 0) {
                QuesttSpawnTable.get().load();
            }
            new QuestTimer().start();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
