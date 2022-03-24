package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;

public class S_AttackPacketPc extends ServerBasePacket {
    private byte[] _byte = null;

    public S_AttackPacketPc(L1PcInstance pc, L1Character target, int type, int dmg) {
        writeC(OpcodesServer.S_OPCODE_ATTACKPACKET);
        writeC(1);
        writeD(pc.getId());
        writeD(target.getId());
        if (dmg > 0) {
            writeH(10);
        } else {
            writeH(0);
        }
        writeC(pc.getHeading());
        writeH(0);
        writeH(0);
        writeC(type);
    }

    public S_AttackPacketPc(L1PcInstance pc, L1Character target) {
        writeC(OpcodesServer.S_OPCODE_ATTACKPACKET);
        writeC(1);
        writeD(pc.getId());
        writeD(target.getId());
        writeH(0);
        writeC(pc.getHeading());
        writeH(0);
        writeH(0);
        writeC(0);
    }

    public S_AttackPacketPc(L1PcInstance pc) {
        writeC(OpcodesServer.S_OPCODE_ATTACKPACKET);
        writeC(1);
        writeD(pc.getId());
        writeD(0);
        writeH(0);
        writeC(pc.getHeading());
        writeH(0);
        writeH(0);
        writeC(0);
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
