package com.lineage.server.serverpackets;

import com.lineage.data.event.ShopXSet;

public class S_CnsSell extends ServerBasePacket {
    private byte[] _byte = null;

    public S_CnsSell(int objectId, String htmlid, String command) {
        buildPacket(objectId, htmlid, command);
    }

    private void buildPacket(int objectId, String htmlid, String command) {
        writeC(253);
        writeD(objectId);
        writeD(0);
        writeD(ShopXSet.MIN);
        writeD(ShopXSet.MIN);
        writeD(ShopXSet.MAX);
        writeH(0);
        writeS(htmlid);
        writeS(command);
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
