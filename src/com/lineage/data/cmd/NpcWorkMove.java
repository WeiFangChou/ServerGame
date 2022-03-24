package com.lineage.data.cmd;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.serverpackets.S_MoveCharPacket;
import com.lineage.server.types.Point;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NpcWorkMove {
    private static final byte[] HEADING_TABLE_X;
    private static final byte[] HEADING_TABLE_Y;
    private static final Log _log = LogFactory.getLog(NpcWorkMove.class);
    private L1NpcInstance _npc;

    static {
        byte[] bArr = new byte[8];
        bArr[1] = 1;
        bArr[2] = 1;
        bArr[3] = 1;
        bArr[5] = -1;
        bArr[6] = -1;
        bArr[7] = -1;
        HEADING_TABLE_X = bArr;
        byte[] bArr2 = new byte[8];
        bArr2[0] = -1;
        bArr2[1] = -1;
        bArr2[3] = 1;
        bArr2[4] = 1;
        bArr2[5] = 1;
        bArr2[7] = -1;
        HEADING_TABLE_Y = bArr2;
    }

    public NpcWorkMove(L1NpcInstance npc) {
        this._npc = npc;
    }

    public boolean actionStart(Point point) {
        try {
            setDirectionMove(this._npc.targetDirection(point.getX(), point.getY()));
            if (this._npc.getLocation().getTileLineDistance(point) == 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }

    }

    private void setDirectionMove(int heading) {
        int locx = this._npc.getX();
        int locy = this._npc.getY();
        int locx2 = locx + HEADING_TABLE_X[heading];
        int locy2 = locy + HEADING_TABLE_Y[heading];
        this._npc.setHeading(heading);
        this._npc.setX(locx2);
        this._npc.setY(locy2);
        this._npc.broadcastPacketAll(new S_MoveCharPacket(this._npc));
    }
}
