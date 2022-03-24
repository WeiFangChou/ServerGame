package com.lineage.data.item_etcitem.wand;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.L1SpawnUtil;
import java.util.Random;

public class Create_Monster_Magic_Wand extends ItemExecutor {
    private Create_Monster_Magic_Wand() {
    }

    public static ItemExecutor get() {
        return new Create_Monster_Magic_Wand();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        Random random = new Random();
        boolean isUse = true;
        pc.broadcastPacketX8(new S_DoActionGFX(pc.getId(), 17));
        if (!pc.getMap().isUsePainwand()) {
            isUse = false;
        } else if (item.getChargeCount() <= 0) {
            isUse = false;
        }
        if (isUse) {
            int[] mobArray = {45008, 45140, 45016, 45021, 45025, 45033, 45099, 45147, 45123, 45130, 45046, 45092, 45138, 45098, 45127, 45143, 45149, 45171, 45040, 45155, 45192, 45173, 45213, 45079, 45144};
            L1SpawnUtil.spawn(pc, mobArray[random.nextInt(mobArray.length)], 0, 300);
            int count = item.getChargeCount() - 1;
            if (count <= 0) {
                count = 0;
            }
            item.setChargeCount(count);
            pc.getInventory().updateItem(item, 128);
            return;
        }
        pc.sendPackets(new S_ServerMessage(79));
    }
}
