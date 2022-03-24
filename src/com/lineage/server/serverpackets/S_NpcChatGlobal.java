package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;

public class S_NpcChatGlobal extends ServerBasePacket {
    private byte[] _byte = null;

    public S_NpcChatGlobal(L1NpcInstance npc, String chat) {
        buildPacket(npc, chat);
    }

    private void buildPacket(L1NpcInstance npc, String chat) {
        writeC(133);
        writeC(3);
        writeD(npc.getId());
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
