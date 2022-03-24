package com.lineage.server.serverpackets;

public class S_RedMessage extends ServerBasePacket {
    private byte[] _byte = null;

    public S_RedMessage(String acc, String msg1) {
        buildPacket(acc, new String[]{msg1});
    }

    private void buildPacket(String acc, String[] info) {
        writeC(90);
        writeS(acc);
        if (info == null) {
            writeC(0);
            return;
        }
        writeC(info.length);
        for (String str : info) {
            writeS(str);
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
