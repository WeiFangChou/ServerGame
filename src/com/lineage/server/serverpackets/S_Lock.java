package com.lineage.server.serverpackets;

public class S_Lock extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Lock() {
        buildPacket();
    }

    private void buildPacket() {
        writeC(OpcodesServer.S_OPCODE_CHARLOCK);
        writeC(0);
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
