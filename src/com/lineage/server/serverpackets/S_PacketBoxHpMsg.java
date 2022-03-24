package com.lineage.server.serverpackets;

public class S_PacketBoxHpMsg extends ServerBasePacket {
    private static final int MSG_FEEL_GOOD = 31;
    private byte[] _byte = null;

    public S_PacketBoxHpMsg() {
        writeC(40);
        writeC(31);
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
