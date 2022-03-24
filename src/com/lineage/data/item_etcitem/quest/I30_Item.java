package com.lineage.data.item_etcitem.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.data.quest.IllusionistLv30_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class I30_Item extends ItemExecutor {
    private I30_Item() {
    }

    public static ItemExecutor get() {
        return new I30_Item();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        L1ItemInstance item1 = pc.getInventory().getItem(data[0]);
        if (item1 == null) {
            pc.sendPackets(new S_ServerMessage(79));
        } else if (item1.getItemId() == 49186) {
            pc.getInventory().removeItem(item, 1);
            pc.getInventory().removeItem(item1, 1);
            if (pc.getQuest().isStart(IllusionistLv30_1.QUEST.get_id())) {
                CreateNewItem.createNewItem(pc, 49189, 1);
            }
        } else {
            pc.sendPackets(new S_ServerMessage(79));
        }
    }
}
