package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;

public class S_Letter extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Letter(L1ItemInstance item) {
        writeC(-33);
    }

    public S_Letter() {
        writeC(-33);
        writeD(0);
        writeH(615);
        writeH(0);
        writeS("123");
        writeS("456");
        writeByte(null);
        writeByte(null);
        writeC(0);
        writeS("info");
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
