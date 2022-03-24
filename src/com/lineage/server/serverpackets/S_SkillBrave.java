package com.lineage.server.serverpackets;

public class S_SkillBrave extends ServerBasePacket {
    public S_SkillBrave(int objid, int mode, int time) {
        writeC(200);
        writeD(objid);
        writeC(mode);
        writeH(time);
        writeH(0);
    }

    public S_SkillBrave(int objid, int mode) {
        writeC(200);
        writeD(objid);
        writeC(mode);
        writeH(300);
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        return getBytes();
    }
}
