package com.lineage.data.npc.mob;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.WizardLv50_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.utils.CheckUtil;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class W50_BaphometVarlok extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(W50_BaphometVarlok.class);
    private static Random _random = new Random();

    private W50_BaphometVarlok() {
    }

    public static NpcExecutor get() {
        return new W50_BaphometVarlok();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 8;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public L1PcInstance death(L1Character lastAttacker, L1NpcInstance npc) {
        try {
            L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);
            if (pc == null || pc.getQuest().isEnd(WizardLv50_1.QUEST.get_id()) || pc.getInventory().checkItem(49164) || !pc.getQuest().isStart(WizardLv50_1.QUEST.get_id())) {
                return pc;
            }
            switch (pc.getQuest().get_step(WizardLv50_1.QUEST.get_id())) {
                case 2:
                    if (_random.nextInt(100) >= 40) {
                        return pc;
                    }
                    CreateNewItem.getQuestItem(pc, npc, 49164, 1);
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
