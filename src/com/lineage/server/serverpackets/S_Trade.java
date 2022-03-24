package com.lineage.server.serverpackets;

public class S_Trade extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Trade(String name) {
        writeC(77);
        writeS(name);
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
