package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ShopSellListCnX;

public class XXX extends ItemExecutor {
    private XXX() {
    }

    public static ItemExecutor get() {
        return new XXX();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        pc.sendPackets(new S_ShopSellListCnX(pc, item.getId()));
    }
}
