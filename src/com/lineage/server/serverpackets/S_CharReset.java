package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_CharReset extends ServerBasePacket {
    public static final int RING_RUNE_SLOT = 67;
    public static final int SUBTYPE_RING = 1;
    private byte[] _byte = null;

    public S_CharReset(int type, int subType, int value) {
        writeC(33);
        writeC(type);
        switch (type) {
            case 67:
                writeD(subType);
                if (subType == 1) {
                    if (value == 2) {
                        value = 15;
                    } else if (value == 1) {
                        value = 7;
                    } else if (value == 0) {
                        value = 3;
                    }
                    writeC(value);
                }
                writeD(0);
                writeD(0);
                writeD(0);
                writeD(0);
                writeD(0);
                writeD(0);
                writeH(0);
                return;
            default:
                return;
        }
    }

    public S_CharReset(L1PcInstance pc, int lv, int hp, int mp, int ac, int str, int intel, int wis, int dex, int con, int cha) {
        writeC(33);
        writeC(2);
        writeC(lv);
        writeC(pc.getTempMaxLevel());
        writeH(hp);
        writeH(mp);
        writeH(ac);
        writeC(str);
        writeC(intel);
        writeC(wis);
        writeC(dex);
        writeC(con);
        writeC(cha);
    }

    public S_CharReset(int point) {
        writeC(33);
        writeC(3);
        writeC(point);
    }

    public S_CharReset(L1PcInstance pc) {
        writeC(33);
        writeC(1);
        if (pc.isCrown()) {
            writeH(14);
            writeH(2);
        } else if (pc.isKnight()) {
            writeH(16);
            writeH(1);
        } else if (pc.isElf()) {
            writeH(15);
            writeH(4);
        } else if (pc.isWizard()) {
            writeH(12);
            writeH(6);
        } else if (pc.isDarkelf()) {
            writeH(12);
            writeH(3);
        } else if (pc.isDragonKnight()) {
            writeH(15);
            writeH(4);
        } else if (pc.isIllusionist()) {
            writeH(15);
            writeH(4);
        }
        writeC(10);
        writeC(pc.getTempMaxLevel());
    }

    public S_CharReset(int pcObjId, int emblemId) {
        writeC(33);
        writeC(60);
        writeD(pcObjId);
        writeD(emblemId);
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
