package com.lineage.server.serverpackets;

public class S_Weather extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Weather(int weather) {
        buildPacket(weather);
    }

    private void buildPacket(int weather) {
        writeC(193);
        writeC(weather);
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
