package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;

public class S_ItemColor extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ItemColor(L1ItemInstance item) {
        if (item != null) {
            buildPacket(item);
        }
    }

    private void buildPacket(L1ItemInstance item) {
        writeC(144);
        writeD(item.getId());
        writeC(item.getBless());
    }

    public S_ItemColor(L1ItemInstance item, int id) {
        writeC(144);
        writeD(item.getId());
        writeC(id);
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
