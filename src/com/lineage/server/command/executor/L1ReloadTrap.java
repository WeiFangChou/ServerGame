package com.lineage.server.command.executor;

import com.lineage.server.datatables.TrapsSpawn;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ReloadTrap implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1ReloadTrap.class);

    private L1ReloadTrap() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ReloadTrap();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        if (pc == null) {
            _log.warn("系統命令執行: " + cmdName + "重新載入陷阱。");
        } else {
            pc.sendPackets(new S_SystemMessage("重新載入陷阱"));
        }
        TrapsSpawn.get().reloadTraps();
    }
}
