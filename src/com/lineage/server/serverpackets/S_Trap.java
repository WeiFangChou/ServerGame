package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1TrapInstance;

public class S_Trap extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Trap(L1TrapInstance trap, String name) {
        writeC(3);
        writeH(trap.getX());
        writeH(trap.getY());
        writeD(trap.getId());
        writeH(7);
        writeC(0);
        writeC(0);
        writeC(0);
        writeC(0);
        writeD(0);
        writeH(0);
        writeS(name);
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
