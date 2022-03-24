package com.lineage.server.serverpackets;

public class S_PacketBoxIcon1 extends ServerBasePacket {
    private static final int _dodge_down = 101;
    private static final int _dodge_up = 88;
    private static final int _icons_1 = 20;
    private byte[] _byte = null;

    public S_PacketBoxIcon1(int type, int time) {
        writeC(40);
        writeC(20);
        writeH(time);
        writeC(type);
    }

    public S_PacketBoxIcon1(boolean type, int i) {
        writeC(40);
        if (type) {
            writeC(88);
            writeC(i);
            return;
        }
        writeC(101);
        writeC(i);
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
