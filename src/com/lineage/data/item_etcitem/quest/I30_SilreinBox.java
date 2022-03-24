package com.lineage.data.item_etcitem.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.data.quest.IllusionistLv30_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class I30_SilreinBox extends ItemExecutor {
    private I30_SilreinBox() {
    }

    public static ItemExecutor get() {
        return new I30_SilreinBox();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        pc.getInventory().removeItem(item, 1);
        if (pc.getQuest().isStart(IllusionistLv30_1.QUEST.get_id())) {
            CreateNewItem.createNewItem(pc, 49183, 1);
            CreateNewItem.createNewItem(pc, 49186, 1);
        }
    }
}
