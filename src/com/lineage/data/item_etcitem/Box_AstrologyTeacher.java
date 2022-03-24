package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class Box_AstrologyTeacher extends ItemExecutor {
    private Box_AstrologyTeacher() {
    }

    public static ItemExecutor get() {
        return new Box_AstrologyTeacher();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        pc.getInventory().removeItem(item, 1);
        CreateNewItem.createNewItem(pc, 41313, 1);
    }
}
