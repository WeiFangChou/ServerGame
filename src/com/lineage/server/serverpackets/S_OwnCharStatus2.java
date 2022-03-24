package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_OwnCharStatus2 extends ServerBasePacket {
    private byte[] _byte = null;

    public S_OwnCharStatus2(L1PcInstance pc) {
        if (pc != null) {
            buildPacket(pc);
        }
    }

    private void buildPacket(L1PcInstance pc) {
        writeC(216);
        writeC(pc.getStr());
        writeC(pc.getInt());
        writeC(pc.getWis());
        writeC(pc.getDex());
        writeC(pc.getCon());
        writeC(pc.getCha());
        writeC(pc.getInventory().getWeight240());
    }

    public S_OwnCharStatus2(L1PcInstance pc, int str) {
        writeC(216);
        writeC(str);
        writeC(pc.getInt());
        writeC(pc.getWis());
        writeC(pc.getDex());
        writeC(pc.getCon());
        writeC(pc.getCha());
        writeC(pc.getInventory().getWeight240());
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
