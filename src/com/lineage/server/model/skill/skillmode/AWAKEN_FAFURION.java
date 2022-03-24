package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_SPMR;

public class AWAKEN_FAFURION extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        if (srcpc.getAwakeSkillId() == 0) {
            srcpc.addMr(30);
            srcpc.sendPackets(new S_SPMR(srcpc));
            srcpc.addWind(30);
            srcpc.addWater(30);
            srcpc.addFire(30);
            srcpc.addEarth(30);
            srcpc.sendPackets(new S_OwnCharAttrDef(srcpc));
            srcpc.sendPackets(new S_OwnCharStatus(srcpc));
            srcpc.setAwakeSkillId(190);
            L1BuffUtil.doPoly(srcpc);
            srcpc.startMpReductionByAwake();
            return 0;
        } else if (srcpc.getAwakeSkillId() != 190) {
            return 0;
        } else {
            stop(srcpc);
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
            pc.addMr(-30);
            pc.sendPackets(new S_SPMR(pc));
            pc.addWind(-30);
            pc.addWater(-30);
            pc.addFire(-30);
            pc.addEarth(-30);
            pc.sendPackets(new S_OwnCharAttrDef(pc));
            pc.sendPackets(new S_OwnCharStatus(pc));
            pc.setAwakeSkillId(0);
            L1BuffUtil.undoPoly(pc);
            pc.stopMpReductionByAwake();
        }
    }
}
