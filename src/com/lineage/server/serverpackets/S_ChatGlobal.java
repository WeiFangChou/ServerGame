package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class S_ChatGlobal extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ChatGlobal(L1PcInstance pc, String chat) {
        buildPacket(pc, chat);
    }

    private void buildPacket(L1PcInstance pc, String chat) {
        writeC(10);
        writeC(3);
        writeS(pc.isGm() ? "[******] " + chat : "[" + pc.getName() + "] " + chat);
    }

    public S_ChatGlobal(L1NpcInstance npc, String chat) {
        writeC(10);
        writeC(3);
        writeS("[" + npc.getNameId() + "] " + chat);
    }

    public S_ChatGlobal(String chat) {
        writeC(10);
        writeC(3);
        writeS(chat);
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
