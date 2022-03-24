package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.lock.PetReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_NewMaster;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Pet;

public class Pet_CollarX extends ItemExecutor {
    private Pet_CollarX() {
    }

    public static ItemExecutor get() {
        return new Pet_CollarX();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (!pc.getInventory().checkItem(49352)) {
            pc.sendPackets(new S_ServerMessage(337, "勇敢的玉石(1)"));
        } else if (withdrawPet(pc, item.getId())) {
            pc.getInventory().consumeItem(49352, 1);
        }
    }

    private boolean withdrawPet(L1PcInstance pc, int itemObjectId) {
        if (!pc.getMap().isTakePets()) {
            pc.sendPackets(new S_ServerMessage(563));
            return false;
        } else if (pc.getPetList().values().toArray().length >= 1) {
            pc.sendPackets(new S_ServerMessage(489));
            return false;
        } else {
            L1Pet l1pet = PetReading.get().getTemplate(itemObjectId);
            if (l1pet != null) {
                L1PetInstance pet = new L1PetInstance(NpcTable.get().getTemplate(l1pet.get_npcid()), pc, l1pet);
                pet.setPetcost(128);
                pc.sendPackets(new S_NewMaster(pc.getName(), pet));
            }
            return true;
        }
    }
}
