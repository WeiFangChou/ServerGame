package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Cooking;

public class Food3 extends ItemExecutor {
    private Food3() {
    }

    public static ItemExecutor get() {
        return new Food3();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        L1Cooking.useCookingItem(pc, item);
    }
}
