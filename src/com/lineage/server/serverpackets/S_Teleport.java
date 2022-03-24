package com.lineage.server.serverpackets;

import com.lineage.echo.OpcodesClient;
import java.util.concurrent.atomic.AtomicInteger;

public class S_Teleport extends ServerBasePacket {
    private static AtomicInteger _nextId = new AtomicInteger(100000);
    private byte[] _byte = null;

    public S_Teleport() {
        writeC(4);
        writeC(OpcodesClient.C_OPCODE_WAR);
        writeC(167);
        writeD(_nextId.incrementAndGet());
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
