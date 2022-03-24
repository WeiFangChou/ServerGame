package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;

public class S_Resurrection extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Resurrection(L1PcInstance target, L1Character use, int type) {
        writeC(227);
        writeD(target.getId());
        writeC(type);
        writeD(use.getId());
        writeD(target.getClassId());
    }

    public S_Resurrection(L1Character target, L1Character use, int type) {
        writeC(227);
        writeD(target.getId());
        writeC(type);
        writeD(use.getId());
        writeD(target.getGfxId());
    }

    public S_Resurrection(L1PcInstance target, int opid, int type) {
        writeC(opid);
        writeD(target.getId());
        writeC(type);
        writeD(target.getId());
        writeD(target.getClassId());
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
