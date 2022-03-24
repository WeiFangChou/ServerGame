package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.WorldMob;
import java.util.Iterator;

public class Mystical_Shell extends ItemExecutor {
    private Mystical_Shell() {
    }

    public static ItemExecutor get() {
        return new Mystical_Shell();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        if (!pc.isElf() || pc.getX() < 33971 || pc.getX() > 33975 || pc.getY() < 32324 || pc.getY() > 32328 || pc.getMapId() != 4) {
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }
        boolean found = false;
        Iterator<L1MonsterInstance> iter = WorldMob.get().all().iterator();
        while (true) {
            if (!iter.hasNext()) {
                break;
            }
            L1MonsterInstance mob = iter.next();
            if (mob != null && mob.getNpcTemplate().get_npcId() == 45300) {
                found = true;
                break;
            }
        }
        if (found) {
            pc.sendPackets(new S_ServerMessage(79));
        } else {
            L1SpawnUtil.spawn(pc, 45300, 2, 300);
        }
    }
}
