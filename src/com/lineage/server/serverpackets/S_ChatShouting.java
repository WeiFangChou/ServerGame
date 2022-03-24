package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class S_ChatShouting extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ChatShouting(L1PcInstance pc, String chat) {
        buildPacket(pc, chat);
    }

    private void buildPacket(L1PcInstance pc, String chat) {
        writeC(76);
        writeC(2);
        writeD(pc.isInvisble() ? 0 : pc.getId());
        writeS("<" + pc.getName() + "> " + chat);
        writeH(pc.getX());
        writeH(pc.getY());
    }

    public S_ChatShouting(L1NpcInstance npc, String chat) {
        writeC(76);
        writeC(2);
        writeD(npc.isInvisble() ? 0 : npc.getId());
        writeS("<" + npc.getNameId() + "> " + chat);
        writeH(npc.getX());
        writeH(npc.getY());
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
