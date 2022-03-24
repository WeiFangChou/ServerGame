package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;

public class S_NewMaster extends ServerBasePacket {
    private byte[] _byte = null;

    public S_NewMaster(String name, L1NpcInstance npc) {
        buildPacket(name, npc);
    }

    private void buildPacket(String name, L1NpcInstance npc) {
        writeC(13);
        writeD(npc.getId());
        writeS(name);
    }

    public S_NewMaster(L1NpcInstance npc) {
        writeC(13);
        writeD(npc.getId());
        writeS("");
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
