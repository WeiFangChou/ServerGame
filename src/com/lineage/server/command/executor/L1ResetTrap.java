package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.WorldTrap;

public class L1ResetTrap implements L1CommandExecutor {
    private L1ResetTrap() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ResetTrap();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        WorldTrap.get().resetAllTraps();
        pc.sendPackets(new S_SystemMessage("重新配置陷阱!"));
    }
}
