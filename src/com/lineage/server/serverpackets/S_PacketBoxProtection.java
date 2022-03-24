package com.lineage.server.serverpackets;

import com.lineage.server.model.skill.L1SkillId;

public class S_PacketBoxProtection extends ServerBasePacket {
    public static final int ENCOUNTER = 6;
    public static final int EVIL_L1 = 3;
    public static final int EVIL_L2 = 4;
    public static final int EVIL_L3 = 5;
    public static final int JUSTICE_L1 = 0;
    public static final int JUSTICE_L2 = 1;
    public static final int JUSTICE_L3 = 2;
    private byte[] _byte = null;

    public S_PacketBoxProtection(int model, int type) {
        writeC(40);
        writeC(L1SkillId.GLOWING_AURA);
        writeD(model);
        writeD(type);
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
