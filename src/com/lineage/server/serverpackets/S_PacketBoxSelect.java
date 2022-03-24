package com.lineage.server.serverpackets;

public class S_PacketBoxSelect extends ServerBasePacket {
    public static final int LOGOUT = 42;
    private byte[] _byte = null;

    public S_PacketBoxSelect() {
        writeC(40);
        writeC(42);
        writeC(0);
        writeC(0);
        writeC(0);
        writeC(0);
        writeC(0);
        writeC(0);
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
