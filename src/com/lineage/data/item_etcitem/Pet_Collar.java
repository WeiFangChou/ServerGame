package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.lock.PetReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Pet;

public class Pet_Collar extends ItemExecutor {
    private Pet_Collar() {
    }

    public static ItemExecutor get() {
        return new Pet_Collar();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (!pc.getInventory().checkItem(41160)) {
            pc.sendPackets(new S_ServerMessage(79));
        } else if (withdrawPet(pc, item.getId())) {
            pc.getInventory().consumeItem(41160, 1);
        }
    }

    private boolean withdrawPet(L1PcInstance pc, int itemObjectId) {
        int divisor;
        if (!pc.getMap().isTakePets()) {
            pc.sendPackets(new S_ServerMessage(563));
            return false;
        }
        int petCost = 0;
        Object[] petList = pc.getPetList().values().toArray();
        for (Object pet : petList) {
            if ((pet instanceof L1PetInstance) && ((L1PetInstance) pet).getItemObjId() == itemObjectId) {
                return false;
            }
            petCost += ((L1NpcInstance) pet).getPetcost();
        }
        int charisma = pc.getCha();
        if (pc.isCrown()) {
            charisma += 6;
        } else if (pc.isElf()) {
            charisma += 12;
        } else if (pc.isWizard()) {
            charisma += 6;
        } else if (pc.isDarkelf()) {
            charisma += 6;
        } else if (pc.isDragonKnight()) {
            charisma += 6;
        } else if (pc.isIllusionist()) {
            charisma += 6;
        }
        L1Pet l1pet = PetReading.get().getTemplate(itemObjectId);
        if (l1pet != null) {
            int npcId = l1pet.get_npcid();
            int charisma2 = charisma - petCost;
            if (npcId == 45313 || npcId == 45710 || npcId == 45711 || npcId == 45712) {
                divisor = 12;
            } else {
                divisor = 6;
            }
            if (charisma2 / divisor <= 0) {
                pc.sendPackets(new S_ServerMessage(489));
                return false;
            }
            new L1PetInstance(NpcTable.get().getTemplate(npcId), pc, l1pet).setPetcost(divisor);
        }
        return true;
    }
}
