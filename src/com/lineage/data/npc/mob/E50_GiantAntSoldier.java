package com.lineage.data.npc.mob;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ElfLv50_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.utils.CheckUtil;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class E50_GiantAntSoldier extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(E50_GiantAntSoldier.class);
    private static Random _random = new Random();

    private E50_GiantAntSoldier() {
    }

    public static NpcExecutor get() {
        return new E50_GiantAntSoldier();
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
            switch (pc.getMapId()) {
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                case 51:
                    if (pc.getQuest().isEnd(ElfLv50_1.QUEST.get_id()) || !pc.getQuest().isStart(ElfLv50_1.QUEST.get_id()) || pc.getInventory().checkItem(49162)) {
                        return pc;
                    }
                    switch (pc.getQuest().get_step(ElfLv50_1.QUEST.get_id())) {
                        case 1:
                            if (_random.nextInt(100) >= 40) {
                                return pc;
                            }
                            CreateNewItem.getQuestItem(pc, npc, 49162, 1);
                            return pc;
                        default:
                            return pc;
                    }
                default:
                    return pc;
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
