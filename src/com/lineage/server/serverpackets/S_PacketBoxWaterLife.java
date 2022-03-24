package com.lineage.server.serverpackets;

public class S_PacketBoxWaterLife extends ServerBasePacket {
    private byte[] _byte = null;

    public S_PacketBoxWaterLife() {
        writeC(40);
        writeC(59);
        writeH(0);
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
