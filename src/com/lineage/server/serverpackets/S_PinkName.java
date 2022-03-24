package com.lineage.server.serverpackets;

public class S_PinkName extends ServerBasePacket {
    private byte[] _byte = null;

    public S_PinkName(int objecId, int time) {
        writeC(OpcodesServer.S_OPCODE_PINKNAME);
        writeD(objecId);
        writeC(time);
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
