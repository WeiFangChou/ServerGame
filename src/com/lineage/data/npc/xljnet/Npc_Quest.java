package com.lineage.data.npc.xljnet;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import william.NpcQuest;

public class Npc_Quest extends NpcExecutor {
    private String _htmlid = null;

    private Npc_Quest() {
    }

    public static NpcExecutor get() {
        return new Npc_Quest();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        if (this._htmlid != null) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), this._htmlid));
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        if (NpcQuest.forNpcQuest(cmd, pc, npc, npc.getNpcTemplate().get_npcId(), (int) amount)) {
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void set_set(String[] set) {
        try {
            this._htmlid = set[1];
        } catch (Exception ignored) {
        }
    }
}
