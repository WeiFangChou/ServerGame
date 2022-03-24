package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;

public class S_DeleteInventoryItem extends ServerBasePacket {
    private byte[] _byte = null;

    public S_DeleteInventoryItem(L1ItemInstance item) {
        writeC(148);
        writeD(item.getId());
    }

    public S_DeleteInventoryItem(int objid) {
        writeC(148);
        writeD(objid);
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
