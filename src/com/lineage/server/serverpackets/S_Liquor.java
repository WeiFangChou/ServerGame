package com.lineage.server.serverpackets;

public class S_Liquor extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Liquor(int objecId) {
        writeC(31);
        writeD(objecId);
        writeC(1);
    }

    public S_Liquor(int objecId, int type) {
        writeC(31);
        writeD(objecId);
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
