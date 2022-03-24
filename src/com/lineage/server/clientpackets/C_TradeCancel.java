package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.L1Trade;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_TradeCancel extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_TradeCancel.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            new L1Trade().tradeCancel(client.getActiveChar());
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
