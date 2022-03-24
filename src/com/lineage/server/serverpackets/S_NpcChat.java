package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class S_NpcChat extends ServerBasePacket {
    private byte[] _byte = null;

    public S_NpcChat(L1NpcInstance npc, String chat) {
        writeC(133);
        writeC(0);
        writeD(npc.getId());
        writeS(String.valueOf(npc.getNameId()) + ": " + chat);
    }

    public S_NpcChat(L1NpcInstance npc, String chat, boolean name) {
        writeC(133);
        writeC(0);
        writeD(npc.getId());
        writeS(String.valueOf(name ? String.valueOf(npc.getNameId()) + ": " : "") + chat);
    }

    public S_NpcChat(L1PcInstance pc, String chat) {
        writeC(133);
        writeC(0);
        writeD(pc.getId());
        writeS(String.valueOf(pc.getName()) + ": " + chat);
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
