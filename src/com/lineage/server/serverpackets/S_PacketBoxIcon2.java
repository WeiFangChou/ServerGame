package com.lineage.server.serverpackets;

public class S_PacketBoxIcon2 extends ServerBasePacket {
    private static final int ICONS2 = 21;
    private byte[] _byte = null;

    public S_PacketBoxIcon2(int type, int time) {
        writeC(40);
        writeC(21);
        writeH(time);
        writeC(type);
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
