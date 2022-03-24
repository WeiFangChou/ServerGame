package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;

public class S_SPMR extends ServerBasePacket {
    private byte[] _byte = null;

    public S_SPMR(L1PcInstance pc, boolean isLogin) {
        writeC(174);
        writeC(pc.getTrueSp());
        writeC(pc.getBaseMr());
    }

    public S_SPMR(L1PcInstance pc) {
        buildPacket(pc);
    }

    private void buildPacket(L1PcInstance pc) {
        writeC(174);
        int sp = pc.getSp() - pc.getTrueSp();
        switch (pc.getInt()) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
                break;
            case 28:
            case 29:
            case 30:
                sp++;
                break;
            case 31:
            case 32:
            case 33:
                sp += 2;
                break;
            case 34:
            case 35:
                sp += 3;
                break;
            case 36:
            case 37:
            case 38:
            case 39:
                sp += 3;
                break;
            case 40:
                sp += 4;
                break;
            case 41:
                sp += 5;
                break;
            case 42:
            case 43:
                sp += 6;
                break;
            case 44:
                sp += 7;
                break;
            case 45:
                sp += 8;
                break;
            case 46:
                sp += 9;
                break;
            case 47:
                sp += 10;
                break;
            case 48:
                sp += 11;
                break;
            case 49:
                sp += 12;
                break;
            case 50:
                sp += 12;
                break;
            case 51:
                sp += 13;
                break;
            case 52:
                sp += 14;
                break;
            case 53:
                sp += 15;
                break;
            case 54:
                sp += 16;
                break;
            case 55:
                sp += 17;
                break;
            case 56:
                sp += 18;
                break;
            case 57:
                sp += 19;
                break;
            case 58:
                sp += 20;
                break;
            case 59:
                sp += 21;
                break;
            case 60:
                sp += 22;
                break;
            case 61:
                sp += 23;
                break;
            case 62:
                sp += 24;
                break;
            case 63:
                sp += 25;
                break;
            case 64:
                sp += 26;
                break;
            case 65:
                sp += 27;
                break;
            case 66:
                sp += 28;
                break;
            case 67:
                sp += 29;
                break;
            case 68:
                sp += 30;
                break;
            case 69:
                sp += 31;
                break;
            case 70:
                sp += 32;
                break;
            case 71:
                sp += 33;
                break;
            case 72:
                sp += 34;
                break;
            case 73:
                sp += 35;
                break;
            case 74:
                sp += 36;
                break;
            case 75:
                sp += 37;
                break;
            case 76:
                sp += 38;
                break;
            case 77:
                sp += 39;
                break;
            default:
                sp += 40;
                break;
        }
        if (pc.hasSkillEffect(L1SkillId.STATUS_WISDOM_POTION)) {
            sp -= 2;
        }
        int mr = pc.getTrueMr() - pc.getBaseMr();
        switch (pc.guardianEncounter()) {
            case 0:
                mr += 3;
                break;
            case 1:
                mr += 6;
                break;
            case 2:
                mr += 9;
                break;
            case 3:
                sp++;
                break;
            case 4:
                sp += 2;
                break;
            case 5:
                sp += 3;
                break;
        }
        writeC(sp);
        writeC(mr);
    }

    public S_SPMR() {
        writeC(174);
        writeC(50);
        writeC(100);
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
