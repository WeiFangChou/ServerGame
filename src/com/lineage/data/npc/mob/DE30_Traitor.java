package com.lineage.data.npc.mob;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv30_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.utils.CheckUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DE30_Traitor extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(DE30_Traitor.class);

    private DE30_Traitor() {
    }

    public static NpcExecutor get() {
        return new DE30_Traitor();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 8;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public L1PcInstance death(L1Character lastAttacker, L1NpcInstance npc) {
        try {
            L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);
            if (pc == null || pc.getQuest().isEnd(DarkElfLv30_1.QUEST.get_id()) || !pc.getQuest().isStart(DarkElfLv30_1.QUEST.get_id()) || pc.getInventory().checkItem(40596)) {
                return pc;
            }
            if (!pc.get_otherList().ATKNPC.contains(Integer.valueOf(npc.getNpcId()))) {
                pc.get_otherList().ATKNPC.add(Integer.valueOf(npc.getNpcId()));
            }
            if (pc.get_otherList().ATKNPC.size() < 7) {
                return pc;
            }
            CreateNewItem.getQuestItem(pc, npc, 40596, 1);
            pc.get_otherList().ATKNPC.clear();
            return pc;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
