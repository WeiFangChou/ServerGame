package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Contract_Book extends ItemExecutor {
    private Contract_Book() {
    }

    public static ItemExecutor get() {
        return new Contract_Book();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        if (item.getItemId() == 41130) {
            if (pc.getQuest().get_step(36) == 255 || pc.getInventory().checkItem(41131, 1)) {
                pc.sendPackets(new S_ServerMessage(79));
            } else {
                CreateNewItem.createNewItem(pc, 41131, 1);
            }
        } else if (pc.getQuest().get_step(37) == 255 || pc.getInventory().checkItem(41122, 1)) {
            pc.sendPackets(new S_ServerMessage(79));
        } else {
            CreateNewItem.createNewItem(pc, 41122, 1);
        }
    }
}
