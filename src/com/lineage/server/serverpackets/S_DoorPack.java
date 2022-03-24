package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1DoorInstance;

public class S_DoorPack extends ServerBasePacket {
    private byte[] _byte = null;

    public S_DoorPack(L1DoorInstance door) {
        buildPacket(door);
    }

    private void buildPacket(L1DoorInstance door) {
        writeC(3);
        writeH(door.getX());
        writeH(door.getY());
        writeD(door.getId());
        writeH(door.getGfxId());
        int doorStatus = door.getStatus();
        int openStatus = door.getOpenStatus();
        if (door.isDead()) {
            writeC(doorStatus);
        } else if (openStatus == 28) {
            writeC(openStatus);
        } else if (door.getMaxHp() <= 1 || doorStatus == 0) {
            writeC(openStatus);
        } else {
            writeC(doorStatus);
        }
        writeC(0);
        writeC(0);
        writeC(0);
        writeD(1);
        writeH(0);
        writeS(null);
        writeS(null);
        writeC(0);
        writeD(0);
        writeS(null);
        writeS(null);
        writeC(0);
        writeC(255);
        writeC(0);
        writeC(0);
        writeC(0);
        writeC(255);
        writeC(255);
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
