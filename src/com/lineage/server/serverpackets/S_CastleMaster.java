package com.lineage.server.serverpackets;

public class S_CastleMaster extends ServerBasePacket {
    private byte[] _byte = null;

    public S_CastleMaster(int type, int objecId) {
        buildPacket(type, objecId);
    }

    private void buildPacket(int type, int objecId) {
        writeC(66);
        writeC(type);
        writeD(objecId);
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
