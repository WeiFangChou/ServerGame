package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_CharVisualUpdate extends ServerBasePacket {
    private byte[] _byte = null;

    public S_CharVisualUpdate(int objid, int weaponType) {
        writeC(113);
        writeD(objid);
        writeC(weaponType);
    }

    public S_CharVisualUpdate(L1PcInstance cha) {
        writeC(113);
        writeD(cha.getId());
        writeC(cha.getCurrentWeapon());
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = getBytes();
        }
        return this._byte;
    }
}
