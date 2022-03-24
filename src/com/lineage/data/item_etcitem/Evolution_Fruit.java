package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Evolution_Fruit extends ItemExecutor {
    private Evolution_Fruit() {
    }

    public static ItemExecutor get() {
        return new Evolution_Fruit();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        pc.sendPackets(new S_ServerMessage(76, item.getLogName()));
        pc.getInventory().removeItem(item, 1);
    }
}
