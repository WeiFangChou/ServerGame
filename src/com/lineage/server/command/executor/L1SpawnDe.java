package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1SpawnDe implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1SpawnDe.class);

    private L1SpawnDe() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1SpawnDe();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
    }
}
