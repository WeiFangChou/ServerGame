package com.lineage.server.serverpackets;

public class S_Invis extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Invis(int objid, int type) {
        buildPacket(objid, type);
    }

    private void buildPacket(int objid, int type) {
        writeC(57);
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
