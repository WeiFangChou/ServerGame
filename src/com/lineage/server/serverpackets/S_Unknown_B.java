package com.lineage.server.serverpackets;

public class S_Unknown_B extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Unknown_B() {
        writeC(33);
        writeC(10);
        writeC(2);
        writeC(0);
        writeC(0);
        writeC(0);
        writeC(43);
        writeC(127);
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
