package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;

public class Scroll_CurseRemoval extends ItemExecutor {
    private Scroll_CurseRemoval() {
    }

    public static ItemExecutor get() {
        return new Scroll_CurseRemoval();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int id_normal;
        L1Item template;
        for (L1ItemInstance tgItem : pc.getInventory().getItems()) {
            if (tgItem.getBless() == 2 && tgItem.getBless() < 128) {
                if ((tgItem.getItemId() != 240074 || item.getBless() == 0) && !((tgItem.getItemId() == 240087 && item.getBless() != 0) || tgItem.getItemId() == 41216 || (template = ItemTable.get().getTemplate((id_normal = tgItem.getItemId() - 200000))) == null)) {
                    boolean isEun = false;
                    if (!pc.getInventory().checkItem(id_normal)) {
                        isEun = true;
                    } else if (template.isStackable()) {
                        pc.getInventory().removeItem(tgItem, tgItem.getCount());
                        pc.getInventory().storeItem(id_normal, tgItem.getCount());
                    } else {
                        isEun = true;
                    }
                    if (isEun) {
                        tgItem.setBless(1);
                        tgItem.setItem(template);
                        pc.getInventory().updateItem(tgItem, 576);
                        pc.getInventory().saveItem(tgItem, 576);
                    }
                }
            }
        }
        pc.getInventory().removeItem(item, 1);
        pc.sendPackets(new S_ServerMessage(155));
    }
}
