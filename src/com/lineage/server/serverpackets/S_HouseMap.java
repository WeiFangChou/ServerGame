package com.lineage.server.serverpackets;

public class S_HouseMap extends ServerBasePacket {
    private byte[] _byte = null;

    public S_HouseMap(int objectId, String house_number) {
        buildPacket(objectId, house_number);
    }

    private void buildPacket(int objectId, String house_number) {
        int number = Integer.valueOf(house_number).intValue();
        writeC(44);
        writeD(objectId);
        writeD(number);
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
