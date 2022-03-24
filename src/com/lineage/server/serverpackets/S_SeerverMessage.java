package com.lineage.server.serverpackets;

public class S_SeerverMessage extends ServerBasePacket {
    private byte[] _byte = null;

    public S_SeerverMessage(String name) {
        writeC(10);
        writeC(3);
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
