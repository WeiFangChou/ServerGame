package com.lineage.data.npc.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.IllusionistLv50_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_BlueSoul extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_BlueSoul.class);

    private Npc_BlueSoul() {
    }

    public static NpcExecutor get() {
        return new Npc_BlueSoul();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {
                return;
            }
            if (pc.isKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluesoul_f4"));
            } else if (pc.isElf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluesoul_f4"));
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluesoul_f4"));
            } else if (pc.isDarkelf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluesoul_f4"));
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluesoul_f4"));
            } else if (!pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluesoul_f4"));
            } else if (pc.getQuest().isStart(IllusionistLv50_1.QUEST.get_id())) {
                switch (pc.getQuest().get_step(IllusionistLv50_1.QUEST.get_id())) {
                    case 2:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluesoul_f1"));
                        return;
                    case 3:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluesoul_f2"));
                        return;
                    default:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluesoul_f4"));
                        return;
                }
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluesoul_f4"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        if (!pc.isIllusionist()) {
            return;
        }
        if (pc.getQuest().isStart(IllusionistLv50_1.QUEST.get_id())) {
            switch (pc.getQuest().get_step(IllusionistLv50_1.QUEST.get_id())) {
                case 2:
                    if (!cmd.equalsIgnoreCase("a")) {
                        return;
                    }
                    if (CreateNewItem.checkNewItem(pc, new int[]{49203, 49204}, new int[]{5, 5}) < 1) {
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluesoul_f3"));
                        return;
                    }
                    CreateNewItem.createNewItem(pc, new int[]{49203, 49204}, new int[]{5, 5}, new int[]{49207, 49208}, 1, new int[]{1, 1});
                    pc.getQuest().set_step(IllusionistLv50_1.QUEST.get_id(), 3);
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluesoul_f2"));
                    return;
                default:
                    pc.sendPackets(new S_CloseList(pc.getId()));
                    return;
            }
        } else {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
