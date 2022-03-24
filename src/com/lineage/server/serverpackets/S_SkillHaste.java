package com.lineage.server.serverpackets;

public class S_SkillHaste extends ServerBasePacket {
    private byte[] _byte = null;

    public S_SkillHaste(int objid, int mode, int time) {
        writeC(149);
        writeD(objid);
        writeC(mode);
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
