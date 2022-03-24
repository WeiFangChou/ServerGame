package com.lineage.data.item_etcitem.quest;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldQuest;
import java.util.HashMap;

public class CKEWLv50Key3 extends ItemExecutor {
    public static ItemExecutor get() {
        return new CKEWLv50Key3();
    }

    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc.getMapId() != 2000) {
            pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
            return;
        }
        HashMap<Integer, L1Object> mapList = new HashMap<>();
        mapList.putAll(World.get().getVisibleObjects(2000));
        int itemid = 49166;
        int i = 0;
        for (L1Object tgobj : mapList.values()) {
            if (tgobj instanceof L1PcInstance) {
                L1PcInstance tgpc = (L1PcInstance)tgobj;
                if (tgpc.get_showId() == pc.get_showId()) {
                    if (tgpc.isCrown()) {
                        i++;
                        continue;
                    }
                    if (tgpc.isKnight()) {
                        i += 2;
                        continue;
                    }
                    if (tgpc.isElf()) {
                        i += 4;
                        continue;
                    }
                    if (tgpc.isWizard())
                        i += 8;
                }
            }
        }
        if (i == 15) {
            for (L1Object tgobj : mapList.values()) {
                if (tgobj instanceof L1PcInstance) {
                    L1PcInstance tgpc = (L1PcInstance)tgobj;
                    if (tgpc.get_showId() == pc.get_showId()) {
                        L1ItemInstance reitem = tgpc.getInventory().findItemId(itemid);
                        if (reitem != null) {
                            tgpc.sendPackets((ServerBasePacket)new S_ServerMessage(165, reitem.getName()));
                            tgpc.getInventory().removeItem(reitem);
                        }
                        if (tgpc.isCrown()) {
                            L1Teleport.teleport(tgpc, 32741, 32776, (short)2000, 2, true);
                            continue;
                        }
                        if (tgpc.isKnight()) {
                            L1Teleport.teleport(tgpc, 32741, 32771, (short)2000, 2, true);
                            continue;
                        }
                        if (tgpc.isElf()) {
                            L1Teleport.teleport(tgpc, 32735, 32771, (short)2000, 2, true);
                            continue;
                        }
                        if (tgpc.isWizard())
                            L1Teleport.teleport(tgpc, 32735, 32776, (short)2000, 2, true);
                    }
                }
            }
        } else {
            pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
            L1QuestUser quest = WorldQuest.get().get(pc.get_showId());
            quest.endQuest();
        }
        mapList.clear();
    }
}
