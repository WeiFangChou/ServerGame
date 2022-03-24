package com.lineage.data.npc.mob;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DragonKnightLv50_1;
import com.lineage.data.quest.IllusionistLv45_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.utils.CheckUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Goras_CrackTime extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Goras_CrackTime.class);

    private Goras_CrackTime() {
    }

    public static NpcExecutor get() {
        return new Goras_CrackTime();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 8;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public L1PcInstance death(L1Character lastAttacker, L1NpcInstance npc) {
        try {
            L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);
            if (pc == null) {
                return pc;
            }
            if (pc.getQuest().isStart(DragonKnightLv50_1.QUEST.get_id())) {
                switch (pc.getQuest().get_step(DragonKnightLv50_1.QUEST.get_id())) {
                    case 2:
                        CreateNewItem.getQuestItem(pc, npc, 49229, 1);
                        break;
                }
            }
            if (!pc.getQuest().isStart(IllusionistLv45_1.QUEST.get_id())) {
                return pc;
            }
            switch (pc.getQuest().get_step(IllusionistLv45_1.QUEST.get_id())) {
                case 2:
                    pc.getQuest().set_step(IllusionistLv45_1.QUEST.get_id(), 3);
                    if (pc.getInventory().checkItem(49202)) {
                        return pc;
                    }
                    CreateNewItem.getQuestItem(pc, npc, 49202, 1);
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
