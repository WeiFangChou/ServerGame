package com.lineage.data.item_etcitem.quest;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;
import java.util.HashMap;

public class DK50_Flute extends ItemExecutor {
    private DK50_Flute() {
    }

    public static ItemExecutor get() {
        return new DK50_Flute();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc.getMapId() != 2004) {
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }
        HashMap<Integer, L1Object> mapList = new HashMap<>();
        mapList.putAll(World.get().getVisibleObjects(pc.getMapId()));
        int i = 0;
        for (L1Object tgobj : mapList.values()) {
            if (tgobj instanceof L1MonsterInstance) {
                L1MonsterInstance tgnpc = (L1MonsterInstance) tgobj;
                if (pc.get_showId() == tgnpc.get_showId() && tgnpc.getNpcId() == 80139) {
                    i++;
                }
            }
        }
        if (i > 0) {
            pc.sendPackets(new S_ServerMessage(79));
        } else {
            L1Location loc = pc.getLocation().randomLocation(5, false);
            pc.sendPacketsXR(new S_EffectLocation(loc, 7004), 8);
            L1SpawnUtil.spawnX(80139, loc, pc.get_showId()).setLink(pc);
            pc.getInventory().removeItem(item, 1);
        }
        mapList.clear();
    }
}
