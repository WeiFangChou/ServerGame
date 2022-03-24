package com.lineage.server.serverpackets;

public class S_Teleport2 extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Teleport2(int mapid, int id) {
        writeC(46);
        writeH(mapid);
        writeD(id);
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
