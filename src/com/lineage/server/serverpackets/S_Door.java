package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1DoorInstance;

public class S_Door extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Door(L1DoorInstance door) {
        buildPacket(door.getEntranceX(), door.getEntranceY(), door.getDirection(), door.getPassable());
    }

    public S_Door(int x, int y, int direction, int passable) {
        buildPacket(x, y, direction, passable);
    }

    private void buildPacket(int x, int y, int direction, int passable) {
        writeC(35);
        writeH(x);
        writeH(y);
        writeC(direction);
        writeC(passable);
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
