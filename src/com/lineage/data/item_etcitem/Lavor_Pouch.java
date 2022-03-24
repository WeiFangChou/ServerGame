package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class Lavor_Pouch extends ItemExecutor {
    private Lavor_Pouch() {
    }

    public static ItemExecutor get() {
        return new Lavor_Pouch();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int item_id;
        switch ((int) (Math.random() * 5.0d)) {
            case 0:
                item_id = 40090;
                break;
            case 1:
                item_id = 40091;
                break;
            case 2:
                item_id = 40092;
                break;
            case 3:
                item_id = 40093;
                break;
            default:
                item_id = 40094;
                break;
        }
        pc.getInventory().removeItem(item, 1);
        CreateNewItem.createNewItem(pc, item_id, 1);
    }
}
