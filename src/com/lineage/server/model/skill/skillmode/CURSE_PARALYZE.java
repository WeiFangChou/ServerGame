package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1CurseParalysis;
import com.lineage.server.model.L1Magic;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_Poison;

public class CURSE_PARALYZE extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        if (!cha.hasSkillEffect(157) && !cha.hasSkillEffect(50) && !cha.hasSkillEffect(80) && !cha.hasSkillEffect(194)) {
            if (cha instanceof L1PcInstance) {
                L1CurseParalysis.curse(cha, L1EffectInstance.CUBE_TIME, 16000, 1);
            } else if (cha instanceof L1MonsterInstance) {
                L1CurseParalysis.curse(cha, 0, 16000, 0);
            }
        }
        return 0;
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1NpcInstance npc, L1Character cha, L1Magic magic, int integer) throws Exception {
        if (!cha.hasSkillEffect(157) && !cha.hasSkillEffect(50) && !cha.hasSkillEffect(80) && !cha.hasSkillEffect(194)) {
            if (cha instanceof L1PcInstance) {
                L1CurseParalysis.curse(cha, L1EffectInstance.CUBE_TIME, 16000, 1);
            } else if (cha instanceof L1MonsterInstance) {
                L1CurseParalysis.curse(cha, 0, 16000, 0);
            }
        }
        return 0;
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void start(L1PcInstance srcpc, Object obj) throws Exception {
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void stop(L1Character cha) throws Exception {
        if (cha instanceof L1PcInstance) {
            L1PcInstance pc = (L1PcInstance) cha;
            pc.sendPacketsAll(new S_Poison(cha.getId(), 0));
            pc.sendPackets(new S_Paralysis(1, false));
        } else if ((cha instanceof L1MonsterInstance) || (cha instanceof L1SummonInstance) || (cha instanceof L1PetInstance)) {
            L1NpcInstance npc = (L1NpcInstance) cha;
            npc.broadcastPacketAll(new S_Poison(cha.getId(), 0));
            npc.setParalyzed(false);
        }
    }
}
