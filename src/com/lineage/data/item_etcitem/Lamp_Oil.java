package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ItemName;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.util.Iterator;

public class Lamp_Oil extends ItemExecutor {
    private Lamp_Oil() {
    }

    public static ItemExecutor get() {
        return new Lamp_Oil();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        Iterator<L1ItemInstance> it = pc.getInventory().getItems().iterator();
        while (true) {
            if (it.hasNext()) {
                L1ItemInstance lightItem = it.next();
                if (lightItem.getItem().getItemId() == 40002) {
                    lightItem.setRemainingTime(item.getItem().getLightFuel());
                    pc.sendPackets(new S_ItemName(lightItem));
                    pc.sendPackets(new S_ServerMessage(230));
                    break;
                }
            } else {
                break;
            }
        }
        pc.getInventory().removeItem(item, 1);
    }
}
