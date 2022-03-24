package com.lineage.server.serverpackets;

public class S_PacketBoxLoc extends ServerBasePacket {
    public static final int SEND_LOC = 111;
    private byte[] _byte = null;

    public S_PacketBoxLoc(String name, int map, int x, int y, int zone) {
        writeC(40);
        writeC(111);
        writeS(name);
        writeH(map);
        writeH(x);
        writeH(y);
        writeD(zone);
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
