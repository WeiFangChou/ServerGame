package com.lineage.data.npc.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CKEWLv50_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.world.WorldQuest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_RottingCorpse extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_RottingCorpse.class);

    private Npc_RottingCorpse() {
    }

    public static NpcExecutor get() {
        return new Npc_RottingCorpse();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 1;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.isInParty()) {
                int i = 0;
                for (L1PcInstance otherPc : pc.getParty().partyUsers().values()) {
                    if (otherPc.isCrown()) {
                        i++;
                    } else if (otherPc.isKnight()) {
                        i += 2;
                    } else if (otherPc.isElf()) {
                        i += 4;
                    } else if (otherPc.isWizard()) {
                        i += 8;
                    }
                }
                if (i != 15) {
                    WorldQuest.get().get(pc.get_showId()).endQuest();
                } else if (pc.isCrown()) {
                    if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id()) && !pc.getInventory().checkItem(49239)) {
                        CreateNewItem.getQuestItem(pc, npc, 49239, 1);
                    }
                } else if (pc.isKnight()) {
                    if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id()) && !pc.getInventory().checkItem(49239)) {
                        CreateNewItem.getQuestItem(pc, npc, 49239, 1);
                    }
                } else if (pc.isElf()) {
                    if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id()) && !pc.getInventory().checkItem(49239)) {
                        CreateNewItem.getQuestItem(pc, npc, 49239, 1);
                    }
                } else if (pc.isWizard()) {
                    if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id()) && !pc.getInventory().checkItem(49239)) {
                        CreateNewItem.getQuestItem(pc, npc, 49239, 1);
                    }
                } else if (!pc.isDarkelf() && !pc.isDragonKnight()) {
                    pc.isIllusionist();
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
