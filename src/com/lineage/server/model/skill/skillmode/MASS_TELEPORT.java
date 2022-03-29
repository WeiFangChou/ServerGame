package com.lineage.server.model.skill.skillmode;

import com.lineage.server.datatables.lock.CharBookReading;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1BookMark;
import com.lineage.server.world.World;

public class MASS_TELEPORT extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        L1PcInstance pc = (L1PcInstance) cha;
        L1BookMark bookm = CharBookReading.get().getBookMark(pc, integer);
        if (bookm != null) {
            if (pc.getMap().isEscapable() || pc.isGm()) {
                int newX = bookm.getLocX();
                int newY = bookm.getLocY();
                int mapId = bookm.getMapId();
                for (L1PcInstance member : World.get().getVisiblePlayer(pc)) {
                    if (pc.getLocation().getTileLineDistance(member.getLocation()) <= 3 && member.getClanid() == pc.getClanid() && pc.getClanid() != 0 && member.getId() != pc.getId() && !member.isPrivateShop()) {
                        member.setTeleportX(newX);
                        member.setTeleportY(newY);
                        member.setTeleportMapId(mapId);
                        member.sendPackets(new S_Message_YN(748));
                    }
                }
                L1Teleport.teleport(pc, newX, newY, mapId, 5, true);
                return 0;
            }
            pc.sendPackets(new S_ServerMessage(276));
            pc.sendPackets(new S_Paralysis(7, false));
            return 0;
        } else if (pc.getMap().isTeleportable() || pc.isGm()) {
            L1Location newLocation = pc.getLocation().randomLocation(200, true);
            int newX2 = newLocation.getX();
            int newY2 = newLocation.getY();
            int mapId2 =  newLocation.getMapId();
            for (L1PcInstance member2 : World.get().getVisiblePlayer(pc)) {
                if (pc.getLocation().getTileLineDistance(member2.getLocation()) <= 3 && member2.getClanid() == pc.getClanid() && pc.getClanid() != 0 && member2.getId() != pc.getId() && !member2.isPrivateShop()) {
                    member2.setTeleportX(newX2);
                    member2.setTeleportY(newY2);
                    member2.setTeleportMapId(mapId2);
                    member2.sendPackets(new S_Message_YN(748));
                }
            }
            L1Teleport.teleport(pc, newX2, newY2, mapId2, 5, true);
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
