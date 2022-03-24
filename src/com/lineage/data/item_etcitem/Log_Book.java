package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Log_Book extends ItemExecutor {
    private Log_Book() {
    }

    public static ItemExecutor get() {
        return new Log_Book();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int itemobj = data[0];
        int itemId = item.getItemId();
        L1ItemInstance tgItem = pc.getInventory().getItem(itemobj);
        if (tgItem != null) {
            int logbookId = tgItem.getItem().getItemId();
            if (logbookId == itemId + 8034) {
                CreateNewItem.createNewItem(pc, logbookId + 2, 1);
                pc.getInventory().removeItem(tgItem, 1);
                pc.getInventory().removeItem(item, 1);
                return;
            }
            pc.sendPackets(new S_ServerMessage(79));
        }
    }
}
