package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;

public class S_ItemName extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ItemName(L1ItemInstance item) {
        if (item != null) {
            writeC(195);
            writeD(item.getId());
            writeS(item.getViewName());
        }
    }

    public S_ItemName(L1ItemInstance item, int id) {
        if (item != null) {
            writeC(195);
            writeD(item.getId());
            writeS(String.valueOf(item.getViewName()) + " ($" + id + ")");
        }
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
