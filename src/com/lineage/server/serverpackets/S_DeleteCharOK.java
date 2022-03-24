package com.lineage.server.serverpackets;

public class S_DeleteCharOK extends ServerBasePacket {
    public static final int DELETE_CHAR_AFTER_7DAYS = 81;
    public static final int DELETE_CHAR_NOW = 5;
    private byte[] _byte = null;

    public S_DeleteCharOK(int type) {
        writeC(5);
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
