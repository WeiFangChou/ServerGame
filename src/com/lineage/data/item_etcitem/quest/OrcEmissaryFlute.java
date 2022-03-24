package com.lineage.data.item_etcitem.quest;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Location;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.L1SpawnUtil;

public class OrcEmissaryFlute extends ItemExecutor {
    private OrcEmissaryFlute() {
    }

    public static ItemExecutor get() {
        return new OrcEmissaryFlute();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc.getMapId() != 61) {
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }
        L1Location loc = pc.getLocation().randomLocation(2, false);
        pc.sendPacketsXR(new S_EffectLocation(loc, 3992), 8);
        L1SpawnUtil.spawnX(84005, loc, pc.get_showId()).setLink(pc);
        pc.getInventory().removeItem(item);
    }
}
