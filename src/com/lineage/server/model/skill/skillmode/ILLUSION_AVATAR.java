package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_SPMR;

public class ILLUSION_AVATAR extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        if (!(cha instanceof L1PcInstance) || cha.hasSkillEffect(L1SkillId.ILLUSION_AVATAR)) {
            return 0;
        }
        L1PcInstance pc = (L1PcInstance) cha;
        pc.addDmgup(10);
        pc.addSp(8);
        pc.sendPackets(new S_SPMR(pc));
        cha.setSkillEffect(L1SkillId.ILLUSION_AVATAR, integer * L1SkillId.STATUS_BRAVE);
        return 0;
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1NpcInstance npc, L1Character cha, L1Magic magic, int integer) throws Exception {
        return 0;
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void start(L1PcInstance srcpc, Object obj) throws Exception {
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void stop(L1Character cha) throws Exception {
        if (cha instanceof L1PcInstance) {
            L1PcInstance pc = (L1PcInstance) cha;
            pc.addDmgup(-10);
            pc.addSp(-8);
            pc.sendPackets(new S_SPMR(pc));
        }
    }
}
