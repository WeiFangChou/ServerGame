package com.lineage.data.npc.quest;

import com.lineage.config.ConfigKill;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.timecontroller.event.ranking.RankingKillTimer;

public class Npc_KillerRanking extends NpcExecutor {
    private Npc_KillerRanking() {
    }

    public static NpcExecutor get() {
        return new Npc_KillerRanking();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_k_1", new String[]{String.valueOf(ConfigKill.KILLLEVEL)}));
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        String[] userName;
        if (cmd.equalsIgnoreCase("1")) {
            String[] userName2 = RankingKillTimer.killName();
            if (userName2 != null) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_k_2", userName2));
            }
        } else if (cmd.equalsIgnoreCase("2") && (userName = RankingKillTimer.deathName()) != null) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_k_3", userName));
        }
    }
}
