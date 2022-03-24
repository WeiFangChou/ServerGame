package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_ExtraCommand extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_ExtraCommand.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        int gfxId;
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGhost()) {
                if (pc.isDead()) {
                    over();
                } else if (pc.isTeleport()) {
                    over();
                } else if (pc.isInvisble()) {
                    over();
                } else {
                    int actionId = readC();
                    if (!pc.hasSkillEffect(67) || (gfxId = pc.getTempCharGfx()) == 6080 || gfxId == 6094) {
                        pc.broadcastPacketX10(new S_DoActionGFX(pc.getId(), actionId));
                        pc.set_actionId(actionId);
                        over();
                        return;
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
