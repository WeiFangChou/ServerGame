package com.lineage.server.serverpackets;

public class S_CurseBlind extends ServerBasePacket {
    private byte[] _byte = null;

    public S_CurseBlind(int type) {
        buildPacket(type);
    }

    private void buildPacket(int type) {
        writeC(238);
        writeH(type);
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
