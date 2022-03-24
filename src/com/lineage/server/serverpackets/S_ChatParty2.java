package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_ChatParty2 extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ChatParty2(L1PcInstance pc, String chat) {
        buildPacket(pc, chat);
    }

    private void buildPacket(L1PcInstance pc, String chat) {
        writeC(76);
        writeC(14);
        writeD(pc.isInvisble() ? 0 : pc.getId());
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
