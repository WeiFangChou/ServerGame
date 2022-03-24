package com.lineage.server.serverpackets;

public class S_Disconnect extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Disconnect() {
        writeC(95);
        writeH(500);
        writeD(0);
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
