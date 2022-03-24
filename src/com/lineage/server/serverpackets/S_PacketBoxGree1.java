package com.lineage.server.serverpackets;

public class S_PacketBoxGree1 extends ServerBasePacket {
    private static final int GREEN_MESSAGE = 84;
    private static final int SECRETSTORY_GFX = 83;
    private byte[] _byte = null;

    public S_PacketBoxGree1(int type, String msg) {
        writeC(40);
        writeC(84);
        writeC(type);
        writeS(msg);
    }

    public S_PacketBoxGree1(int type) {
        writeC(40);
        writeC(83);
        writeD(type);
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
