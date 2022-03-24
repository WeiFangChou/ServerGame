package com.lineage.server.serverpackets;

public class S_CharCreateStatus extends ServerBasePacket {
    public static final int REASON_ALREADY_EXSISTS = 6;
    public static final int REASON_INVALID_NAME = 9;
    public static final int REASON_OK = 2;
    public static final int REASON_WRONG_AMOUNT = 21;
    private byte[] _byte = null;

    public S_CharCreateStatus(int reason) {
        writeC(153);
        writeC(reason);
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
