package com.lineage.data.item_etcitem.xljnet;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;

public class RingsExpansionGem extends ItemExecutor {
    private RingsExpansionGem() {
    }

    public static ItemExecutor get() {
        return new RingsExpansionGem();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int count = 0;
        if ((pc.getRingsExpansion() & 1) != 1) {
            count = 0 + 1;
        }
        if ((pc.getRingsExpansion() & 2) != 2) {
            count += 2;
        }
        if (count == 0) {
            pc.sendPackets(new S_SystemMessage("無法再擴充戒指欄位。"));
            return;
        }
        pc.getInventory().removeItem(item, 1);
        if ((count & 1) == 1) {
            pc.setRingsExpansion((byte) Math.min(pc.getRingsExpansion() + 1, 3));
            pc.sendPackets(new S_SystemMessage("成功擴充左邊戒指欄位。"));
        } else {
            pc.setRingsExpansion((byte) Math.min(pc.getRingsExpansion() + 2, 3));
            pc.sendPackets(new S_SystemMessage("成功擴充右邊戒指欄位。"));
        }
        CharacterTable.updateRingsExpansion(pc);
    }
}
