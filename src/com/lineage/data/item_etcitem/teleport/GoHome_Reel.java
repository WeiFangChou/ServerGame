package com.lineage.data.item_etcitem.teleport;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.GetbackTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;

public class GoHome_Reel extends ItemExecutor {
    private GoHome_Reel() {
    }

    public static ItemExecutor get() {
        return new GoHome_Reel();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc.getMap().isEscapable() || pc.isGm()) {
            int[] loc = GetbackTable.GetBack_Location(pc, true);
            L1Teleport.teleport(pc, loc[0], loc[1],  loc[2], 5, true);
            pc.getInventory().removeItem(item, 1);
        } else {
            pc.sendPackets(new S_ServerMessage(647));
            pc.sendPackets(new S_Paralysis(7, false));
        }
        L1BuffUtil.cancelAbsoluteBarrier(pc);
    }
}
