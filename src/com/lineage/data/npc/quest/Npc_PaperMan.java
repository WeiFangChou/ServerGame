package com.lineage.data.npc.quest;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_PaperMan extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_PaperMan.class);

    private Npc_PaperMan() {
    }

    public static NpcExecutor get() {
        return new Npc_PaperMan();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 1;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_paper"));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
