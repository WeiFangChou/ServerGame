package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.RangeInt;

public class S_MPUpdate extends ServerBasePacket {
    private static final RangeInt _mpRangeA = new RangeInt(0, 32767);
    private static final RangeInt _mpRangeX = new RangeInt(1, 32767);
    private byte[] _byte = null;

    public S_MPUpdate(int currentmp, int maxmp) {
        buildPacket(currentmp, maxmp);
    }

    public S_MPUpdate(L1PcInstance pc) {
        buildPacket(pc.getCurrentMp(), pc.getMaxMp());
    }

    private void buildPacket(int currentmp, int maxmp) {
        writeC(73);
        writeH(_mpRangeA.ensure(currentmp));
        writeH(_mpRangeX.ensure(maxmp));
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
