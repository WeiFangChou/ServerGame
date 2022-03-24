package com.lineage.server.serverpackets;

public class S_PacketBoxTest extends ServerBasePacket {
    private byte[] _byte = null;

    public S_PacketBoxTest() {
        writeC(40);
        writeC(82);
        writeC(150);
        writeC(0);
        writeC(0);
        writeC(0);
    }

    public S_PacketBoxTest(byte ocid, int time) {
        writeC(40);
        writeC(ocid);
        writeC(time);
        writeC(0);
        writeC(0);
        writeC(0);
    }

    public S_PacketBoxTest(int type, int time) {
        writeC(40);
        writeC(type);
        writeH(time);
        writeH(0);
    }

    public S_PacketBoxTest(int value, String[] clanName) {
        writeC(40);
        writeC(value);
        for (int i = 0; i < value; i++) {
            writeS(clanName[i]);
        }
    }

    public S_PacketBoxTest(int time) {
        writeC(40);
        writeC(79);
        writeC(2);
        writeS("TEMP");
        writeS("AASS");
    }

    public S_PacketBoxTest(byte[] bs) {
        for (byte outbs : bs) {
            writeC(outbs);
        }
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
