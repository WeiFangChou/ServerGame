package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;

public class S_NoSell extends ServerBasePacket {
    private byte[] _byte = null;

    public S_NoSell(L1NpcInstance npc) {
        buildPacket(npc);
    }

    private void buildPacket(L1NpcInstance npc) {
        writeC(OpcodesServer.S_OPCODE_SHOWHTML);
        writeD(npc.getId());
        writeS("nosell");
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
