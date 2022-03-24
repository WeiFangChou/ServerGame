package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_Paralysis;

public class STATUS_FREEZE extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        if (cha.hasSkillEffect(L1SkillId.STATUS_FREEZE)) {
            return 0;
        }
        if (cha instanceof L1PcInstance) {
            L1PcInstance pc = (L1PcInstance) cha;
            pc.setSkillEffect(L1SkillId.STATUS_FREEZE, integer * L1SkillId.STATUS_BRAVE);
            pc.sendPackets(new S_Paralysis(6, true));
            return 0;
        } else if (!(cha instanceof L1MonsterInstance) && !(cha instanceof L1SummonInstance) && !(cha instanceof L1PetInstance)) {
            return 0;
        } else {
            ((L1NpcInstance) cha).setParalyzed(true);
            return 0;
        }
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1NpcInstance npc, L1Character cha, L1Magic magic, int integer) throws Exception {
        if (cha.hasSkillEffect(L1SkillId.STATUS_FREEZE)) {
            return 0;
        }
        if (cha instanceof L1PcInstance) {
            L1PcInstance pc = (L1PcInstance) cha;
            pc.setSkillEffect(L1SkillId.STATUS_FREEZE, integer * L1SkillId.STATUS_BRAVE);
            pc.sendPackets(new S_Paralysis(6, true));
            return 0;
        } else if (!(cha instanceof L1MonsterInstance) && !(cha instanceof L1SummonInstance) && !(cha instanceof L1PetInstance)) {
            return 0;
        } else {
            ((L1NpcInstance) cha).setParalyzed(true);
            return 0;
        }
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void start(L1PcInstance srcpc, Object obj) throws Exception {
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void stop(L1Character cha) throws Exception {
        if (cha instanceof L1PcInstance) {
            ((L1PcInstance) cha).sendPackets(new S_Paralysis(6, false));
        } else if ((cha instanceof L1MonsterInstance) || (cha instanceof L1SummonInstance) || (cha instanceof L1PetInstance)) {
            ((L1NpcInstance) cha).setParalyzed(false);
        }
    }
}
