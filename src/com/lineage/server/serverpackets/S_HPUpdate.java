package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.RangeInt;

public class S_HPUpdate extends ServerBasePacket {
    private static final RangeInt _hpRange = new RangeInt(1, 32767);
    private byte[] _byte = null;

    public S_HPUpdate(int currentHp, int maxHp) {
        buildPacket(currentHp, maxHp);
    }

    public S_HPUpdate(L1PcInstance pc) {
        buildPacket(pc.getCurrentHp(), pc.getMaxHp());
    }

    public void buildPacket(int currentHp, int maxHp) {
        writeC(42);
        writeH(_hpRange.ensure(currentHp));
        writeH(_hpRange.ensure(maxHp));
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
