package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;
import java.util.Iterator;

public class Fuel extends ItemExecutor {
    private Fuel() {
    }

    public static ItemExecutor get() {
        return new Fuel();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        Iterator<L1Object> it = World.get().getVisibleObjects(pc, 3).iterator();
        while (it.hasNext()) {
            L1Object object = it.next();
            if ((object instanceof L1EffectInstance) && ((L1NpcInstance) object).getNpcTemplate().get_npcId() == 81170) {
                pc.sendPackets(new S_ServerMessage(1162));
                return;
            }
        }
        int[] iArr = new int[2];
        int[] loc = pc.getFrontLoc();
        L1SpawnUtil.spawnEffect(81170, 600, loc[0], loc[1], pc.getMapId(), null, 0);
        pc.getInventory().removeItem(item, 1);
    }
}
