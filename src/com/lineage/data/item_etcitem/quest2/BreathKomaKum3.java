package com.lineage.data.item_etcitem.quest2;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.data.quest.ADLv80_1;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

public class BreathKomaKum3 extends ItemExecutor {
    private BreathKomaKum3() {
    }

    public static ItemExecutor get() {
        return new BreathKomaKum3();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc.getMapId() == 1005) {
            pc.getInventory().removeItem(item, 1);
            for (L1Object obj : World.get().getVisibleObjects(ADLv80_1.MAPID).values()) {
                if (obj instanceof L1DoorInstance) {
                    L1DoorInstance door = (L1DoorInstance) obj;
                    if (door.get_showId() == pc.get_showId() && door.getDoorId() == 10007) {
                        door.open();
                        door.deleteMe();
                    }
                }
            }
            return;
        }
        pc.sendPackets(new S_ServerMessage(79));
    }
}
