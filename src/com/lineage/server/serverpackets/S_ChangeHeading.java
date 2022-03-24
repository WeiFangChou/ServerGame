package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Character;

public class S_ChangeHeading extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ChangeHeading(L1Character cha) {
        buildPacket(cha);
    }

    private void buildPacket(L1Character cha) {
        writeC(199);
        writeD(cha.getId());
        writeC(cha.getHeading());
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
