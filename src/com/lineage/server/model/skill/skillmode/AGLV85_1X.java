package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_MPUpdate;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;

public class AGLV85_1X extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        if (srcpc.hasSkillEffect(L1SkillId.AGLV85_1X)) {
            return 0;
        }
        srcpc.addMaxHp(150);
        srcpc.addMaxMp(50);
        srcpc.addWater(30);
        srcpc.addEarth(30);
        srcpc.addFire(30);
        srcpc.addWind(30);
        srcpc.addDmgup(1);
        srcpc.addBowDmgup(1);
        srcpc.addHitup(5);
        srcpc.addBowHitup(5);
        srcpc.addAc(10);
        srcpc.setSkillEffect(L1SkillId.AGLV85_1X, integer * L1SkillId.STATUS_BRAVE);
        srcpc.sendPackets(new S_HPUpdate(srcpc.getCurrentHp(), srcpc.getMaxHp()));
        srcpc.sendPackets(new S_MPUpdate(srcpc));
        srcpc.sendPackets(new S_OwnCharAttrDef(srcpc));
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
        cha.addMaxHp(-150);
        cha.addMaxMp(-50);
        cha.addWater(-30);
        cha.addEarth(-30);
        cha.addFire(-30);
        cha.addWind(-30);
        cha.addDmgup(-1);
        cha.addBowDmgup(-1);
        cha.addHitup(-5);
        cha.addBowHitup(-5);
        cha.addAc(-10);
        if (cha instanceof L1PcInstance) {
            L1PcInstance pc = (L1PcInstance) cha;
            pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
            pc.sendPackets(new S_MPUpdate(pc));
            pc.sendPackets(new S_OwnCharAttrDef(pc));
        }
    }
}
