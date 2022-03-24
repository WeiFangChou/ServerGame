package com.lineage.data.npc.mob;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.KnightLv30_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.utils.CheckUtil;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class K30_Ramia extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(K30_Ramia.class);
    private static Random _random = new Random();

    private K30_Ramia() {
    }

    public static NpcExecutor get() {
        return new K30_Ramia();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 8;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public L1PcInstance death(L1Character lastAttacker, L1NpcInstance npc) {
        try {
            L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);
            if (pc == null || pc.getQuest().isEnd(KnightLv30_1.QUEST.get_id()) || !pc.getQuest().isStart(KnightLv30_1.QUEST.get_id()) || pc.getInventory().checkItem(40543) || _random.nextInt(100) >= 20) {
                return pc;
            }
            CreateNewItem.getQuestItem(pc, npc, 40543, 1);
            return pc;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
