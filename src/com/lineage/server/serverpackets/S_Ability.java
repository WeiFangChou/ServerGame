package com.lineage.server.serverpackets;

public class S_Ability extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Ability(int type, boolean equipped) {
        buildPacket(type, equipped);
    }

    private void buildPacket(int type, boolean equipped) {
        writeC(116);
        writeC(type);
        writeC(equipped ? 1 : 0);
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
