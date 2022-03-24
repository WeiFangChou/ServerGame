package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class C_ToPc extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_ToPc.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) throws Exception {
        try {
            read(decrypt);
            String s = readS();
            String s2 = s.substring(2, s.length());
            L1PcInstance target = World.get().getPlayer(s2);
            if (target != null) {
                L1PcInstance pc = client.getActiveChar();
                if (pc != null && pc.isGm()) {
                    L1Teleport.teleport(pc, target.getX(), target.getY(), target.getMapId(), 5, false);
                    pc.set_showId(target.get_showId());
                    pc.sendPackets(new S_ServerMessage(166, "移動座標至指定人物身邊: " + s2));
                }
                over();
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            over();
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
