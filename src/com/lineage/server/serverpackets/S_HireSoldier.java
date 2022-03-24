package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_HireSoldier extends ServerBasePacket {
    private byte[] _byte = null;

    public S_HireSoldier(L1PcInstance pc) {
        writeC(126);
        writeH(0);
        writeH(0);
        writeH(0);
        writeS(pc.getName());
        writeD(0);
        writeH(0);
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
