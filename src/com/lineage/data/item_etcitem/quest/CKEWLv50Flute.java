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
import com.lineage.server.world.WorldQuest;
import java.util.HashMap;

public class CKEWLv50Flute extends ItemExecutor {
    private CKEWLv50Flute() {
    }

    public static ItemExecutor get() {
        return new CKEWLv50Flute();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc.getMapId() != 2000) {
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }
        HashMap<Integer, L1Object> mapList = new HashMap<>();
        mapList.putAll(World.get().getVisibleObjects(2000));
        int i = 0;
        for (L1Object tgobj : mapList.values()) {
            if (tgobj instanceof L1MonsterInstance) {
                L1MonsterInstance tgnpc = (L1MonsterInstance) tgobj;
                if (pc.get_showId() == tgnpc.get_showId()) {
                    switch (tgnpc.getNpcId()) {
                        case 87015:
                        case 87016:
                        case 87017:
                        case 87018:
                            i += 16;
                            break;
                    }
                }
            }
            if (tgobj instanceof L1PcInstance) {
                L1PcInstance tgpc = (L1PcInstance) tgobj;
                if (tgpc.get_showId() == pc.get_showId()) {
                    if (tgpc.isCrown()) {
                        i++;
                    } else if (tgpc.isKnight()) {
                        i += 2;
                    } else if (tgpc.isElf()) {
                        i += 4;
                    } else if (tgpc.isWizard()) {
                        i += 8;
                    }
                }
            }
        }
        if (i == 15) {
            for (L1Object tgobj2 : mapList.values()) {
                if (tgobj2 instanceof L1PcInstance) {
                    L1PcInstance tgpc2 = (L1PcInstance) tgobj2;
                    if (tgpc2.get_showId() == pc.get_showId()) {
                        L1ItemInstance reitem = tgpc2.getInventory().findItemId(49167);
                        if (reitem != null) {
                            tgpc2.sendPackets(new S_ServerMessage(165, reitem.getName()));
                            tgpc2.getInventory().removeItem(reitem);
                        }
                        L1Location loc = tgpc2.getLocation().randomLocation(2, false);
                        pc.sendPacketsXR(new S_EffectLocation(loc, 3992), 8);
                        if (tgpc2.isCrown()) {
                            L1SpawnUtil.spawnX(87017, loc, tgpc2.get_showId());
                        } else if (tgpc2.isKnight()) {
                            L1SpawnUtil.spawnX(87018, loc, tgpc2.get_showId());
                        } else if (tgpc2.isElf()) {
                            L1SpawnUtil.spawnX(87015, loc, tgpc2.get_showId());
                        } else if (tgpc2.isWizard()) {
                            L1SpawnUtil.spawnX(87016, loc, tgpc2.get_showId());
                        }
                    }
                }
            }
        } else {
            pc.sendPackets(new S_ServerMessage(79));
            WorldQuest.get().get(pc.get_showId()).endQuest();
        }
        mapList.clear();
    }
}
