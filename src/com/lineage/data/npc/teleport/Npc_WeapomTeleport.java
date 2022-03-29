package com.lineage.data.npc.teleport;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import java.util.Random;

public class Npc_WeapomTeleport extends NpcExecutor {
    public static final Random _random = new Random();

    private Npc_WeapomTeleport() {
    }

    public static NpcExecutor get() {
        return new Npc_WeapomTeleport();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 1;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        int r = _random.nextInt(3);
        if (r == 0) {
            L1Teleport.teleport(pc, 32557, 32863,  5167, 6, true);
        } else if (r == 1) {
            L1Teleport.teleport(pc, 32723, 32799,  5167, 0, true);
        } else if (r == 2) {
            L1Teleport.teleport(pc, 32606, 32745,  5167, 4, true);
        }
    }
}
