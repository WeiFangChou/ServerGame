package com.lineage.server.model.skill.skillmode;

import com.lineage.server.datatables.lock.CharBookReading;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1BookMark;

public class TELEPORT extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        L1PcInstance pc = (L1PcInstance) cha;
        L1BookMark bookm = CharBookReading.get().getBookMark(pc, integer);
        if (bookm != null) {
            if (pc.getMap().isEscapable() || pc.isGm()) {
                L1Teleport.teleport(pc, bookm.getLocX(), bookm.getLocY(), bookm.getMapId(), 5, true);
                return 0;
            }
            pc.sendPackets(new S_ServerMessage(276));
            pc.sendPackets(new S_Paralysis(7, false));
            return 0;
        } else if (pc.getMap().isTeleportable() || pc.isGm()) {
            L1Location newLocation = pc.getLocation().randomLocation(200, true);
            L1Teleport.teleport(pc, newLocation.getX(), newLocation.getY(), (short) newLocation.getMapId(), 5, true);
            return 0;
        } else {
            pc.sendPackets(new S_ServerMessage(276));
            pc.sendPackets(new S_Paralysis(7, false));
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
    }
}
