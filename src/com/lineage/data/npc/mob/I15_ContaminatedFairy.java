package com.lineage.data.npc.mob;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.IllusionistLv15_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.utils.CheckUtil;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class I15_ContaminatedFairy extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(I15_ContaminatedFairy.class);
    private static Random _random = new Random();

    private I15_ContaminatedFairy() {
    }

    public static NpcExecutor get() {
        return new I15_ContaminatedFairy();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 8;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public L1PcInstance death(L1Character lastAttacker, L1NpcInstance npc) {
        try {
            L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);
            if (pc == null || pc.getQuest().isEnd(IllusionistLv15_1.QUEST.get_id()) || !pc.getQuest().isStart(IllusionistLv15_1.QUEST.get_id()) || pc.getInventory().checkItem(49170) || _random.nextInt(100) >= 40) {
                return pc;
            }
            CreateNewItem.getQuestItem(pc, npc, 49170, 1);
            return pc;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
