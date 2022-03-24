package com.lineage.data.item_etcitem.quest;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1PolyMorph;

public class OrcEmissaryPoly extends ItemExecutor {
    private OrcEmissaryPoly() {
    }

    public static ItemExecutor get() {
        return new OrcEmissaryPoly();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        pc.getInventory().removeItem(item);
        L1PolyMorph.doPoly(pc, 6984, 1800, 1);
    }
}
