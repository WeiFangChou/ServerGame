package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1SignboardInstance;

public class S_NPCPack_Signboard extends ServerBasePacket {
    private byte[] _byte = null;

    public S_NPCPack_Signboard(L1SignboardInstance signboard) {
        writeC(3);
        writeH(signboard.getX());
        writeH(signboard.getY());
        writeD(signboard.getId());
        writeH(signboard.getGfxId());
        writeC(0);
        writeC(getDirection(signboard.getHeading()));
        writeC(0);
        writeC(0);
        writeD(0);
        writeH(0);
        writeS(null);
        writeS(signboard.getName());
        writeC(0);
        writeD(0);
        writeS(null);
        writeS(null);
        writeC(0);
        writeC(255);
        writeC(0);
        writeC(0);
        writeC(0);
        writeC(255);
        writeC(255);
    }

    private int getDirection(int heading) {
        switch (heading) {
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
            default:
                return 0;
            case 6:
                return 4;
            case 7:
                return 5;
        }
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
