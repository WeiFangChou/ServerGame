package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_OwnCharAttrDef extends ServerBasePacket {
    private byte[] _byte = null;

    public S_OwnCharAttrDef(L1PcInstance pc) {
        buildPacket(pc);
    }

    private void buildPacket(L1PcInstance pc) {
        writeC(15);
        writeC(pc.getAc());
        writeC(pc.getFire());
        writeC(pc.getWater());
        writeC(pc.getWind());
        writeC(pc.getEarth());
    }

    public S_OwnCharAttrDef() {
        writeC(15);
        writeC(-99);
        writeC(90);
        writeC(85);
        writeC(80);
        writeC(75);
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
