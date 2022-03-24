package com.lineage.server.serverpackets;

import com.lineage.server.templates.L1Config;

public class S_PacketBoxConfig extends ServerBasePacket {
    public static final int CHARACTER_CONFIG = 41;
    private byte[] _byte = null;

    public S_PacketBoxConfig(L1Config config) {
        int length = config.getLength();
        byte[] data = config.getData();
        writeC(40);
        writeC(41);
        writeD(length);
        writeByte(data);
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
