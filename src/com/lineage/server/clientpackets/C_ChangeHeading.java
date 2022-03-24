package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeHeading;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_ChangeHeading extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_ChangeHeading.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGhost()) {
                if (pc.isDead()) {
                    over();
                } else if (pc.isTeleport()) {
                    over();
                } else {
                    pc.setHeading(readC());
                    if (!pc.isGmInvis() && !pc.isGhost() && !pc.isInvisble()) {
                        pc.broadcastPacketAll(new S_ChangeHeading(pc));
                    }
                    over();
                }
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
