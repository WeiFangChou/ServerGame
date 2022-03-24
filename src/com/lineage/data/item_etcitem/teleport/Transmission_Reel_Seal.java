package com.lineage.data.item_etcitem.teleport;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Transmission_Reel_Seal extends ItemExecutor {
    private Transmission_Reel_Seal() {
    }

    public static ItemExecutor get() {
        return new Transmission_Reel_Seal();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int itemId = item.getItemId();
        pc.getInventory().removeItem(item, 1);
        L1ItemInstance item1 = pc.getInventory().storeItem(itemId + 9, 1);
        if (item1 != null) {
            pc.sendPackets(new S_ServerMessage(403, item1.getLogName()));
        }
    }
}
