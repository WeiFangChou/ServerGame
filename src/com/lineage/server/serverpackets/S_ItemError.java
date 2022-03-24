package com.lineage.server.serverpackets;

public class S_ItemError extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ItemError(int skillid) {
        buildPacket(skillid);
    }

    private void buildPacket(int skillid) {
        writeC(89);
        writeD(skillid);
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
