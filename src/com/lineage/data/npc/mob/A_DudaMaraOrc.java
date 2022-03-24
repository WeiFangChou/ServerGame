package com.lineage.data.npc.mob;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ALv15_1;
import com.lineage.data.quest.ElfLv15_2;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.utils.CheckUtil;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class A_DudaMaraOrc extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(A_DudaMaraOrc.class);
    private static Random _random = new Random();

    private A_DudaMaraOrc() {
    }

    public static NpcExecutor get() {
        return new A_DudaMaraOrc();
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
            if (pc.getQuest().isStart(ElfLv15_2.QUEST.get_id()) && _random.nextInt(100) < 20) {
                CreateNewItem.getQuestItem(pc, npc, 40611, 1);
            }
            if (!pc.getQuest().isStart(ALv15_1.QUEST.get_id()) || _random.nextInt(100) >= 30) {
                return pc;
            }
            CreateNewItem.getQuestItem(pc, npc, 40133, 1);
            return pc;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
