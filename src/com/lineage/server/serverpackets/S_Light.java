package com.lineage.server.serverpackets;

public class S_Light extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Light(int objid, int type) {
        buildPacket(objid, type);
    }

    private void buildPacket(int objid, int type) {
        writeC(53);
        writeD(objid);
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
