package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;

public class S_Chat extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Chat(L1PcInstance pc, String chat) {
        buildPacket(pc, chat);
    }

    private void buildPacket(L1PcInstance pc, String chat) {
        int i = 0;
        writeC(76);
        writeC(0);
        if (!pc.isInvisble()) {
            i = pc.getId();
        }
        writeD(i);
        writeS(String.valueOf(pc.getName()) + ": " + chat);
    }

    public S_Chat(L1NpcInstance npc, String chat) {
        int i = 0;
        writeC(76);
        writeC(0);
        writeD(!npc.isInvisble() ? npc.getId() : i);
        writeS(String.valueOf(npc.getNameId()) + ": " + chat);
    }

    public S_Chat(L1Object object, String chat, int x) {
        writeC(76);
        writeC(0);
        writeD(object.getId());
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
