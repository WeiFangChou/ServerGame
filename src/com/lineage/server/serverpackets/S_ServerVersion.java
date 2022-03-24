package com.lineage.server.serverpackets;

import com.lineage.config.Config;

public class S_ServerVersion extends ServerBasePacket {
    private static final int CLIENT_LANGUAGE = Config.CLIENT_LANGUAGE;
    private static final int UPTIME = ((int) (System.currentTimeMillis() / 1000));
    private byte[] _byte = null;

    public S_ServerVersion() {
        writeC(151);
        writeC(0);
        writeC(13);
        writeD(Config.SVer);
        writeD(Config.CVer);
        writeD(Config.AVer);
        writeD(Config.NVer);
        writeD(0);
        writeC(0);
        writeC(0);
        writeC(CLIENT_LANGUAGE);
        writeD(Config.Time);
        writeD(UPTIME);
        writeC(1);
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
