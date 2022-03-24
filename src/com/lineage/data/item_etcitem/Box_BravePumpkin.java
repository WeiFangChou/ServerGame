package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;

public class Box_BravePumpkin extends ItemExecutor {
    private Box_BravePumpkin() {
    }

    public static ItemExecutor get() {
        return new Box_BravePumpkin();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        pc.getInventory().removeItem(item, 1);
        int item_id = 0;
        int count = 1;
        switch ((int) (Math.random() * 10.0d)) {
            case 0:
                item_id = 41251;
                break;
            case 1:
                item_id = L1ItemId.POTION_OF_HEALING;
                count = 30;
                break;
            case 2:
            case 3:
                item_id = L1ItemId.POTION_OF_HEALING;
                count = 15;
                break;
            case 4:
            case 5:
                item_id = L1ItemId.POTION_OF_EXTRA_HEALING;
                count = 15;
                break;
            case 6:
            case 7:
                item_id = L1ItemId.POTION_OF_GREATER_HEALING;
                count = 15;
                break;
            case 8:
            case 9:
                item_id = 40088;
                count = 6;
                break;
        }
        if (item_id != 0) {
            CreateNewItem.createNewItem(pc, item_id, (long) count);
        }
    }
}
