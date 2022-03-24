package com.lineage.data.item_etcitem.quest;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.data.quest.IllusionistLv30_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.utils.L1SpawnUtil;

public class I30_Flute extends ItemExecutor {
    private I30_Flute() {
    }

    public static ItemExecutor get() {
        return new I30_Flute();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc.getQuest().isStart(IllusionistLv30_1.QUEST.get_id())) {
            L1PolyMorph.doPoly(pc, 6214, 1800, 1);
            L1Location loc = pc.getLocation().randomLocation(5, false);
            pc.sendPacketsXR(new S_EffectLocation(loc, 7004), 8);
            L1SpawnUtil.spawnX(45020, loc, pc.get_showId()).setLink(pc);
        }
        pc.getInventory().removeItem(item, 1);
    }
}
