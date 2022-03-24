package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Chap_Center extends ItemExecutor {
    private Chap_Center() {
    }

    public static ItemExecutor get() {
        return new Chap_Center();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int targetItemId = pc.getInventory().getItem(data[0]).getItem().getItemId();
        switch (targetItemId) {
            case 49095:
                if (pc.getInventory().consumeItem(49092, 1) && pc.getInventory().consumeItem(targetItemId, 1)) {
                    CreateNewItem.createNewItem(pc, 49096, 1);
                    return;
                }
                return;
            case 49099:
                if (pc.getInventory().consumeItem(49092, 1) && pc.getInventory().consumeItem(targetItemId, 1)) {
                    CreateNewItem.createNewItem(pc, 49100, 1);
                    return;
                }
                return;
            case 49274:
                if (pc.getInventory().consumeItem(49092, 1) && pc.getInventory().consumeItem(targetItemId, 1)) {
                    CreateNewItem.createNewItem(pc, 49284, 1);
                    return;
                }
                return;
            case 49275:
                if (pc.getInventory().consumeItem(49092, 1) && pc.getInventory().consumeItem(targetItemId, 1)) {
                    CreateNewItem.createNewItem(pc, 49285, 1);
                    return;
                }
                return;
            default:
                pc.sendPackets(new S_ServerMessage(79));
                return;
        }
    }
}
