package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.L1Character;

public class S_MoveNpcPacket extends ServerBasePacket {
    private byte[] _byte = null;

    public S_MoveNpcPacket(L1MonsterInstance npc, int x, int y, int heading) {
        writeC(122);
        writeD(npc.getId());
        writeH(x);
        writeH(y);
        writeC(heading);
        writeC(128);
    }

    public S_MoveNpcPacket(L1Character cha) {
        writeC(122);
        writeD(cha.getId());
        writeH(cha.getX());
        writeH(cha.getY());
        writeC(cha.getHeading());
        writeC(128);
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
