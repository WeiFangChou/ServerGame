package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Log_Book1 extends ItemExecutor {
    private Log_Book1() {
    }

    public static ItemExecutor get() {
        return new Log_Book1();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int itemobj = data[0];
        int itemId = item.getItemId();
        L1ItemInstance tgItem = pc.getInventory().getItem(itemobj);
        if (tgItem != null) {
            if (tgItem.getItem().getItemId() == itemId + 8034) {
                CreateNewItem.createNewItem(pc, 41058, 1);
                pc.getInventory().removeItem(tgItem, 1);
                pc.getInventory().removeItem(item, 1);
                return;
            }
            pc.sendPackets(new S_ServerMessage(79));
        }
    }
}
