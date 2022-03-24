package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_ServerMessage;

public class ELEMENTAL_FALL_DOWN extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        if (!cha.hasSkillEffect(133)) {
            int playerAttr = srcpc.getElfAttr();
            if (!(cha instanceof L1PcInstance)) {
                if (cha instanceof L1MonsterInstance) {
                    L1MonsterInstance mob = (L1MonsterInstance) cha;
                    switch (playerAttr) {
                        case 0:
                            srcpc.sendPackets(new S_ServerMessage(79));
                            break;
                        case 1:
                            mob.addEarth(-50);
                            mob.setAddAttrKind(1);
                            break;
                        case 2:
                            mob.addFire(-50);
                            mob.setAddAttrKind(2);
                            break;
                        case 4:
                            mob.addWater(-50);
                            mob.setAddAttrKind(4);
                            break;
                        case 8:
                            mob.addWind(-50);
                            mob.setAddAttrKind(8);
                            break;
                    }
                }
            } else {
                L1PcInstance pc = (L1PcInstance) cha;
                switch (playerAttr) {
                    case 0:
                        srcpc.sendPackets(new S_ServerMessage(79));
                        break;
                    case 1:
                        pc.addEarth(-50);
                        pc.setAddAttrKind(1);
                        break;
                    case 2:
                        pc.addFire(-50);
                        pc.setAddAttrKind(2);
                        break;
                    case 4:
                        pc.addWater(-50);
                        pc.setAddAttrKind(4);
                        break;
                    case 8:
                        pc.addWind(-50);
                        pc.setAddAttrKind(8);
                        break;
                }
            }
        }
        cha.setSkillEffect(133, integer * L1SkillId.STATUS_BRAVE);
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
            switch (pc.getAddAttrKind()) {
                case 1:
                    pc.addEarth(50);
                    break;
                case 2:
                    pc.addFire(50);
                    break;
                case 4:
                    pc.addWater(50);
                    break;
                case 8:
                    pc.addWind(50);
                    break;
            }
            pc.setAddAttrKind(0);
            pc.sendPackets(new S_OwnCharAttrDef(pc));
        } else if (cha instanceof L1NpcInstance) {
            L1NpcInstance npc = (L1NpcInstance) cha;
            switch (npc.getAddAttrKind()) {
                case 1:
                    npc.addEarth(50);
                    break;
                case 2:
                    npc.addFire(50);
                    break;
                case 4:
                    npc.addWater(50);
                    break;
                case 8:
                    npc.addWind(50);
                    break;
            }
            npc.setAddAttrKind(0);
        }
    }
}
