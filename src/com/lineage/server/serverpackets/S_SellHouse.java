package com.lineage.server.serverpackets;

public class S_SellHouse extends ServerBasePacket {
    private byte[] _byte = null;

    public S_SellHouse(int objectId, String houseNumber) {
        buildPacket(objectId, houseNumber);
    }

    private void buildPacket(int objectId, String houseNumber) {
        writeC(253);
        writeD(objectId);
        writeD(0);
        writeD(100000);
        writeD(100000);
        writeD(2000000000);
        writeH(0);
        writeS("agsell");
        writeS("agsell " + houseNumber);
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
