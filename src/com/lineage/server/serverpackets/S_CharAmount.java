package com.lineage.server.serverpackets;

import com.lineage.config.ConfigAlt;
import com.lineage.echo.ClientExecutor;

public class S_CharAmount extends ServerBasePacket {
    private byte[] _byte = null;

    public S_CharAmount(int value, ClientExecutor client) {
        buildPacket(value, client);
    }

    private void buildPacket(int value, ClientExecutor client) {
        int characterSlot = client.getAccount().get_character_slot();
        writeC(126);
        writeC(value);
        writeC(ConfigAlt.DEFAULT_CHARACTER_SLOT + characterSlot);
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
