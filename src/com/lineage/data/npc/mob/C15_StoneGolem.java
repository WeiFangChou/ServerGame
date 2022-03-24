package com.lineage.data.npc.mob;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CrownLv15_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.utils.CheckUtil;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C15_StoneGolem extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(C15_StoneGolem.class);
    private static Random _random = new Random();

    private C15_StoneGolem() {
    }

    public static NpcExecutor get() {
        return new C15_StoneGolem();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 8;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public L1PcInstance death(L1Character lastAttacker, L1NpcInstance npc) {
        try {
            L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);
            if (pc == null || pc.getQuest().isEnd(CrownLv15_1.QUEST.get_id()) || !pc.getQuest().isStart(CrownLv15_1.QUEST.get_id()) || pc.getInventory().checkItem(40564) || _random.nextInt(100) >= 40) {
                return pc;
            }
            CreateNewItem.getQuestItem(pc, npc, 40564, 1);
            return pc;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}