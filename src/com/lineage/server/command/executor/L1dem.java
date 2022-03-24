package com.lineage.server.command.executor;

import com.lineage.server.command.GmHtml;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1dem implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1dem.class);

    private L1dem() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1dem();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            new GmHtml(pc, 1).show();
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
