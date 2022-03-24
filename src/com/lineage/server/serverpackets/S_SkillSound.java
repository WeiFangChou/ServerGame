package com.lineage.server.serverpackets;

public class S_SkillSound extends ServerBasePacket {
    private byte[] _byte = null;

    public S_SkillSound(int objid, int gfxid) {
        buildPacket(objid, gfxid);
    }

    private void buildPacket(int objid, int gfxid) {
        writeC(OpcodesServer.S_OPCODE_SKILLSOUNDGFX);
        writeD(objid);
        writeH(gfxid);
    }

    public S_SkillSound(int objid, int gfxid, int time) {
        writeC(OpcodesServer.S_OPCODE_SKILLSOUNDGFX);
        writeD(objid);
        writeH(gfxid);
        writeH(time);
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
