package com.lineage.data.npc.mob;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.utils.CheckUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CH_Monster extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(CH_Monster.class);

    private CH_Monster() {
    }

    public static NpcExecutor get() {
        return new CH_Monster();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 8;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public L1PcInstance death(L1Character lastAttacker, L1NpcInstance npc) {
        try {
            L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);
            if (pc == null || pc.get_hardinR() == null) {
                return pc;
            }
            pc.get_hardinR().down_count(npc);
            return pc;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
