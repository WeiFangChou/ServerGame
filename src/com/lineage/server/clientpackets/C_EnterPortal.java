package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.DungeonTable;
import com.lineage.server.model.Instance.L1PcInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_EnterPortal extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_EnterPortal.class);

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
                    int locx = readH();
                    int locy = readH();
                    if (pc.isTeleport()) {
                        over();
                        return;
                    }
                    DungeonTable.get().dg(locx, locy, pc.getMap().getId(), pc);
                    over();
                }
            }
        } catch (Exception ignored) {
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            over();
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
