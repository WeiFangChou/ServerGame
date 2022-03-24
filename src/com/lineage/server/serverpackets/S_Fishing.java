package com.lineage.server.serverpackets;

public class S_Fishing extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Fishing(int objectId, int motionNum, int x, int y) {
        buildPacket(objectId, motionNum, x, y);
    }

    private void buildPacket(int objectId, int motionNum, int x, int y) {
        writeC(218);
        writeD(objectId);
        writeC(motionNum);
        writeH(x);
        writeH(y);
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
