package com.lineage.data.npc.mob;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.KnightLv45_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.utils.CheckUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class K45_GiantGuardian extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(K45_GiantGuardian.class);

    private K45_GiantGuardian() {
    }

    public static NpcExecutor get() {
        return new K45_GiantGuardian();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 8;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public L1PcInstance death(L1Character lastAttacker, L1NpcInstance npc) {
        try {
            L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);
            if (pc == null || pc.getQuest().isEnd(KnightLv45_1.QUEST.get_id()) || !pc.getQuest().isStart(KnightLv45_1.QUEST.get_id()) || pc.getInventory().checkItem(40528) || pc.getInventory().checkItem(40597) || pc.getInventory().checkItem(40537)) {
                return pc;
            }
            CreateNewItem.getQuestItem(pc, npc, 40528, 1);
            return pc;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
