package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.skill.TargetStatus;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class S_RangeSkill extends ServerBasePacket {
    public static final int TYPE_DIR = 8;
    public static final int TYPE_NODIR = 0;
    private static AtomicInteger _sequentialNumber = new AtomicInteger(9000000);
    private byte[] _byte = null;

    public S_RangeSkill(L1Character cha, ArrayList<TargetStatus> targetList, int spellgfx, int actionId, int type) {
        writeC(16);
        writeC(actionId);
        writeD(cha.getId());
        writeH(cha.getX());
        writeH(cha.getY());
        switch (type) {
            case 0:
                writeC(cha.getHeading());
                break;
            case 8:
                cha.setHeading(calcheading(cha.getX(), cha.getY(), targetList.get(0).getTarget().getX(), targetList.get(0).getTarget().getY()));
                writeC(cha.getHeading());
                break;
        }
        writeD(_sequentialNumber.incrementAndGet());
        writeH(spellgfx);
        writeC(type);
        writeH(0);
        writeH(targetList.size());
        Iterator<TargetStatus> it = targetList.iterator();
        while (it.hasNext()) {
            TargetStatus target = it.next();
            writeD(target.getTarget().getId());
            if (target.isCalc()) {
                writeH(32);
            } else {
                writeH(0);
            }
        }
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
