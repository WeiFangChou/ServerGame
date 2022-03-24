package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_ChatOut extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ChatOut(int objid) {
        buildPacket(objid);
    }

    public S_ChatOut(L1PcInstance pc) {
        buildPacket(pc.getId());
    }

    private void buildPacket(int objid) {
        writeC(17);
        writeD(objid);
        writeD(0);
        writeD(0);
        writeD(0);
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
