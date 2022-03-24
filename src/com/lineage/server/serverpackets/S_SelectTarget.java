package com.lineage.server.serverpackets;

public class S_SelectTarget extends ServerBasePacket {
    private byte[] _byte = null;

    public S_SelectTarget(int ObjectId) {
        writeC(OpcodesServer.S_OPCODE_SELECTTARGET);
        writeD(ObjectId);
        writeC(0);
        writeC(0);
        writeC(2);
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
