package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_SkillHaste;

public class HASTE extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        if (cha.getMoveSpeed() != 2) {
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                if (pc.getHasteItemEquipped() <= 0) {
                    pc.setDrink(false);
                    pc.sendPackets(new S_SkillHaste(pc.getId(), 1, integer));
                }
            }
            cha.setSkillEffect(43, integer * L1SkillId.STATUS_BRAVE);
            cha.broadcastPacketAll(new S_SkillHaste(cha.getId(), 1, 0));
            cha.setMoveSpeed(1);
        } else {
            int skillNum = 0;
            if (cha.hasSkillEffect(29)) {
                skillNum = 29;
            } else if (cha.hasSkillEffect(76)) {
                skillNum = 76;
            } else if (cha.hasSkillEffect(L1SkillId.ENTANGLE)) {
                skillNum = L1SkillId.ENTANGLE;
            }
            if (skillNum != 0) {
                cha.removeSkillEffect(skillNum);
                cha.removeSkillEffect(43);
                cha.setMoveSpeed(0);
            }
        }
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
            pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
        }
        cha.broadcastPacketAll(new S_SkillHaste(cha.getId(), 0, 0));
        cha.setMoveSpeed(0);
    }
}
