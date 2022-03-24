package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;

public class S_NPCPack_Pet extends ServerBasePacket {
    private static final int STATUS_POISON = 1;
    private byte[] _byte = null;

    public S_NPCPack_Pet(L1PetInstance pet, L1PcInstance pc) {
        buildPacket(pet, pc);
    }

    private void buildPacket(L1PetInstance pet, L1PcInstance pc) {
        writeC(3);
        writeH(pet.getX());
        writeH(pet.getY());
        writeD(pet.getId());
        writeH(pet.getGfxId());
        writeC(pet.getStatus());
        writeC(pet.getHeading());
        writeC(pet.getChaLightSize());
        writeC(pet.getMoveSpeed());
        writeD((int) pet.getExp());
        writeH(pet.getTempLawful());
        writeS(pet.getName());
        writeS(pet.getTitle());
        int status = 0;
        if (pet.getPoison() != null && pet.getPoison().getEffectId() == 1) {
            status = 1;
        }
        writeC(status);
        writeD(0);
        writeS(null);
        StringBuilder stringBuilder = new StringBuilder();
        if (pet.getMaster() != null) {
            if (pet.getMaster() instanceof L1PcInstance) {
                L1PcInstance master = (L1PcInstance) pet.getMaster();
                if (master.get_other().get_color() != 0) {
                    stringBuilder.append(master.get_other().color());
                }
            }
            stringBuilder.append(pet.getMaster().getName());
        } else {
            stringBuilder.append("");
        }
        writeS(stringBuilder.toString());
        writeC(0);
        if (pet.getMaster() == null || pet.getMaster().getId() != pc.getId()) {
            writeC(255);
        } else {
            writeC((pet.getCurrentHp() * 100) / pet.getMaxHp());
        }
        writeC(0);
        writeC(0);
        writeC(0);
        writeC(255);
        writeC(255);
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
        return getClass().getSimpleName();
    }
}
