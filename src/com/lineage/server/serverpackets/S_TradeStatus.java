package com.lineage.server.serverpackets;

public class S_TradeStatus extends ServerBasePacket {
    private byte[] _byte = null;

    public S_TradeStatus(int type) {
        writeC(239);
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
