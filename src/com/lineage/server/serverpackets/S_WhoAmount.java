package com.lineage.server.serverpackets;

public class S_WhoAmount extends ServerBasePacket {
    private byte[] _byte = null;

    public S_WhoAmount(String amount) {
        writeC(14);
        writeH(81);
        writeC(1);
        writeS(amount);
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
