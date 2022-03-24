package com.lineage.data.item_etcitem.quest;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;

public class DK50_Teleport extends ItemExecutor {
    public static ItemExecutor get() {
        return new DK50_Teleport();
    }

    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        pc.getInventory().removeItem(item, 1L);
        L1Teleport.teleport(pc, 33436, 32814, (short)4, 5, true);
    }
}
