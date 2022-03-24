package com.lineage.data.npc.quest;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Harding extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Harding.class);

    private Npc_Harding() {
    }

    public static NpcExecutor get() {
        return new Npc_Harding();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 1;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.get_hardinR() == null) {
                return;
            }
            if (pc.get_hardinR().get_time() <= 0 || pc.get_hardinR().get_time() > 180) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep001"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep002"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
