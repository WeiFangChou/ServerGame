package com.lineage.server.serverpackets;

public class S_SkillIconBlessOfEva extends ServerBasePacket {
    public S_SkillIconBlessOfEva(int objectId, int time) {
        writeC(12);
        writeD(objectId);
        writeH(time);
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        return getBytes();
    }
}
