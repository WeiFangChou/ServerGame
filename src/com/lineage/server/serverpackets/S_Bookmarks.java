package com.lineage.server.serverpackets;

public class S_Bookmarks extends ServerBasePacket {
    public S_Bookmarks(String name, int map, int x, int y, int id) {
        writeC(11);
        writeS(name);
        writeH(map);
        writeH(x);
        writeH(y);
        writeD(id);
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public final byte[] getContent() {
        return getBytes();
    }
}
