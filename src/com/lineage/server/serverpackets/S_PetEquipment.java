package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PetInstance;

public class S_PetEquipment extends ServerBasePacket {
    private static final String S_PET_EQUIPMENT = "[S] S_PetEquipment";

    public S_PetEquipment(int i, L1PetInstance pet, int j) {
        writeC(40);
        writeC(37);
        writeC(i);
        writeD(pet.getId());
        writeC(j);
        writeC(pet.getAc());
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        return getBytes();
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public String getType() {
        return S_PET_EQUIPMENT;
    }
}
