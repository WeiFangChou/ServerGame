package com.lineage.server.serverpackets;

public class S_PacketBoxExp extends ServerBasePacket {
    public static final int LEAVES = 82;
    private byte[] _byte = null;

    public S_PacketBoxExp(int exp) {
        writeC(40);
        writeC(82);
        writeC(exp);
        writeC(0);
        writeC(0);
        writeC(0);
    }

    public S_PacketBoxExp() {
        writeC(40);
        writeC(82);
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
