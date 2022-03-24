package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_OwnCharStatus;

public class AWAKEN_ANTHARAS extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        if (srcpc.getAwakeSkillId() == 0) {
            srcpc.addMaxHp(127);
            srcpc.sendPackets(new S_HPUpdate(srcpc.getCurrentHp(), srcpc.getMaxHp()));
            if (srcpc.isInParty()) {
                srcpc.getParty().updateMiniHP(srcpc);
            }
            srcpc.addAc(-12);
            srcpc.sendPackets(new S_OwnCharStatus(srcpc));
            srcpc.setAwakeSkillId(185);
            L1BuffUtil.doPoly(srcpc);
            srcpc.startMpReductionByAwake();
            return 0;
        } else if (srcpc.getAwakeSkillId() != 185) {
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
            pc.addMaxHp(-127);
            pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
            if (pc.isInParty()) {
                pc.getParty().updateMiniHP(pc);
            }
            pc.addAc(12);
            pc.sendPackets(new S_OwnCharStatus(pc));
            pc.setAwakeSkillId(0);
            L1BuffUtil.undoPoly(pc);
            pc.stopMpReductionByAwake();
        }
    }
}
