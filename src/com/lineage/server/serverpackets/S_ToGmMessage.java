package com.lineage.server.serverpackets;

public class S_ToGmMessage extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ToGmMessage(String info) {
        writeC(133);
        writeC(0);
        writeD(0);
        writeS("\\fY" + info);
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
