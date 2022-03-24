package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;

public class S_ChangeShape extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ChangeShape(L1Character obj, int polyId) {
        buildPacket(obj, polyId, false);
    }

    public S_ChangeShape(L1Character obj, int polyId, boolean weaponTakeoff) {
        buildPacket(obj, polyId, weaponTakeoff);
    }

    private void buildPacket(L1Character obj, int polyId, boolean weaponTakeoff) {
        writeC(164);
        writeD(obj.getId());
        writeH(polyId);
        writeH(weaponTakeoff ? 0 : 29);
    }

    public S_ChangeShape(L1PcInstance pc, L1NpcInstance npc, int polyId) {
        writeC(204);
        writeD(npc.getId());
        writeD(pc.getId());
        writeH(polyId);
        writeS(pc.getName());
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = getBytes();
        }
        return this._byte;
    }
}
