package com.lineage.data.item_etcitem.poweritem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_ServerMessage;

public class ItemId_ADENA extends ItemExecutor {
    private ItemId_ADENA() {
    }

    public static ItemExecutor get() {
        return new ItemId_ADENA();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        switch (item.getItemId()) {
            case 51005:
                if (pc.getInventory().checkItem(51005, 1)) {
                    pc.getInventory().consumeItem(51005, 1);
                    pc.sendPackets(new S_ServerMessage("獲得" + ItemTable.get().getTemplate(L1ItemId.ADENA).getName() + "(" + pc.getInventory().storeItem(L1ItemId.ADENA, 500000000).getCount() + ")。"));
                    return;
                } else if (!pc.getInventory().checkItem(51005, 1)) {
                    pc.sendPackets(new S_ServerMessage(337, String.valueOf(ItemTable.get().getTemplate(51005).getName()) + "(" + (1 - pc.getInventory().countItems(51005)) + ")"));
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }
}
