package com.lineage.data.item_etcitem.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.data.quest.IllusionistLv45_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class ThoughtFragment1 extends ItemExecutor {
    private ThoughtFragment1() {
    }

    public static ItemExecutor get() {
        return new ThoughtFragment1();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        L1ItemInstance item1 = pc.getInventory().getItem(data[0]);
        if (item1 == null) {
            pc.sendPackets(new S_ServerMessage(79));
        } else if (item1.getItemId() == 49198) {
            pc.getInventory().removeItem(item, 1);
            pc.getInventory().removeItem(item1, 1);
            if (pc.getQuest().isStart(IllusionistLv45_1.QUEST.get_id())) {
                CreateNewItem.createNewItem(pc, 49200, 1);
            }
        } else {
            pc.sendPackets(new S_ServerMessage(79));
        }
    }
}
