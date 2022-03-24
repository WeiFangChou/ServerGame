package com.lineage.data.npc.quest2;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillMode;
import com.lineage.server.model.skill.skillmode.SkillMode;
import com.lineage.server.serverpackets.S_SkillSound;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Saell extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Saell.class);

    private Npc_Saell() {
    }

    public static NpcExecutor get() {
        return new Npc_Saell();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 1;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.hasSkillEffect(L1SkillId.AGLV85_1X)) {
                pc.removeSkillEffect(L1SkillId.AGLV85_1X);
            }
            if (pc.hasSkillEffect(L1SkillId.ADLV80_1)) {
                pc.removeSkillEffect(L1SkillId.ADLV80_1);
            }
            if (!pc.hasSkillEffect(4010)) {
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7680));
                SkillMode mode = L1SkillMode.get().getSkill(4010);
                if (mode != null) {
                    try {
                        mode.start(pc, (L1Character) null, (L1Magic) null, 2400);
                    } catch (Exception e) {
                        _log.error(e.getLocalizedMessage(), e);
                    }
                }
            }
        } catch (Exception e2) {
            _log.error(e2.getLocalizedMessage(), e2);
        }
    }
}
