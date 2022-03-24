package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1IllusoryInstance;

public class S_NPCPack_Ill extends ServerBasePacket {
    private byte[] _byte = null;

    public S_NPCPack_Ill(L1IllusoryInstance de) {
        writeC(3);
        writeH(de.getX());
        writeH(de.getY());
        writeD(de.getId());
        writeH(de.getTempCharGfx());
        writeC(de.getStatus());
        writeC(de.getHeading());
        writeC(de.getChaLightSize());
        writeC(de.getMoveSpeed());
        writeD(0);
        writeH(de.getLawful());
        writeS(de.getNameId());
        writeS(de.getTitle());
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
