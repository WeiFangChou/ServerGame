package com.lineage.server.serverpackets;

public class S_Poison extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Poison(int objId, int type) {
        writeC(93);
        writeD(objId);
        switch (type) {
            case 0:
                writeC(0);
                writeC(0);
                return;
            case 1:
                writeC(1);
                writeC(0);
                return;
            case 2:
                writeC(0);
                writeC(1);
                return;
            default:
                return;
        }
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
