package com.lineage.server.serverpackets;

public class S_Bonusstats extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Bonusstats(int objid) {
        buildPacket(objid);
    }

    private void buildPacket(int objid) {
        writeC(OpcodesServer.S_OPCODE_SHOWHTML);
        writeD(objid);
        writeS("RaiseAttr");
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
