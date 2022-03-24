package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.FaceToFace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Fight extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Fight.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGhost()) {
                L1PcInstance target = FaceToFace.faceToFace(pc);
                if (target != null && !target.isParalyzed()) {
                    if (pc.getFightId() != 0) {
                        pc.sendPackets(new S_ServerMessage(633));
                        over();
                        return;
                    } else if (target.getFightId() != 0) {
                        target.sendPackets(new S_ServerMessage(634));
                        over();
                        return;
                    } else {
                        pc.setFightId(target.getId());
                        target.setFightId(pc.getId());
                        target.sendPackets(new S_Message_YN(630, pc.getName()));
                    }
                }
                over();
            }
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
