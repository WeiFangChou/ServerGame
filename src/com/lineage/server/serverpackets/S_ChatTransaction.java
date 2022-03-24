package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class S_ChatTransaction extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ChatTransaction(L1PcInstance pc, String chat) {
        buildPacket(pc, chat);
    }

    private void buildPacket(L1PcInstance pc, String chat) {
        writeC(10);
        writeC(12);
        writeS("[" + pc.getName() + "] " + chat);
    }

    public S_ChatTransaction(L1NpcInstance npc, String chat) {
        writeC(10);
        writeC(12);
        writeS("[" + npc.getNameId() + "] " + chat);
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
