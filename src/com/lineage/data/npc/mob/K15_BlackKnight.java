package com.lineage.data.npc.mob;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.KnightLv15_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.utils.CheckUtil;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class K15_BlackKnight extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(K15_BlackKnight.class);
    private static Random _random = new Random();

    private K15_BlackKnight() {
    }

    public static NpcExecutor get() {
        return new K15_BlackKnight();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 8;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public L1PcInstance death(L1Character lastAttacker, L1NpcInstance npc) {
        try {
            L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);
            if (pc == null || pc.getQuest().isEnd(KnightLv15_1.QUEST.get_id()) || !pc.getQuest().isStart(KnightLv15_1.QUEST.get_id())) {
                return pc;
            }
            if (pc.getInventory().checkItem(20005)) {
                if (_random.nextInt(100) >= 40) {
                    return pc;
                }
                CreateNewItem.getQuestItem(pc, npc, 40540, 1);
                return pc;
            } else if (pc.getInventory().checkItem(40608) || _random.nextInt(100) >= 40) {
                return pc;
            } else {
                CreateNewItem.getQuestItem(pc, npc, 40608, 1);
                return pc;
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
