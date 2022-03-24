package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SkillSound;

public class Letter_Firecrackers extends ItemExecutor {
    private Letter_Firecrackers() {
    }

    public static ItemExecutor get() {
        return new Letter_Firecrackers();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        pc.sendPacketsAll(new S_SkillSound(pc.getId(), item.getItemId() - 34946));
        pc.getInventory().removeItem(item, 1);
    }
}
