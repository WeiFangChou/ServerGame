package com.lineage.server.command.executor;

import com.lineage.server.datatables.lock.CharBuffReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxGm;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1BuffKick implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1BuffKick.class);

    private L1BuffKick() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1BuffKick();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            int objid = Integer.parseInt(arg);
            CharBuffReading.get().deleteBuff(objid);
            pc.sendPackets(new S_ServerMessage(166, String.valueOf(objid) + " Buff清除!"));
        } catch (Exception e) {
            try {
                pc.sendPackets(new S_PacketBoxGm(pc, 0));
            } catch (Exception e2) {
                _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
                pc.sendPackets(new S_ServerMessage(261));
            }
        }
    }
}
