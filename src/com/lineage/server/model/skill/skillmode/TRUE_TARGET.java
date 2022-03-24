package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.serverpackets.S_TrueTarget;
import com.lineage.server.world.WorldClan;

public class TRUE_TARGET extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        srcpc.sendPackets(new S_TrueTarget(cha.getId(), srcpc.getId(), srcpc.getText()));
        if (srcpc.getClan() != null) {
            L1PcInstance[] players = WorldClan.get().getClan(srcpc.getClanname()).getOnlineClanMember();
            for (L1PcInstance pc : players) {
                if (pc.getLocation().isInScreen(srcpc.getLocation())) {
                    pc.sendPackets(new S_TrueTarget(cha.getId(), pc.getId(), srcpc.getText()));
                }
            }
        }
        srcpc.setText("");
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
    }
}
