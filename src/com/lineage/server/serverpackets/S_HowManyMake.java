package com.lineage.server.serverpackets;

public class S_HowManyMake extends ServerBasePacket {
    private byte[] _byte = null;

    public S_HowManyMake(int objId, int max, String htmlId) {
        writeC(253);
        writeD(objId);
        writeD(0);
        writeD(0);
        writeD(0);
        writeD(max);
        writeH(0);
        writeS("request");
        writeS(htmlId);
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
