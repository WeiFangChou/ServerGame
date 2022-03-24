package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1DwarfInstance;

public class S_NPCPack_D extends ServerBasePacket {
    private byte[] _byte = null;

    public S_NPCPack_D(L1DwarfInstance npc) {
        writeC(3);
        writeH(npc.getX());
        writeH(npc.getY());
        writeD(npc.getId());
        writeH(npc.getGfxId());
        writeC(npc.getStatus());
        writeC(npc.getHeading());
        writeC(npc.getChaLightSize());
        writeC(npc.getMoveSpeed());
        writeD((int) npc.getExp());
        writeH(npc.getTempLawful());
        writeS(npc.getNameId());
        writeS(null);
        writeC(0);
        writeD(0);
        writeS(null);
        writeS(null);
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
