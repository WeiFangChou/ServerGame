package com.lineage.server.model.skill.skillmode;

import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.serverpackets.S_ServerMessage;

public class LESSER_ELEMENTAL extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        L1PcInstance pc = (L1PcInstance) cha;
        int attr = pc.getElfAttr();
        if (attr != 0) {
            if (!pc.getMap().isRecallPets()) {
                pc.sendPackets(new S_ServerMessage(353));
                return 0;
            }
            int petcost = 0;
            for (Object pet : pc.getPetList().values().toArray()) {
                petcost += ((L1NpcInstance) pet).getPetcost();
            }
            if (petcost == 0) {
                int summonid = 0;
                switch (attr) {
                    case 1:
                        summonid = 45306;
                        break;
                    case 2:
                        summonid = 45303;
                        break;
                    case 4:
                        summonid = 45304;
                        break;
                    case 8:
                        summonid = 45305;
                        break;
                }
                if (summonid != 0) {
                    new L1SummonInstance(NpcTable.get().getTemplate(summonid), pc).setPetcost(pc.getCha() + 7);
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
