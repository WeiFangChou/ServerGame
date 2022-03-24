package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.FaceToFace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Propose extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Propose.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            int c = readC();
            L1PcInstance pc = client.getActiveChar();
            if (c == 0) {
                if (!pc.isGhost()) {
                    L1PcInstance target = FaceToFace.faceToFace(pc);
                    if (target != null) {
                        if (pc.getPartnerId() != 0) {
                            pc.sendPackets(new S_ServerMessage(657));
                            over();
                            return;
                        } else if (target.getPartnerId() != 0) {
                            pc.sendPackets(new S_ServerMessage(658));
                            over();
                            return;
                        } else if (pc.get_sex() == target.get_sex()) {
                            pc.sendPackets(new S_ServerMessage(661));
                            over();
                            return;
                        } else if (pc.getX() >= 33974 && pc.getX() <= 33976 && pc.getY() >= 33362 && pc.getY() <= 33365 && pc.getMapId() == 4 && target.getX() >= 33974 && target.getX() <= 33976 && target.getY() >= 33362 && target.getY() <= 33365 && target.getMapId() == 4) {
                            target.setTempID(pc.getId());
                            target.sendPackets(new S_Message_YN(654, pc.getName()));
                        }
                    }
                } else {
                    return;
                }
            } else if (c == 1) {
                if (pc.getPartnerId() == 0) {
                    pc.sendPackets(new S_ServerMessage(662));
                    over();
                    return;
                }
                pc.sendPackets(new S_Message_YN(653));
            }
            over();
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
