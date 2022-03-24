package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Hone extends ItemExecutor {
    private Hone() {
    }

    public static ItemExecutor get() {
        return new Hone();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        L1ItemInstance item1 = pc.getInventory().getItem(data[0]);
        if (item1 != null) {
            if (item1.getItem().getType2() == 0 || item1.get_durability() <= 0) {
                pc.sendPackets(new S_ServerMessage(79));
            } else {
                pc.getInventory().recoveryDamage(item1);
                String msg0 = item1.getLogName();
                if (item1.get_durability() == 0) {
                    pc.sendPackets(new S_ServerMessage(464, msg0));
                } else {
                    pc.sendPackets(new S_ServerMessage(463, msg0));
                }
            }
            pc.getInventory().removeItem(item, 1);
        }
    }
}
