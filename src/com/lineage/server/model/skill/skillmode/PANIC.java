package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_OwnCharStatus2;

public class PANIC extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        if (cha.hasSkillEffect(217)) {
            return 0;
        }
        if (cha instanceof L1PcInstance) {
            L1PcInstance pc = (L1PcInstance) cha;
            pc.addStr(-5);
            pc.addCon(-5);
            pc.addDex(-5);
            pc.addWis(-5);
            pc.addInt(-5);
            pc.setSkillEffect(217, integer * L1SkillId.STATUS_BRAVE);
            pc.sendPackets(new S_OwnCharStatus2(pc));
            return 0;
        } else if (!(cha instanceof L1MonsterInstance) && !(cha instanceof L1SummonInstance) && !(cha instanceof L1PetInstance)) {
            return 0;
        } else {
            L1NpcInstance tgnpc = (L1NpcInstance) cha;
            tgnpc.addStr(-5);
            tgnpc.addCon(-5);
            tgnpc.addDex(-5);
            tgnpc.addWis(-5);
            tgnpc.addInt(-5);
            tgnpc.setSkillEffect(217, integer * L1SkillId.STATUS_BRAVE);
            return 0;
        }
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
            pc.addStr(5);
            pc.addCon(5);
            pc.addDex(5);
            pc.addWis(5);
            pc.addInt(5);
        } else if ((cha instanceof L1MonsterInstance) || (cha instanceof L1SummonInstance) || (cha instanceof L1PetInstance)) {
            L1NpcInstance tgnpc = (L1NpcInstance) cha;
            tgnpc.addStr(5);
            tgnpc.addCon(5);
            tgnpc.addDex(5);
            tgnpc.addWis(5);
            tgnpc.addInt(5);
        }
    }
}
