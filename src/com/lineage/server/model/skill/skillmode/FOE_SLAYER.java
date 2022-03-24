package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.serverpackets.S_SkillSound;

public class FOE_SLAYER extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        srcpc.isFoeSlayer(true);
        for (int i = 0; i < 4; i++) {
            cha.onAction(srcpc);
        }
        srcpc.sendPacketsX8(new S_SkillSound(srcpc.getId(), 7020));
        srcpc.sendPacketsX8(new S_SkillSound(cha.getId(), 6509));
        return 0;
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1NpcInstance npc, L1Character cha, L1Magic magic, int integer) throws Exception {
        for (int i = 0; i < 3; i++) {
            npc.attackTarget(cha);
        }
        npc.broadcastPacketX8(new S_SkillSound(cha.getId(), 6509));
        npc.broadcastPacketX8(new S_SkillSound(cha.getId(), 7020));
        return 0;
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void start(L1PcInstance srcpc, Object obj) throws Exception {
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void stop(L1Character cha) throws Exception {
    }
}
