package com.lineage.server.serverpackets;

public class S_Drawal extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Drawal(int objectId, long count) {
        writeC(OpcodesServer.S_OPCODE_DRAWAL);
        writeD(objectId);
        writeD((int) Math.min(count, 2000000000L));
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
