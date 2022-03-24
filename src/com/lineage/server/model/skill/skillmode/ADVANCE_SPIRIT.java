package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_MPUpdate;

public class ADVANCE_SPIRIT extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        if (!cha.hasSkillEffect(79)) {
            L1PcInstance pc = (L1PcInstance) cha;
            pc.setAdvenHp(pc.getBaseMaxHp() / 5);
            pc.setAdvenMp(pc.getBaseMaxMp() / 5);
            pc.addMaxHp(pc.getAdvenHp());
            pc.addMaxMp(pc.getAdvenMp());
            pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
            if (pc.isInParty()) {
                pc.getParty().updateMiniHP(pc);
            }
            pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
        }
        cha.setSkillEffect(79, integer * L1SkillId.STATUS_BRAVE);
        return 0;
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1NpcInstance npc, L1Character cha, L1Magic magic, int integer) throws Exception {
        if (!cha.hasSkillEffect(79)) {
            L1PcInstance pc = (L1PcInstance) cha;
            pc.setAdvenHp(pc.getBaseMaxHp() / 5);
            pc.setAdvenMp(pc.getBaseMaxMp() / 5);
            pc.addMaxHp(pc.getAdvenHp());
            pc.addMaxMp(pc.getAdvenMp());
            pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
            if (pc.isInParty()) {
                pc.getParty().updateMiniHP(pc);
            }
            pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
        }
        cha.setSkillEffect(79, integer * L1SkillId.STATUS_BRAVE);
        return 0;
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void start(L1PcInstance srcpc, Object obj) throws Exception {
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void stop(L1Character cha) throws Exception {
        if (cha instanceof L1PcInstance) {
            L1PcInstance pc = (L1PcInstance) cha;
            pc.addMaxHp(-pc.getAdvenHp());
            pc.addMaxMp(-pc.getAdvenMp());
            pc.setAdvenHp(0);
            pc.setAdvenMp(0);
            pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
            if (pc.isInParty()) {
                pc.getParty().updateMiniHP(pc);
            }
            pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
        }
    }
}
