package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Letter;

public class Letter_closed extends ItemExecutor {
    private Letter_closed() {
    }

    public static ItemExecutor get() {
        return new Letter_closed();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int itemId = item.getItemId();
        pc.sendPackets(new S_Letter(item));
        item.setItemId(itemId + 1);
        pc.getInventory().updateItem(item, 64);
        pc.getInventory().saveItem(item, 64);
    }
}
