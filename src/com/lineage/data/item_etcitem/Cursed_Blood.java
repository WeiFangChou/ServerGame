package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import java.sql.Timestamp;

public class Cursed_Blood extends ItemExecutor {
    private Cursed_Blood() {
    }

    public static ItemExecutor get() {
        return new Cursed_Blood();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int item_id;
        switch ( ((int) (Math.random() * 6.0d))) {
            case 1:
                item_id = 40031;
                break;
            case 2:
                item_id = 40006;
                break;
            case 3:
                item_id = 40008;
                break;
            case 4:
                item_id = 40009;
                break;
            case 5:
                item_id = 40524;
                break;
            default:
                item_id = 40007;
                break;
        }
        CreateNewItem.createNewItem(pc, item_id, 1);
        item.setLastUsed(new Timestamp(System.currentTimeMillis()));
        pc.getInventory().updateItem(item, 32);
        pc.getInventory().saveItem(item, 32);
    }
}
