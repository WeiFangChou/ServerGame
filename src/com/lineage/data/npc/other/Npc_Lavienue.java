package com.lineage.data.npc.other;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ALv40_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_ItemCount;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Lavienue extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Lavienue.class);

    private Npc_Lavienue() {
    }

    public static NpcExecutor get() {
        return new Npc_Lavienue();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (!pc.getQuest().isEnd(ALv40_1.QUEST.get_id()) && pc.getLevel() >= 40) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "lavienue9"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        try {
            if (cmd.equalsIgnoreCase("request tear of dark")) {
                int[] items = {40324, 40524, 40443};
                int[] counts = {1, 3, 1};
                int[] gitems = {40525};
                int[] gcounts = {1};
                long xcount = CreateNewItem.checkNewItem(pc, items, counts);
                if (xcount == 1) {
                    CreateNewItem.createNewItem(pc, items, counts, gitems, 1, gcounts);
                    QuestClass.get().startQuest(pc, ALv40_1.QUEST.get_id());
                    QuestClass.get().endQuest(pc, ALv40_1.QUEST.get_id());
                    isCloseList = true;
                } else if (xcount > 1) {
                    pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "a1"));
                } else if (xcount < 1) {
                    isCloseList = true;
                }
            } else if (cmd.equalsIgnoreCase("a1")) {
                int[] items2 = {40324, 40524, 40443};
                int[] counts2 = {1, 3, 1};
                int[] gitems2 = {40525};
                int[] gcounts2 = {1};
                if (CreateNewItem.checkNewItem(pc, items2, counts2) >= amount) {
                    CreateNewItem.createNewItem(pc, items2, counts2, gitems2, amount, gcounts2);
                    QuestClass.get().startQuest(pc, ALv40_1.QUEST.get_id());
                    QuestClass.get().endQuest(pc, ALv40_1.QUEST.get_id());
                }
                isCloseList = true;
            } else if (cmd.equalsIgnoreCase("request spellbook62")) {
                int[] items3 = {40162, 40413, 40409, 40169};
                int[] counts3 = {1, 1, 1, 1};
                int[] gitems3 = {40208};
                int[] gcounts3 = {1};
                if (CreateNewItem.checkNewItem(pc, items3, counts3) >= 1) {
                    CreateNewItem.createNewItem(pc, items3, counts3, gitems3, 1, gcounts3);
                    QuestClass.get().startQuest(pc, ALv40_1.QUEST.get_id());
                    QuestClass.get().endQuest(pc, ALv40_1.QUEST.get_id());
                }
                isCloseList = true;
            } else if (cmd.equalsIgnoreCase("request spellbook113")) {
                int[] items4 = {40162, 40413, 40409, 40169};
                int[] counts4 = {1, 1, 1, 1};
                int[] gitems4 = {40227};
                int[] gcounts4 = {1};
                if (CreateNewItem.checkNewItem(pc, items4, counts4) >= 1) {
                    CreateNewItem.createNewItem(pc, items4, counts4, gitems4, 1, gcounts4);
                    QuestClass.get().startQuest(pc, ALv40_1.QUEST.get_id());
                    QuestClass.get().endQuest(pc, ALv40_1.QUEST.get_id());
                }
                isCloseList = true;
            } else if (cmd.equalsIgnoreCase("request spellbook116")) {
                int[] items5 = {40162, 40413, 40409, 40169};
                int[] counts5 = {2, 2, 2, 2};
                int[] gitems5 = {40230};
                int[] gcounts5 = {1};
                if (CreateNewItem.checkNewItem(pc, items5, counts5) >= 1) {
                    CreateNewItem.createNewItem(pc, items5, counts5, gitems5, 1, gcounts5);
                    QuestClass.get().startQuest(pc, ALv40_1.QUEST.get_id());
                    QuestClass.get().endQuest(pc, ALv40_1.QUEST.get_id());
                }
                isCloseList = true;
            } else if (cmd.equalsIgnoreCase("request spellbook114")) {
                int[] items6 = {40162, 40413, 40409, 40169};
                int[] counts6 = {4, 4, 4, 4};
                int[] gitems6 = {40229};
                int[] gcounts6 = {1};
                if (CreateNewItem.checkNewItem(pc, items6, counts6) >= 1) {
                    CreateNewItem.createNewItem(pc, items6, counts6, gitems6, 1, gcounts6);
                    QuestClass.get().startQuest(pc, ALv40_1.QUEST.get_id());
                    QuestClass.get().endQuest(pc, ALv40_1.QUEST.get_id());
                }
                isCloseList = true;
            } else if (cmd.equalsIgnoreCase("request spellbook117")) {
                int[] items7 = {40162, 40413, 40409, 40169};
                int[] counts7 = {3, 3, 3, 3};
                int[] gitems7 = {40231};
                int[] gcounts7 = {1};
                if (CreateNewItem.checkNewItem(pc, items7, counts7) >= 1) {
                    CreateNewItem.createNewItem(pc, items7, counts7, gitems7, 1, gcounts7);
                    QuestClass.get().startQuest(pc, ALv40_1.QUEST.get_id());
                    QuestClass.get().endQuest(pc, ALv40_1.QUEST.get_id());
                }
                isCloseList = true;
            }
            if (isCloseList) {
                pc.sendPackets(new S_CloseList(pc.getId()));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
