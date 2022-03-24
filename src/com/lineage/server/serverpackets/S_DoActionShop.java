package com.lineage.server.serverpackets;

public class S_DoActionShop extends ServerBasePacket {
    public S_DoActionShop(int object, byte[] message) {
        writeC(218);
        writeD(object);
        writeC(70);
        writeByte(message);
    }

    public S_DoActionShop(int object, String message) {
        writeC(218);
        writeD(object);
        writeC(70);
        writeS(message);
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        return getBytes();
    }
}
