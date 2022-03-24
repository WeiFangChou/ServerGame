package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Liquor;

public class Liquor extends ItemExecutor {
    private Liquor() {
    }

    public static ItemExecutor get() {
        return new Liquor();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        pc.setDrink(true);
        pc.sendPackets(new S_Liquor(pc.getId()));
        pc.getInventory().removeItem(item, 1);
    }
}
