package com.lineage.server.serverpackets;

public class S_PacketBoxWisdomPotion extends ServerBasePacket {
    public static final int WISDOM_POTION = 57;
    private byte[] _byte = null;

    public S_PacketBoxWisdomPotion(int time) {
        writeC(40);
        writeC(57);
        writeC(44);
        writeH(time);
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
