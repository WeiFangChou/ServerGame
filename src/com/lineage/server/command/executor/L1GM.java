package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;

public class L1GM implements L1CommandExecutor {
    private L1GM() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1GM();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        pc.setGm(!pc.isGm());
        pc.sendPackets(new S_SystemMessage("setGm = " + pc.isGm()));
    }
}
