package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_OtherCharPacks extends ServerBasePacket {
    private static final int STATUS_BRAVE = 16;
    private static final int STATUS_ELFBRAVE = 32;
    private static final int STATUS_FASTMOVABLE = 64;
    private static final int STATUS_INVISIBLE = 2;
    private static final int STATUS_PC = 4;
    private static final int STATUS_POISON = 1;
    private byte[] _byte = null;

    public S_OtherCharPacks(L1PcInstance pc) {
        int status = 4;
        if (pc.getPoison() != null && pc.getPoison().getEffectId() == 1) {
            status = 4 | 1;
        }
        status = pc.isInvisble() ? status | 2 : status;
        status = pc.isBrave() ? status | 16 : status;
        status = pc.isElfBrave() ? status | 16 | 32 : status;
        status = pc.isFastMovable() ? status | 64 : status;
        writeC(3);
        writeH(pc.getX());
        writeH(pc.getY());
        writeD(pc.getId());
        if (pc.isDead()) {
            writeH(pc.getTempCharGfxAtDead());
        } else {
            writeH(pc.getTempCharGfx());
        }
        if (pc.isDead()) {
            writeC(pc.getStatus());
        } else {
            writeC(pc.getCurrentWeapon());
        }
        writeC(pc.getHeading());
        writeC(pc.getChaLightSize());
        writeC(pc.getMoveSpeed());
        writeD(0);
        writeH(pc.getLawful());
        StringBuilder stringBuilder = new StringBuilder();
        if (pc.get_other().get_color() != 0) {
            stringBuilder.append(pc.get_other().color());
        }
        stringBuilder.append(pc.getName());
        writeS(stringBuilder.toString());
        writeS(pc.getTitle());
        writeC(status);
        writeD(pc.getClanid());
        writeS(pc.getClanname());
        writeS(null);
        writeC(pc.getClanRank() << 4);
        writeC(255);
        if (pc.hasSkillEffect(998)) {
            writeC(8);
        } else {
            writeC(0);
        }
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
