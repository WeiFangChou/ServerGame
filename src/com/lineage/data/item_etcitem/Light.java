package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ItemName;

public class Light extends ItemExecutor {
    private Light() {
    }

    public static ItemExecutor get() {
        return new Light();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        if (item.isNowLighting()) {
            item.setNowLighting(false);
            pc.turnOnOffLight();
        } else {
            item.setNowLighting(true);
            pc.turnOnOffLight();
        }
        pc.sendPackets(new S_ItemName(item));
    }
}
