package com.lineage.server.serverpackets;

public class S_TaxRate extends ServerBasePacket {
    private byte[] _byte = null;

    public S_TaxRate(int objecId) {
        writeC(72);
        writeD(objecId);
        writeC(10);
        writeC(50);
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
