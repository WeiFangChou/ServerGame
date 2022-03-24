package com.lineage.data.npc.mob;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv50_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.utils.CheckUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DE50_DiscipleChaos extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(DE50_DiscipleChaos.class);

    private DE50_DiscipleChaos() {
    }

    public static NpcExecutor get() {
        return new DE50_DiscipleChaos();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 8;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public L1PcInstance death(L1Character lastAttacker, L1NpcInstance npc) {
        try {
            L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);
            if (pc == null || pc.getQuest().isEnd(DarkElfLv50_1.QUEST.get_id()) || !pc.getQuest().isStart(DarkElfLv50_1.QUEST.get_id()) || pc.getInventory().checkItem(40606)) {
                return pc;
            }
            CreateNewItem.getQuestItem(pc, npc, 40606, 1);
            return pc;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}