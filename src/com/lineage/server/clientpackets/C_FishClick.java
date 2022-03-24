package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.timecontroller.p002pc.PcFishingTimer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_FishClick extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_FishClick.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            PcFishingTimer.finishFishing(client.getActiveChar(), false);
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
