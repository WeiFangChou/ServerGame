package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.poison.L1DamagePoison;

public class Acid_Liquor extends ItemExecutor {
    private Acid_Liquor() {
    }

    public static ItemExecutor get() {
        return new Acid_Liquor();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        L1DamagePoison.doInfection(pc, pc, 3000, 5);
        pc.getInventory().removeItem(item, 1);
    }
}
