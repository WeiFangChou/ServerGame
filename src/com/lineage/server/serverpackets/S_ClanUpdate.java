package com.lineage.server.serverpackets;

public class S_ClanUpdate extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ClanUpdate(int objid, String Clanname, int rank) {
        writeC(8);
        writeD(objid);
        writeS(Clanname);
        writeS(null);
        writeD(0);
        writeC(rank);
    }

    public S_ClanUpdate(int objid) {
        writeC(8);
        writeD(objid);
        writeS(null);
        writeS(null);
        writeD(0);
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
