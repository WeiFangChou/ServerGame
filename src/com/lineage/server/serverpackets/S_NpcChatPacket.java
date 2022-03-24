package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class S_NpcChatPacket extends ServerBasePacket {
    private static final String S_NPC_CHAT_PACKET = "[S] S_NpcChatPacket";
    private byte[] _byte = null;

    public S_NpcChatPacket(L1NpcInstance npc, String chat, int type) {
        buildPacket(npc, chat, type);
    }

    public S_NpcChatPacket(L1NpcInstance npc, String chat) {
        buildPacket(npc, chat, 0);
    }

    public S_NpcChatPacket(L1PcInstance pc, String string) {
        buildPacket(pc, string, 0);
    }

    public S_NpcChatPacket(L1PcInstance pc, String string, int type) {
        buildPacket(pc, string, type);
    }

    private void buildPacket(L1NpcInstance npc, String chat, int type) {
        writeC(133);
        writeC(type);
        switch (type) {
            case 0:
                writeD(npc.getId());
                writeS(String.valueOf(npc.getName()) + ": " + chat);
                return;
            case 1:
            default:
                return;
            case 2:
                writeD(npc.getId());
                writeS("<" + npc.getName() + "> " + chat);
                return;
            case 3:
                writeD(npc.getId());
                writeS("[" + npc.getName() + "] " + chat);
                return;
        }
    }

    public void buildPacket(L1PcInstance pc, String chat, int type) {
        writeC(133);
        writeC(type);
        writeD(pc.getId());
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
        return S_NPC_CHAT_PACKET;
    }
}
