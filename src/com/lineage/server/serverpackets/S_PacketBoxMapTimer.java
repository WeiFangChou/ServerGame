package com.lineage.server.serverpackets;

public class S_PacketBoxMapTimer extends ServerBasePacket {
    private byte[] _byte = null;

    public S_PacketBoxMapTimer() {
        writeC(40);
        writeC(159);
        writeD(3);
        writeD(1);
        writeS("$12125");
        writeD(OpcodesServer.S_OPCODE_INVLIST);
        writeD(2);
        writeS("$6081");
        writeD(60);
        writeD(3);
        writeS("$12126");
        writeD(OpcodesServer.S_OPCODE_STRUP);
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
        return "[S] " + getClass().getSimpleName() + " [S->C 發送封包盒子(離線切換觀看入場時間)]";
    }
}
