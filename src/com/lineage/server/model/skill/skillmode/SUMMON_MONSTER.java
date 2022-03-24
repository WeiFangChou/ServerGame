package com.lineage.server.model.skill.skillmode;

import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_ShowSummonList;
import com.lineage.server.templates.L1Npc;

public class SUMMON_MONSTER extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        int level = srcpc.getLevel();
        if (!srcpc.getMap().isRecallPets()) {
            srcpc.sendPackets(new S_ServerMessage(353));
            return 0;
        }
        if (srcpc.getInventory().checkEquipped(20284)) {
            srcpc.sendPackets(new S_ShowSummonList(srcpc.getId()));
            if (!srcpc.isSummonMonster()) {
                srcpc.setShapeChange(false);
                srcpc.setSummonMonster(true);
            }
            start(srcpc, String.valueOf(srcpc.getSummonId()));
        } else {
            int[] summons = {81210, 81213, 81216, 81219, 81222, 81225, 81228};
            int summonid = 0;
            int levelRange = 32;
            int i = 0;
            while (true) {
                if (i >= summons.length) {
                    break;
                } else if (level < levelRange || i == summons.length - 1) {
                    summonid = summons[i];
                } else {
                    levelRange += 4;
                    i++;
                }
            }
            summonid = summons[i];
            int petcost = 0;
            Object[] petlist = srcpc.getPetList().values().toArray();
            int length = petlist.length;
            for (int i2 = 0; i2 < length; i2++) {
                petcost += ((L1NpcInstance) petlist[i2]).getPetcost();
            }
            int summoncount = ((srcpc.getCha() + 6) - petcost) / 6;
            L1Npc npcTemp = NpcTable.get().getTemplate(summonid);
            for (int i3 = 0; i3 < summoncount; i3++) {
                new L1SummonInstance(npcTemp, srcpc).setPetcost(6);
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
        String s = (String) obj;
        int summonid = 0;
        int levelrange = 0;
        int summoncost = 0;
        String[] summonstr_list = {"7", "263", "519", "8", "264", "520", "9", "265", "521", "10", "266", "522", "11", "267", "523", "12", "268", "524", "13", "269", "525", "14", "270", "526", "15", "271", "527", "16", "17", "18", "274"};
        int[] summonid_list = {81210, 81211, 81212, 81213, 81214, 81215, 81216, 81217, 81218, 81219, 81220, 81221, 81222, 81223, 81224, 81225, 81226, 81227, 81228, 81229, 81230, 81231, 81232, 81233, 81234, 81235, 81236, 81237, 81238, 81239, 81240};
        int[] summonlvl_list = {28, 28, 28, 32, 32, 32, 36, 36, 36, 40, 40, 40, 44, 44, 44, 48, 48, 48, 52, 52, 52, 56, 56, 56, 60, 60, 60, 64, 68, 72, 72};
        int[] summoncha_list = {8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 10, 10, 10, 12, 12, 12, 20, 42, 42, 50};
        int loop = 0;
        while (true) {
            if (loop >= summonstr_list.length) {
                break;
            } else if (s.equalsIgnoreCase(summonstr_list[loop])) {
                summonid = summonid_list[loop];
                levelrange = summonlvl_list[loop];
                summoncost = summoncha_list[loop];
                break;
            } else {
                loop++;
            }
        }
        if (srcpc.getLevel() < levelrange) {
            srcpc.sendPackets(new S_ServerMessage(743));
            return;
        }
        int petcost = 0;
        Object[] petlist = srcpc.getPetList().values().toArray();
        int length = petlist.length;
        for (int i = 0; i < length; i++) {
            petcost += ((L1NpcInstance) petlist[i]).getPetcost();
        }
        if ((summonid == 81238 || summonid == 81239 || summonid == 81240) && petcost != 0) {
            srcpc.sendPackets(new S_CloseList(srcpc.getId()));
            return;
        }
        int summoncount = ((srcpc.getCha() + 6) - petcost) / summoncost;
        boolean isStop = false;
        if (srcpc.getCha() + 6 < summoncost) {
            isStop = true;
        }
        if (summoncount <= 0) {
            isStop = true;
        }
        if (isStop) {
            srcpc.sendPackets(new S_ServerMessage(319));
            srcpc.sendPackets(new S_CloseList(srcpc.getId()));
            return;
        }
        L1Npc npcTemp = NpcTable.get().getTemplate(summonid);
        for (int cnt = 0; cnt < summoncount; cnt++) {
            L1SummonInstance summon = new L1SummonInstance(npcTemp, srcpc);
            int upPetcost = 0;
            if (summon.getNameId().equals("$1554")) {
                upPetcost = 7;
            }
            if (summon.getNameId().equals("$2106")) {
                upPetcost = 7;
            }
            if (summon.getNameId().equals("$2587")) {
                upPetcost = 7;
            }
            summon.setPetcost(summoncost + upPetcost);
        }
        srcpc.sendPackets(new S_CloseList(srcpc.getId()));
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void stop(L1Character cha) throws Exception {
    }
}
