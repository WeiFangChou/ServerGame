package com.lineage.server.serverpackets;

public class S_SystemMessage extends ServerBasePacket {
    private byte[] _byte = null;

    public S_SystemMessage(String msg) {
        writeC(10);
        writeC(9);
        writeS(msg);
    }

    public S_SystemMessage(String msg, boolean nameid) {
        writeC(133);
        writeC(2);
        writeD(0);
        writeS(msg);
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
