package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_ChatParty extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ChatParty(L1PcInstance pc, String chat) {
        buildPacket(pc, chat);
    }

    private void buildPacket(L1PcInstance pc, String chat) {
        writeC(10);
        writeC(11);
        writeS("(" + pc.getName() + ") " + chat);
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
