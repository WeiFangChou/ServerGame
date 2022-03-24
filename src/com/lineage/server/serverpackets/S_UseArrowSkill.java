package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Character;

public class S_UseArrowSkill extends ServerBasePacket {
    private byte[] _byte = null;

    public S_UseArrowSkill(L1Character cha, int targetobj, int spellgfx, int x, int y, int dmg) {
        int aid = 1;
        switch (cha.getTempCharGfx()) {
            case 2716:
                aid = 19;
                break;
            case 3860:
                aid = 21;
                break;
        }
        writeC(OpcodesServer.S_OPCODE_ATTACKPACKET);
        writeC(aid);
        writeD(cha.getId());
        writeD(targetobj);
        if (dmg > 0) {
            writeH(10);
        } else {
            writeH(0);
        }
        writeC(cha.getHeading());
        writeD(338);
        writeH(spellgfx);
        writeC(127);
        writeH(cha.getX());
        writeH(cha.getY());
        writeH(x);
        writeH(y);
        writeD(0);
        writeC(0);
    }

    public S_UseArrowSkill(L1Character cha, int spellgfx, int x, int y) {
        int aid = cha.getTempCharGfx() == 3860 ? 21 : 1;
        writeC(OpcodesServer.S_OPCODE_ATTACKPACKET);
        writeC(aid);
        writeD(cha.getId());
        writeD(0);
        writeH(0);
        writeC(cha.getHeading());
        writeD(338);
        writeH(spellgfx);
        writeC(127);
        writeH(cha.getX());
        writeH(cha.getY());
        writeH(x);
        writeH(y);
        writeD(0);
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
