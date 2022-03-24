package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.L1Character;

public class S_PetCtrlMenu extends ServerBasePacket {
    private byte[] _byte = null;

    public S_PetCtrlMenu(L1Character cha, L1NpcInstance npc, boolean open) {
        writeC(33);
        writeC(12);
        if (open) {
            writeH(3);
            writeD(0);
            writeD(npc.getId());
            writeD(0);
            writeH(npc.getX());
            writeH(npc.getY());
            writeS(npc.getName());
            return;
        }
        writeH(0);
        writeD(1);
        writeD(npc.getId());
        writeS(null);
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
