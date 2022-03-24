package com.lineage.server.serverpackets;

public class S_TrueTarget extends ServerBasePacket {
    private byte[] _byte = null;

    public S_TrueTarget(int targetId, int objectId, String message) {
        buildPacket(targetId, objectId, message);
    }

    private void buildPacket(int targetId, int objectId, String message) {
        writeC(110);
        writeD(targetId);
        writeD(objectId);
        writeS(message);
    }

    public S_TrueTarget(int targetId, int gfxid) {
        writeC(110);
        writeD(targetId);
        writeD(targetId);
        writeS(null);
        writeH(gfxid);
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
