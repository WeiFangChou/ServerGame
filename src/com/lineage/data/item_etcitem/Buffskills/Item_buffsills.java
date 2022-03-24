package com.lineage.data.item_etcitem.Buffskills;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import william.madmanBuffskills;

public class Item_buffsills extends ItemExecutor {
    public static ItemExecutor get() {
        return new Item_buffsills();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        madmanBuffskills.forItemUSe(pc, item);
    }
}
