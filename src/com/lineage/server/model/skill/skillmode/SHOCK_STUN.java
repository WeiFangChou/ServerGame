package com.lineage.server.model.skill.skillmode;

import com.lineage.config.ConfigKnightSkill;
import com.lineage.server.model.Instance.L1GuardInstance;
import com.lineage.server.model.Instance.L1GuardianInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.utils.L1SpawnUtil;
import java.util.Random;

public class SHOCK_STUN extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        int dmg = magic.calcMagicDamage(87);
        Random random = new Random();
        int[] s = ConfigKnightSkill.SHOCK_STUN_TIMER;
        int shock = s[random.nextInt(s.length)];
        if ((cha instanceof L1PcInstance) && cha.hasSkillEffect(87)) {
            shock += cha.getSkillEffectTimeSec(87) * L1SkillId.STATUS_BRAVE;
        }
        if (shock > 6000) {
            shock = 6000;
        }
        cha.setSkillEffect(87, shock);
        L1SpawnUtil.spawnEffect(81162, shock / L1SkillId.STATUS_BRAVE, cha.getX(), cha.getY(), cha.getMapId(), cha, 0);
        if (cha instanceof L1PcInstance) {
            ((L1PcInstance) cha).sendPackets(new S_Paralysis(5, true));
        } else if ((cha instanceof L1MonsterInstance) || (cha instanceof L1SummonInstance) || (cha instanceof L1PetInstance)) {
            ((L1NpcInstance) cha).setParalyzed(true);
        }
        return dmg;
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1NpcInstance npc, L1Character cha, L1Magic magic, int integer) throws Exception {
        int dmg = magic.calcMagicDamage(87);
        Random random = new Random();
        int[] s = ConfigKnightSkill.SHOCK_STUN_TIMER;
        int shock = s[random.nextInt(s.length)];
        if ((cha instanceof L1PcInstance) && cha.hasSkillEffect(87)) {
            shock += cha.getSkillEffectTimeSec(87) * L1SkillId.STATUS_BRAVE;
        }
        if (shock > 6000) {
            shock = 6000;
        }
        cha.setSkillEffect(87, shock);
        L1SpawnUtil.spawnEffect(81162, shock / L1SkillId.STATUS_BRAVE, cha.getX(), cha.getY(), cha.getMapId(), cha, 0);
        if (cha instanceof L1PcInstance) {
            ((L1PcInstance) cha).sendPackets(new S_Paralysis(5, true));
        } else if ((cha instanceof L1MonsterInstance) || (cha instanceof L1SummonInstance) || (cha instanceof L1GuardianInstance) || (cha instanceof L1GuardInstance) || (cha instanceof L1PetInstance)) {
            ((L1NpcInstance) cha).setParalyzed(true);
        }
        return dmg;
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void start(L1PcInstance srcpc, Object obj) throws Exception {
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void stop(L1Character cha) throws Exception {
        if (cha instanceof L1PcInstance) {
            ((L1PcInstance) cha).sendPackets(new S_Paralysis(5, false));
        } else if ((cha instanceof L1MonsterInstance) || (cha instanceof L1SummonInstance) || (cha instanceof L1GuardianInstance) || (cha instanceof L1GuardInstance) || (cha instanceof L1PetInstance)) {
            ((L1NpcInstance) cha).setParalyzed(false);
        }
    }
}
