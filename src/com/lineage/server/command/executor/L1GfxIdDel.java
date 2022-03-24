package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1GfxIdDel implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1GfxIdDel.class);

    private L1GfxIdDel() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1GfxIdDel();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            for (L1Object object : World.get().getObject()) {
                if (object instanceof L1NpcInstance) {
                    L1NpcInstance npc = (L1NpcInstance) object;
                    if (npc.getNpcId() == 50000) {
                        npc.deleteMe();
                    }
                }
            }
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
