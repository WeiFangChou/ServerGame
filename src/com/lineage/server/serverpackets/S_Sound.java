package com.lineage.server.serverpackets;

public class S_Sound extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Sound(int sound) {
        buildPacket(sound, 0);
    }

    public S_Sound(int sound, int repeat) {
        buildPacket(sound, repeat);
    }

    private void buildPacket(int sound, int repeat) {
        writeC(84);
        writeC(repeat);
        writeH(sound);
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
