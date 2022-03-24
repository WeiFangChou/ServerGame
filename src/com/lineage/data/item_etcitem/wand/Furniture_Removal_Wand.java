package com.lineage.data.item_etcitem.wand;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.lock.FurnitureSpawnReading;
import com.lineage.server.model.Instance.L1FurnitureInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

public class Furniture_Removal_Wand extends ItemExecutor {
    private Furniture_Removal_Wand() {
    }

    public static ItemExecutor get() {
        return new Furniture_Removal_Wand();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        useFurnitureRemovalWand(pc, data[0], item);
    }

    private void useFurnitureRemovalWand(L1PcInstance pc, int targetId, L1ItemInstance item) {
        L1Object target = World.get().findObject(targetId);
        if (target != null) {
            pc.sendPacketsX8(new S_DoActionGFX(pc.getId(), 17));
            if (item.getChargeCount() <= 0) {
                pc.sendPackets(new S_ServerMessage(79));
            } else if (target != null && (target instanceof L1FurnitureInstance)) {
                L1FurnitureInstance furniture = (L1FurnitureInstance) target;
                furniture.deleteMe();
                FurnitureSpawnReading.get().deleteFurniture(furniture);
                item.setChargeCount(item.getChargeCount() - 1);
                pc.getInventory().updateItem(item, 128);
            }
        }
    }
}
