package com.lineage.server.serverpackets;

import com.lineage.server.datatables.SceneryTable;
import com.lineage.server.model.Instance.L1FieldObjectInstance;

public class S_NPCPack_F extends ServerBasePacket {
    private byte[] _byte = null;

    public S_NPCPack_F(L1FieldObjectInstance npc) {
        writeC(3);
        writeH(npc.getX());
        writeH(npc.getY());
        writeD(npc.getId());
        writeH(npc.getGfxId());
        writeC(npc.getStatus());
        writeC(npc.getHeading());
        writeC(npc.getChaLightSize());
        writeC(npc.getMoveSpeed());
        writeD((int) npc.getExp());
        writeH(npc.getTempLawful());
        writeS(npc.getNameId());
        String sceneryHtml = SceneryTable.get().get_sceneryHtml(npc.getId());
        if (sceneryHtml != null) {
            writeS(sceneryHtml);
        } else {
            writeS(null);
        }
        writeC(0);
        writeD(0);
        writeS(null);
        writeS(null);
        writeC(0);
        writeC(255);
        writeC(0);
        writeC(0);
        writeC(0);
        writeC(255);
        writeC(255);
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
