package com.lineage.data.npc.mob;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DragonKnightLv45_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.utils.CheckUtil;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DK45_Yeti extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(DK45_Yeti.class);
    private static Random _random = new Random();

    private DK45_Yeti() {
    }

    public static NpcExecutor get() {
        return new DK45_Yeti();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 8;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public L1PcInstance death(L1Character lastAttacker, L1NpcInstance npc) {
        try {
            L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);
            if (pc == null || !pc.getQuest().isStart(DragonKnightLv45_1.QUEST.get_id())) {
                return pc;
            }
            switch (pc.getQuest().get_step(DragonKnightLv45_1.QUEST.get_id())) {
                case 3:
                    if (_random.nextInt(100) >= 40) {
                        return pc;
                    }
                    CreateNewItem.getQuestItem(pc, npc, 49225, 1);
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
