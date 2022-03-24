package com.lineage.server.serverpackets;

public class S_PacketBoxItemLv extends ServerBasePacket {
    public static final int MSG_LEVEL_OVER = 12;
    private byte[] _byte = null;

    public S_PacketBoxItemLv(int minLv, int maxLv) {
        writeC(40);
        writeC(12);
        writeC(minLv);
        writeC(maxLv);
    }

    public S_PacketBoxItemLv(int opid) {
        writeC(opid);
        writeC(12);
        writeC(10);
        writeC(1249);
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
