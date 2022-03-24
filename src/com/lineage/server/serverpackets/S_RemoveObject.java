package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Object;

public class S_RemoveObject extends ServerBasePacket {
    private byte[] _byte = null;

    public S_RemoveObject(L1Object obj) {
        writeC(185);
        writeD(obj.getId());
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
