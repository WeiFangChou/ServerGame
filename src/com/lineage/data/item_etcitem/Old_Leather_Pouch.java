package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class Old_Leather_Pouch extends ItemExecutor {
    private Old_Leather_Pouch() {
    }

    public static ItemExecutor get() {
        return new Old_Leather_Pouch();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int item_id;
        int count = 1;
        switch ((int) (Math.random() * 10.0d)) {
            case 1:
                item_id = 40058;
                count = 2;
                break;
            case 2:
                item_id = 40071;
                break;
            case 3:
                item_id = 40039;
                count = 2;
                break;
            case 4:
                item_id = 40040;
                break;
            case 5:
                item_id = 40335;
                break;
            case 6:
                item_id = 40332;
                break;
            case 7:
                item_id = 40331;
                break;
            case 8:
                item_id = 40336;
                break;
            case 9:
                item_id = 40338;
                break;
            default:
                item_id = 40334;
                break;
        }
        pc.getInventory().removeItem(item, 1);
        CreateNewItem.createNewItem(pc, item_id, (long) count);
    }
}
