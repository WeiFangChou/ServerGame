package com.lineage.data.npc;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillUse;

public class Npc_NewUser extends NpcExecutor {
    private Npc_NewUser() {
    }

    public static NpcExecutor get() {
        return new Npc_NewUser();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 1;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        int[] allBuffSkill;
        if (!pc.hasSkillEffect(79)) {
            for (int skillid : new int[]{79, 48, 42, 26}) {
                startSkill(pc, npc, skillid);
            }
        }
    }

    private void startSkill(L1PcInstance pc, L1NpcInstance npc, int skillid) {
        new L1SkillUse().handleCommands(pc, skillid, pc.getId(), pc.getX(), pc.getY(), 0, 3, npc);
    }
}
