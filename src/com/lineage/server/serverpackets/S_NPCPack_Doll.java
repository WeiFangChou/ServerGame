package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class S_NPCPack_Doll extends ServerBasePacket {
    private byte[] _byte = null;

    public S_NPCPack_Doll(L1DollInstance pet, L1PcInstance pc) {
        writeC(3);
        writeH(pet.getX());
        writeH(pet.getY());
        writeD(pet.getId());
        writeH(pet.getGfxId());
        writeC(pet.getStatus());
        writeC(pet.getHeading());
        writeC(0);
        writeC(pet.getMoveSpeed());
        writeD(0);
        writeH(0);
        writeS(pet.getNameId());
        writeS(pet.getTitle());
        writeC(0);
        writeD(0);
        writeS(null);
        if (pet.getMaster() == null) {
            writeS("");
        } else if (pet.getMaster() instanceof L1PcInstance) {
            writeS(pet.getMaster().getName());
        } else if (pet.getMaster() instanceof L1NpcInstance) {
            writeS(((L1NpcInstance) pet.getMaster()).getNameId());
        }
        writeC(0);
        writeC(255);
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
