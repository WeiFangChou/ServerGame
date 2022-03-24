package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_IdentifyDesc;

public class Appraisal_Reel extends ItemExecutor {
    private Appraisal_Reel() {
    }

    public static ItemExecutor get() {
        return new Appraisal_Reel();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        L1ItemInstance item1 = pc.getInventory().getItem(data[0]);
        if (item1 != null) {
            if (!item1.isIdentified()) {
                item1.setIdentified(true);
                pc.getInventory().updateItem(item1, 2);
            }
            pc.sendPackets(new S_IdentifyDesc(item1));
            pc.getInventory().removeItem(item, 1);
        }
    }
}
