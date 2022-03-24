package com.lineage.data.npc.other;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv50_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Kimadum extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Kimadum.class);

    private Npc_Kimadum() {
    }

    public static NpcExecutor get() {
        return new Npc_Kimadum();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 1;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.getQuest().isStart(DarkElfLv50_1.QUEST.get_id())) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kimadums"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kimadum"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
