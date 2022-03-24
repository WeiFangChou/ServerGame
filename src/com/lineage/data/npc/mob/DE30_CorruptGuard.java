package com.lineage.data.npc.mob;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv30_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.utils.CheckUtil;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DE30_CorruptGuard extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(DE30_CorruptGuard.class);
    private static Random _random = new Random();

    private DE30_CorruptGuard() {
    }

    public static NpcExecutor get() {
        return new DE30_CorruptGuard();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 8;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public L1PcInstance death(L1Character lastAttacker, L1NpcInstance npc) {
        try {
            L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);
            if (pc == null || pc.getQuest().isEnd(DarkElfLv30_1.QUEST.get_id()) || pc.getQuest().get_step(DarkElfLv30_1.QUEST.get_id()) != 1 || pc.getInventory().checkItem(40554) || _random.nextInt(100) >= 40) {
                return pc;
            }
            CreateNewItem.getQuestItem(pc, npc, 40554, 1);
            return pc;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
