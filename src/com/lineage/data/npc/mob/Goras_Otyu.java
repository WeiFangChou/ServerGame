package com.lineage.data.npc.mob;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.IllusionistLv50_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.utils.CheckUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Goras_Otyu extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Goras_Otyu.class);

    private Goras_Otyu() {
    }

    public static NpcExecutor get() {
        return new Goras_Otyu();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 8;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public L1PcInstance death(L1Character lastAttacker, L1NpcInstance npc) {
        try {
            L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);
            if (pc == null || !pc.getQuest().isStart(IllusionistLv50_1.QUEST.get_id())) {
                return pc;
            }
            switch (pc.getQuest().get_step(IllusionistLv50_1.QUEST.get_id())) {
                case 2:
                    CreateNewItem.getQuestItem(pc, npc, 49203, 1);
                    return pc;
                default:
                    return pc;
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
