package com.lineage.data.npc.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DragonKnightLv30_1;
import com.lineage.data.quest.DragonKnightLv45_1;
import com.lineage.data.quest.DragonKnightLv50_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Talrion extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Talrion.class);

    private Npc_Talrion() {
    }

    public static NpcExecutor get() {
        return new Npc_Talrion();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion4"));
            } else if (pc.isKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion4"));
            } else if (pc.isElf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion4"));
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion4"));
            } else if (pc.isDarkelf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion4"));
            } else if (pc.isDragonKnight()) {
                if (pc.getQuest().isEnd(DragonKnightLv50_1.QUEST.get_id()) && pc.getInventory().checkItem(49228) && !pc.getInventory().checkItem(49230)) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion10"));
                } else if (pc.getQuest().isEnd(DragonKnightLv45_1.QUEST.get_id()) && pc.getInventory().checkItem(49214)) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion9"));
                } else if (!pc.getQuest().isEnd(DragonKnightLv30_1.QUEST.get_id()) || !pc.getInventory().checkItem(49213)) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion4"));
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion1"));
                }
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion4"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion4"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        boolean isCloseList = false;
        if (!pc.isDragonKnight()) {
            isCloseList = true;
        } else if (cmd.equalsIgnoreCase("a")) {
            L1ItemInstance item = pc.getInventory().checkItemX(49213, 1);
            if (item != null) {
                pc.getInventory().removeItem(item, 1);
                CreateNewItem.getQuestItem(pc, npc, 21103, 1);
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion2"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion3"));
            }
        } else if (cmd.equalsIgnoreCase("b")) {
            L1ItemInstance item2 = pc.getInventory().checkItemX(49214, 1);
            if (item2 != null) {
                pc.getInventory().removeItem(item2, 1);
                CreateNewItem.getQuestItem(pc, npc, 21102, 1);
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion7"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion8"));
            }
        } else if (cmd.equalsIgnoreCase("c")) {
            if (pc.getInventory().checkItemX(49228, 1) != null) {
                CreateNewItem.getQuestItem(pc, npc, 49230, 1);
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion5"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion6"));
            }
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void attack(L1PcInstance pc, L1NpcInstance npc) {
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void work(L1NpcInstance npc) {
    }
}
