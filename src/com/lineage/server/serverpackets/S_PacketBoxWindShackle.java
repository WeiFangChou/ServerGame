package com.lineage.server.serverpackets;

public class S_PacketBoxWindShackle extends ServerBasePacket {
    public static final int WIND_SHACKLE = 44;
    private byte[] _byte = null;

    public S_PacketBoxWindShackle(int objectId, int time) {
        writeC(40);
        writeC(44);
        writeD(objectId);
        writeH(time >> 2);
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
