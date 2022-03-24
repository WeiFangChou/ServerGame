package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import java.util.Random;

public class S_OwnCharPack extends ServerBasePacket {
    private static final int STATUS_BRAVE = 16;
    private static final int STATUS_ELFBRAVE = 32;
    private static final int STATUS_FASTMOVABLE = 64;
    private static final int STATUS_GHOST = 128;
    private static final int STATUS_INVISIBLE = 2;
    private static final int STATUS_PC = 4;
    public static final Random _random = new Random();
    private byte[] _byte = null;

    public S_OwnCharPack(L1PcInstance pc) {
        buildPacket(pc);
    }

    private void buildPacket(L1PcInstance pc) {
        int status = 4;
        if (pc.isInvisble() || pc.isGmInvis()) {
            status = 4 | 2;
        }
        if (pc.isBrave()) {
            status |= 16;
        }
        if (pc.isElfBrave()) {
            status = status | 16 | 32;
        }
        if (pc.isFastMovable()) {
            status |= 64;
        }
        if (pc.isGhost()) {
            status |= 128;
        }
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
        writeC(pc.getOwnLightSize());
        writeC(pc.getMoveSpeed());
        writeD((int) pc.getExp());
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
        writeC(pc.getClanRank() > 0 ? pc.getClanRank() << 4 : L1SkillId.ADDITIONAL_FIRE);
        if (pc.isInParty()) {
            writeC((pc.getCurrentHp() * 100) / pc.getMaxHp());
        } else {
            writeC(255);
        }
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
