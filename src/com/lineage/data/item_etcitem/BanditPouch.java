package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;

public class BanditPouch extends ItemExecutor {
    private BanditPouch() {
    }

    public static ItemExecutor get() {
        return new BanditPouch();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        pc.getInventory().removeItem(item, 1);
        CreateNewItem.createNewItem(pc, (int) L1ItemId.ADENA, (long) (((int) (Math.random() * 300.0d)) + 300));
    }
}
