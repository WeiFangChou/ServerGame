package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.L1Character;

public class S_AttackPacketNpc extends ServerBasePacket {
    private byte[] _byte = null;

    public S_AttackPacketNpc(L1NpcInstance npc, L1Character target, int type, int dmg) {
        writeC(OpcodesServer.S_OPCODE_ATTACKPACKET);
        writeC(type);
        writeD(npc.getId());
        writeD(target.getId());
        if (dmg > 0) {
            writeH(10);
        } else {
            writeH(0);
        }
        writeC(npc.getHeading());
        writeH(0);
        writeH(0);
        writeC(0);
    }

    public S_AttackPacketNpc(L1NpcInstance npc, int targetid, int type, int dmg) {
        writeC(OpcodesServer.S_OPCODE_ATTACKPACKET);
        writeC(type);
        writeD(npc.getId());
        writeD(targetid);
        if (dmg > 0) {
            writeH(10);
        } else {
            writeH(0);
        }
        writeC(npc.getHeading());
        writeH(0);
        writeH(0);
        writeC(0);
    }

    public S_AttackPacketNpc(L1NpcInstance npc, L1Character target, int type) {
        writeC(OpcodesServer.S_OPCODE_ATTACKPACKET);
        writeC(type);
        writeD(npc.getId());
        writeD(target.getId());
        writeH(0);
        writeC(npc.getHeading());
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
