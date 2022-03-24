package com.lineage.data.npc.quest;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ALv45_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Tion extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Tion.class);

    private Npc_Tion() {
    }

    public static NpcExecutor get() {
        return new Npc_Tion();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.getQuest().isEnd(ALv45_1.QUEST.get_id())) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion8"));
            } else if (pc.getLevel() >= ALv45_1.QUEST.get_questlevel()) {
                switch (pc.getQuest().get_step(ALv45_1.QUEST.get_id())) {
                    case 0:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion1"));
                        QuestClass.get().startQuest(pc, ALv45_1.QUEST.get_id());
                        return;
                    case 1:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion1"));
                        return;
                    case 2:
                    case 3:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion5"));
                        return;
                    case 4:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion6"));
                        return;
                    case 5:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion7"));
                        return;
                    default:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion8"));
                        return;
                }
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion20"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (cmd.equalsIgnoreCase("A")) {
            int[] items = {41339};
            int[] counts = {5};
            if (CreateNewItem.checkNewItem(pc, items, counts) < 1) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion9"));
                return;
            }
            CreateNewItem.createNewItem(pc, items, counts, new int[]{41340}, 1, new int[]{1});
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion4"));
            pc.getQuest().set_step(ALv45_1.QUEST.get_id(), 2);
        } else if (cmd.equalsIgnoreCase("B")) {
            if (CreateNewItem.checkNewItem(pc, new int[]{41341}, new int[]{1}) < 1) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion10"));
                return;
            }
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion5"));
        } else if (cmd.equalsIgnoreCase("C")) {
            int[] items2 = {41343, 41341};
            int[] counts2 = {1, 1};
            if (CreateNewItem.checkNewItem(pc, items2, counts2) < 1) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion12"));
                return;
            }
            CreateNewItem.createNewItem(pc, items2, counts2, new int[]{21057}, 1, new int[]{1});
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion6"));
            pc.getQuest().set_step(ALv45_1.QUEST.get_id(), 4);
        } else if (cmd.equalsIgnoreCase("D")) {
            int[] items3 = {41344, 21057};
            int[] counts3 = {1, 1};
            if (CreateNewItem.checkNewItem(pc, items3, counts3) < 1) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion17"));
                return;
            }
            CreateNewItem.createNewItem(pc, items3, counts3, new int[]{21058}, 1, new int[]{1});
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion7"));
            pc.getQuest().set_step(ALv45_1.QUEST.get_id(), 5);
        } else if (cmd.equalsIgnoreCase("E")) {
            int[] items4 = {41345, 21058};
            int[] counts4 = {1, 1};
            if (CreateNewItem.checkNewItem(pc, items4, counts4) < 1) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion18"));
                return;
            }
            CreateNewItem.createNewItem(pc, items4, counts4, new int[]{21059}, 1, new int[]{1});
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion8"));
            pc.getQuest().set_end(ALv45_1.QUEST.get_id());
        } else {
            isCloseList = true;
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
