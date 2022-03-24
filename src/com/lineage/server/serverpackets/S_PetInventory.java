package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import java.util.List;

public class S_PetInventory extends ServerBasePacket {
    private static final String S_PET_INVENTORY = "[S] S_PetInventory";
    private byte[] _byte = null;

    public S_PetInventory(L1PetInstance pet) {
        List<L1ItemInstance> itemList = pet.getInventory().getItems();
        writeC(250);
        writeD(pet.getId());
        writeH(itemList.size());
        writeC(11);
        for (L1ItemInstance petItem : itemList) {
            if (petItem != null) {
                writeD(petItem.getId());
                writeC(22);
                writeH(petItem.get_gfxid());
                writeC(petItem.getBless());
                writeD((int) Math.min(petItem.getCount(), 2000000000L));
                if (petItem.getItem().getType2() == 0 && petItem.getItem().getType() == 11 && petItem.isEquipped()) {
                    writeC(petItem.isIdentified() ? 3 : 2);
                } else {
                    writeC(petItem.isIdentified() ? 1 : 0);
                }
                writeS(petItem.getViewName());
            }
        }
        writeC(pet.getAc());
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = getBytes();
        }
        return this._byte;
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public String getType() {
        return S_PET_INVENTORY;
    }
}
