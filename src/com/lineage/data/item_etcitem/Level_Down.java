package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Level_Down extends ItemExecutor {
    private Level_Down() {
    }

    public static ItemExecutor get() {
        return new Level_Down();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc.getLevel() > 9) {
            pc.setExp(6581);
            pc.sendPackets(new S_ServerMessage(822));
            pc.getInventory().removeItem(item, 1);
            return;
        }
        pc.sendPackets(new S_ServerMessage(79));
    }
}
