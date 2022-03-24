package com.lineage.data.item_etcitem.furniture;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.lock.FurnitureSpawnReading;
import com.lineage.server.model.Instance.L1FurnitureInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1HouseLocation;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;
import java.util.Iterator;

public class PartyCakes extends ItemExecutor {
    private PartyCakes() {
    }

    public static ItemExecutor get() {
        return new PartyCakes();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        int itemObjectId = item.getId();
        if (!L1HouseLocation.isInHouse(pc.getX(), pc.getY(), pc.getMapId())) {
            pc.sendPackets(new S_ServerMessage(563));
            return;
        }
        boolean isAppear = true;
        L1FurnitureInstance furniture = null;
        Iterator<L1Object> it = World.get().getObject().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            L1Object l1object = it.next();
            if (l1object instanceof L1FurnitureInstance) {
                furniture = (L1FurnitureInstance) l1object;
                if (furniture.getItemObjId() == itemObjectId) {
                    isAppear = false;
                    break;
                }
            }
        }
        if (pc.getHeading() != 0 && pc.getHeading() != 2) {
            pc.sendPackets(new S_ServerMessage(79));
        } else if (isAppear) {
            L1SpawnUtil.spawn(pc, 80159, itemObjectId);
        } else {
            furniture.deleteMe();
            FurnitureSpawnReading.get().deleteFurniture(furniture);
        }
    }
}
