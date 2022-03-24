package com.lineage.data.npc.mob;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.WizardLv45_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.utils.CheckUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class W45_Doppelganger extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(W45_Doppelganger.class);

    private W45_Doppelganger() {
    }

    public static NpcExecutor get() {
        return new W45_Doppelganger();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 8;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public L1PcInstance death(L1Character lastAttacker, L1NpcInstance npc) {
        L1ItemInstance item;
        try {
            L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);
            if (pc == null || pc.getQuest().isEnd(WizardLv45_1.QUEST.get_id()) || !pc.getQuest().isStart(WizardLv45_1.QUEST.get_id()) || pc.getInventory().checkItem(40536) || pc.getInventory().checkItem(40542) || (item = npc.getInventory().checkItemX(40032, 1)) == null || npc.getInventory().removeItem(item, 1) != 1) {
                return pc;
            }
            CreateNewItem.getQuestItem(pc, npc, 40542, 1);
            return pc;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
