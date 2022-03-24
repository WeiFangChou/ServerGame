package com.lineage.data.npc.other;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Npc_GoddessAtonement extends NpcExecutor {
    private Npc_GoddessAtonement() {
    }

    public static NpcExecutor get() {
        return new Npc_GoddessAtonement();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "restore1pk"));
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        if (!cmd.equalsIgnoreCase("pk")) {
            return;
        }
        if (pc.getLawful() < 30000) {
            pc.sendPackets(new S_ServerMessage(559));
        } else if (pc.get_PKcount() < 5) {
            pc.sendPackets(new S_ServerMessage(560));
        } else if (pc.getInventory().consumeItem(L1ItemId.ADENA, 700000)) {
            pc.set_PKcount(pc.get_PKcount() - 5);
            pc.sendPackets(new S_ServerMessage(561, String.valueOf(pc.get_PKcount())));
        } else {
            pc.sendPackets(new S_ServerMessage((int) L1SkillId.SHOCK_SKIN));
        }
    }
}
