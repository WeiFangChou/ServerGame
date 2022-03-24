package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import java.util.concurrent.atomic.AtomicInteger;

public class S_UseAttackSkill extends ServerBasePacket {
    private static AtomicInteger _sequentialNumber = new AtomicInteger(4500000);
    private byte[] _byte = null;

    public S_UseAttackSkill(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId, boolean motion) {
        buildPacket(cha, targetobj, spellgfx, x, y, actionId, 0, motion);
    }

    public S_UseAttackSkill(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId, int dmg) {
        buildPacket(cha, targetobj, spellgfx, x, y, 18, dmg, true);
    }

    private void buildPacket(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId, int dmg, boolean withCastMotion) {
        int i;
        if ((cha instanceof L1PcInstance) && cha.hasSkillEffect(67) && actionId == 18) {
            int tempchargfx = cha.getTempCharGfx();
            if (tempchargfx == 5727 || tempchargfx == 5730) {
                actionId = 19;
            } else if (tempchargfx == 5733 || tempchargfx == 5736) {
                actionId = 1;
            }
        }
        if (cha.getTempCharGfx() == 4013) {
            actionId = 1;
        }
        int newheading = calcheading(cha.getX(), cha.getY(), x, y);
        cha.setHeading(newheading);
        writeC(OpcodesServer.S_OPCODE_ATTACKPACKET);
        writeC(actionId);
        if (withCastMotion) {
            i = cha.getId();
        } else {
            i = 0;
        }
        writeD(i);
        writeD(targetobj);
        if (dmg > 0) {
            writeH(10);
        } else {
            writeH(0);
        }
        writeC(newheading);
        writeD(_sequentialNumber.incrementAndGet());
        writeH(spellgfx);
        writeC(0);
        writeH(cha.getX());
        writeH(cha.getY());
        writeH(x);
        writeH(y);
        writeD(0);
        writeC(0);
    }

    private static int calcheading(int myx, int myy, int tx, int ty) {
        int newheading = 0;
        if (tx > myx && ty > myy) {
            newheading = 3;
        }
        if (tx < myx && ty < myy) {
            newheading = 7;
        }
        if (tx > myx && ty == myy) {
            newheading = 2;
        }
        if (tx < myx && ty == myy) {
            newheading = 6;
        }
        if (tx == myx && ty < myy) {
            newheading = 0;
        }
        if (tx == myx && ty > myy) {
            newheading = 4;
        }
        if (tx < myx && ty > myy) {
            newheading = 5;
        }
        if (tx <= myx || ty >= myy) {
            return newheading;
        }
        return 1;
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
