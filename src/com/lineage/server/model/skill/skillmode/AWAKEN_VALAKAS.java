package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_OwnCharStatus;

public class AWAKEN_VALAKAS extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        if (srcpc.getAwakeSkillId() == 0) {
            srcpc.addStr(5);
            srcpc.addCon(5);
            srcpc.addDex(5);
            srcpc.addCha(5);
            srcpc.addInt(5);
            srcpc.addWis(5);
            srcpc.sendPackets(new S_OwnCharStatus(srcpc));
            srcpc.setAwakeSkillId(195);
            L1BuffUtil.doPoly(srcpc);
            srcpc.startMpReductionByAwake();
            return 0;
        } else if (srcpc.getAwakeSkillId() != 195) {
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
            pc.addStr(-5);
            pc.addCon(-5);
            pc.addDex(-5);
            pc.addCha(-5);
            pc.addInt(-5);
            pc.addWis(-5);
            pc.sendPackets(new S_OwnCharStatus(pc));
            pc.setAwakeSkillId(0);
            L1BuffUtil.undoPoly(pc);
            pc.stopMpReductionByAwake();
        }
    }
}
