package com.lineage.data.npc.mob;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.WizardLv30_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.utils.CheckUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class W30_Skeleton extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(W30_Skeleton.class);

    private W30_Skeleton() {
    }

    public static NpcExecutor get() {
        return new W30_Skeleton();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 8;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public L1PcInstance death(L1Character lastAttacker, L1NpcInstance npc) {
        try {
            L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);
            if (pc == null || pc.getQuest().isEnd(WizardLv30_1.QUEST.get_id()) || !pc.getQuest().isStart(WizardLv30_1.QUEST.get_id()) || pc.getInventory().checkItem(40604)) {
                return pc;
            }
            CreateNewItem.getQuestItem(pc, npc, 40604, 1);
            return pc;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
