package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;

public class S_ItemStatus extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ItemStatus(L1ItemInstance item) {
        if (item != null) {
            buildPacket(item);
        }
    }

    private void buildPacket(L1ItemInstance item) {
        writeC(127);
        writeD(item.getId());
        writeS(item.getViewName());
        writeD((int) Math.min(item.getCount(), 2000000000L));
        if (!item.isIdentified()) {
            writeC(0);
            return;
        }
        byte[] status = item.getStatusBytes();
        writeC(status.length);
        for (byte b : status) {
            writeC(b);
        }
    }

    public S_ItemStatus(L1ItemInstance item, long count) {
        writeC(127);
        writeD(item.getId());
        writeS(item.getNumberedViewName(count));
        writeD((int) Math.min(count, 2000000000L));
        if (!item.isIdentified()) {
            writeC(0);
            return;
        }
        byte[] status = item.getStatusBytes();
        writeC(status.length);
        for (byte b : status) {
            writeC(b);
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
