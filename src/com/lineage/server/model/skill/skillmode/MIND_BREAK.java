package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import java.util.Random;

public class MIND_BREAK extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        int dmg = 0;
        int reMp = new Random().nextInt(46) + 5;
        if (cha.getCurrentMp() > reMp) {
            dmg = magic.calcMagicDamage(207) << 2;
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                int newMp = pc.getCurrentMp() - reMp;
                if (newMp < 0) {
                    newMp = 0;
                }
                pc.setCurrentMp(newMp);
            } else if ((cha instanceof L1MonsterInstance) || (cha instanceof L1SummonInstance) || (cha instanceof L1PetInstance)) {
                L1NpcInstance npc = (L1NpcInstance) cha;
                int newMp2 = npc.getCurrentMp() - reMp;
                if (newMp2 < 0) {
                    newMp2 = 0;
                }
                npc.setCurrentMp(newMp2);
            }
        }
        return dmg;
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
    }
}
