package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_Strup extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Strup(L1PcInstance pc, int type, int time) {
        writeC(OpcodesServer.S_OPCODE_STRUP);
        writeH(time);
        writeC(pc.getStr());
        writeC(pc.getInventory().getWeight240());
        writeC(type);
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
