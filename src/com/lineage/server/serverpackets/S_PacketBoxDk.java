package com.lineage.server.serverpackets;

public class S_PacketBoxDk extends ServerBasePacket {
    public static final int LV1 = 1;
    public static final int LV2 = 2;
    public static final int LV3 = 3;
    private byte[] _byte = null;

    public S_PacketBoxDk(int lv) {
        writeC(40);
        writeC(75);
        writeC(lv);
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
