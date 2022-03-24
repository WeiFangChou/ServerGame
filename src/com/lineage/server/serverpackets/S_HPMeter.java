package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Character;

public class S_HPMeter extends ServerBasePacket {
    private byte[] _byte = null;

    public S_HPMeter(int objId, int hpRatio) {
        buildPacket(objId, hpRatio);
    }

    public S_HPMeter(L1Character cha) {
        buildPacket(cha.getId(), cha.getMaxHp() > 0 ? (cha.getCurrentHp() * 100) / cha.getMaxHp() : 100);
    }

    private void buildPacket(int objId, int hpRatio) {
        writeC(128);
        writeD(objId);
        writeC(hpRatio);
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
