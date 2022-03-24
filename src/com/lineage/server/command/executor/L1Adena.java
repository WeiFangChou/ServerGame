package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Adena implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1Adena.class);

    private L1Adena() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Adena();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            long count = Long.parseLong(new StringTokenizer(arg).nextToken());
            if (pc.getInventory().storeItem(L1ItemId.ADENA, count) != null) {
                pc.sendPackets(new S_ServerMessage(403, "$4: " + count));
            }
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
