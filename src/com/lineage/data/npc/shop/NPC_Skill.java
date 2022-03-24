package com.lineage.data.npc.shop;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_SkillSound;

public class NPC_Skill extends NpcExecutor {
    private NPC_Skill() {
    }

    public static NpcExecutor get() {
        return new NPC_Skill();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 17;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_skill_01"));
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int workTime() {
        return 11;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void work(L1NpcInstance npc) {
        npc.broadcastPacketX8(new S_SkillSound(npc.getId(), 4396));
    }
}
