package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Sound;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.WorldMob;
import java.util.Iterator;

public class Silver_Flute extends ItemExecutor {
    private Silver_Flute() {
    }

    public static ItemExecutor get() {
        return new Silver_Flute();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        pc.sendPacketsX8(new S_Sound(10));
        if (pc.getX() >= 32619 && pc.getX() <= 32623 && pc.getY() >= 33120 && pc.getY() <= 33124 && pc.getMapId() == 440) {
            boolean found = false;
            Iterator<L1MonsterInstance> iter = WorldMob.get().all().iterator();
            while (true) {
                if (!iter.hasNext()) {
                    break;
                }
                L1MonsterInstance mob = iter.next();
                if (mob != null && mob.getNpcTemplate().get_npcId() == 45875) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                L1SpawnUtil.spawn(pc, 45875, 0, 0);
            }
        }
    }
}
