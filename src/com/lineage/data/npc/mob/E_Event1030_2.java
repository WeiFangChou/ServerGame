package com.lineage.data.npc.mob;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_SkillSound;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class E_Event1030_2 extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(E_Event1030_2.class);
    private static Random _random = new Random();

    private E_Event1030_2() {
    }

    public static NpcExecutor get() {
        return new E_Event1030_2();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 4;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void attack(L1PcInstance pc, L1NpcInstance npc) {
        try {
            int i = _random.nextInt(100);
            if (i >= 0 && i <= 1 && !npc.isremovearmor() && !pc.hasSkillEffect(L1SkillId.ADLV80_2_2)) {
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7782));
                pc.setSkillEffect(L1SkillId.ADLV80_2_2, 12000);
                npc.set_removearmor(true);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
