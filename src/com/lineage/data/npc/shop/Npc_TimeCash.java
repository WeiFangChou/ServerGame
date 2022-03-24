package com.lineage.data.npc.shop;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.plugin.P_Pay;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_SystemMessage;

public class Npc_TimeCash extends NpcExecutor {
    private String _htmlid = null;

    public static NpcExecutor get() {
        return new Npc_TimeCash();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        if (this._htmlid != null) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), this._htmlid));
        } else {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "greencash"));
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        if (!cmd.equalsIgnoreCase("long_pay")) {
            return;
        }
        if (!pc.hasSkillEffect(90006)) {
            pc.setSkillEffect(90006, 60000);
            P_Pay.checkSponsor(pc);
            return;
        }
        pc.sendPackets(new S_SystemMessage("請稍後再執行."));
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void set_set(String[] set) {
        try {
            this._htmlid = set[1];
        } catch (Exception ignored) {
        }
    }
}
