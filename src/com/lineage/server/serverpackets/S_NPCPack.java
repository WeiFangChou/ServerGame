package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;

public class S_NPCPack extends ServerBasePacket {
    private static final int STATUS_PC = 4;
    private static final int STATUS_POISON = 1;
    private byte[] _byte = null;

    public S_NPCPack(L1NpcInstance npc) {
        writeC(3);
        writeH(npc.getX());
        writeH(npc.getY());
        writeD(npc.getId());
        if (npc.getTempCharGfx() == 0) {
            writeH(npc.getGfxId());
        } else {
            writeH(npc.getTempCharGfx());
        }
        if (!npc.getNpcTemplate().is_doppel() || npc.getGfxId() == 31) {
            writeC(npc.getStatus());
        } else {
            writeC(4);
        }
        writeC(npc.getHeading());
        writeC(npc.getChaLightSize());
        writeC(npc.getMoveSpeed());
        writeD((int) npc.getExp());
        writeH(npc.getTempLawful());
        writeS(npc.getNameId());
        writeS(npc.getTitle());
        int status = 0;
        if (npc.getPoison() != null && npc.getPoison().getEffectId() == 1) {
            status = 1;
        }
        if (npc.getNpcTemplate().is_doppel() && npc.getNpcTemplate().get_npcId() != 81069) {
            status |= 4;
        }
        writeC(npc.getNpcTemplate().get_npcId() == 90024 ? status | 1 : status);
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
