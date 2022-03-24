package com.lineage.data.npc;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Azel extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Azel.class);

    private Npc_Azel() {
    }

    public static NpcExecutor get() {
        return new Npc_Azel();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 1;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "azel1"));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
