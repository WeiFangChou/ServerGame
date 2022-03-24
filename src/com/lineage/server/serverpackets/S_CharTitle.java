package com.lineage.server.serverpackets;

public class S_CharTitle extends ServerBasePacket {
    private byte[] _byte = null;

    public S_CharTitle(int objid, StringBuilder title) {
        writeC(202);
        writeD(objid);
        writeS(title.toString());
    }

    public S_CharTitle(int objid) {
        writeC(202);
        writeD(objid);
        writeS("");
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
