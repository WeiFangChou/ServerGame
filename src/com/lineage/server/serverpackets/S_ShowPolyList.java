package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Character;

public class S_ShowPolyList extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ShowPolyList(int objid) {
        writeC(OpcodesServer.S_OPCODE_SHOWHTML);
        writeD(objid);
        writeS("monlist");
    }

    public S_ShowPolyList(L1Character target) {
        writeC(OpcodesServer.S_OPCODE_SHOWHTML);
        writeD(target.getId());
        writeS("monlist");
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
