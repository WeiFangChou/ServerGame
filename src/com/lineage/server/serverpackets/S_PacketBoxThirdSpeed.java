package com.lineage.server.serverpackets;

public class S_PacketBoxThirdSpeed extends ServerBasePacket {
    public static final int CAKE = 60;
    private byte[] _byte = null;

    public S_PacketBoxThirdSpeed(int time) {
        writeC(40);
        writeC(60);
        writeC(time >> 2);
        writeC(8);
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
