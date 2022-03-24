package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_Exp extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Exp(L1PcInstance pc) {
        writeC(121);
        writeC(pc.getLevel());
        writeExp(pc.getExp());
        writeC(1);
    }

    public S_Exp() {
        writeC(121);
        writeC(59);
        writeD(414931028);
        writeC(1);
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
