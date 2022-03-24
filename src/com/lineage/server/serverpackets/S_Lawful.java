package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_Lawful extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Lawful(L1PcInstance pc) {
        buildPacket(pc);
    }

    private void buildPacket(L1PcInstance pc) {
        writeC(OpcodesServer.S_OPCODE_LAWFUL);
        writeD(pc.getId());
        writeH(pc.getLawful());
    }

    public S_Lawful(int objid) {
        writeC(OpcodesServer.S_OPCODE_LAWFUL);
        writeD(objid);
        writeH(-32768);
        writeH(-32768);
        writeH(-32768);
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
