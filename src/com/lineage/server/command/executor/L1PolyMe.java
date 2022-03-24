package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1PolyMe implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1PolyMe.class);

    private L1PolyMe() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1PolyMe();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            try {
                L1PolyMorph.doPoly(pc, Integer.parseInt(arg), 7200, 2);
            } catch (Exception e) {
                pc.sendPackets(new S_SystemMessage(".polyme [外型代號]"));
            }
        } catch (Exception e2) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
