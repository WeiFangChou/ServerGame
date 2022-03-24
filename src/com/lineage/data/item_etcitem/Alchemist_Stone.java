package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import java.sql.Timestamp;

public class Alchemist_Stone extends ItemExecutor {
    private Alchemist_Stone() {
    }

    public static ItemExecutor get() {
        return new Alchemist_Stone();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int item_id;
        int count = 2;
        switch ((int) (Math.random() * 7.0d)) {
            case 1:
                item_id = 40024;
                break;
            case 2:
                item_id = 40023;
                break;
            case 3:
                item_id = 40022;
                count = 3;
                break;
            case 4:
                item_id = L1ItemId.POTION_OF_MANA;
                break;
            case 5:
                item_id = L1ItemId.POTION_OF_EMOTION_WISDOM;
                break;
            case 6:
                item_id = 40042;
                count = 1;
                break;
            default:
                item_id = 40068;
                break;
        }
        CreateNewItem.createNewItem(pc, item_id, (long) count);
        item.setLastUsed(new Timestamp(System.currentTimeMillis()));
        pc.getInventory().updateItem(item, 32);
        pc.getInventory().saveItem(item, 32);
    }
}
