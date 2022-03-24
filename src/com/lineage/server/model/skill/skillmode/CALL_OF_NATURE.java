package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1TowerInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import java.util.Iterator;

public class CALL_OF_NATURE extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        if (cha instanceof L1PcInstance) {
            L1PcInstance pc = (L1PcInstance) cha;
            if (srcpc.getId() != pc.getId()) {
                if (World.get().getVisiblePlayer(pc, 0).size() > 0) {
                    Iterator<L1PcInstance> it = World.get().getVisiblePlayer(pc, 0).iterator();
                    while (it.hasNext()) {
                        if (!it.next().isDead()) {
                            srcpc.sendPackets(new S_ServerMessage(592));
                            break;
                        }
                    }
                }
                if (pc.isDead()) {
                    pc.setTempID(srcpc.getId());
                    pc.sendPackets(new S_Message_YN(322));
                }
            }
        }
        if ((cha instanceof L1NpcInstance) && !(cha instanceof L1TowerInstance)) {
            L1NpcInstance npc = (L1NpcInstance) cha;
            if (!npc.getNpcTemplate().isCantResurrect()) {
                if ((npc instanceof L1PetInstance) && World.get().getVisiblePlayer(npc, 0).size() > 0) {
                    Iterator<L1PcInstance> it2 = World.get().getVisiblePlayer(npc, 0).iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            if (!it2.next().isDead()) {
                                srcpc.sendPackets(new S_ServerMessage(592));
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
                if (npc.isDead()) {
                    npc.resurrect(cha.getMaxHp());
                    npc.setResurrect(true);
                }
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
    }
}
