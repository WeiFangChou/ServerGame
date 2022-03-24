package com.lineage.data.item_etcitem.quest2;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class MiraculousFragment extends ItemExecutor {
    private MiraculousFragment() {
    }

    public static ItemExecutor get() {
        return new MiraculousFragment();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        long count = item.getCount();
        if (count >= 100) {
            pc.getInventory().removeItem(item, 100);
            CreateNewItem.createNewItem(pc, 49352, 1);
            return;
        }
        pc.sendPackets(new S_ServerMessage(337, "奇蹟的碎片(" + (100 - count) + ")"));
    }
}
