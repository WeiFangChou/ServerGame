package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Trade;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_TradeOK extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_TradeOK.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            L1PcInstance player = client.getActiveChar();
            L1PcInstance trading_partner = (L1PcInstance) World.get().findObject(player.getTradeID());
            if (trading_partner != null) {
                player.setTradeOk(true);
                if (player.getTradeOk() && trading_partner.getTradeOk()) {
                    if (player.getInventory().getSize() >= 160 || trading_partner.getInventory().getSize() >= 160) {
                        player.sendPackets(new S_ServerMessage(263));
                        trading_partner.sendPackets(new S_ServerMessage(263));
                        new L1Trade().tradeCancel(player);
                    } else {
                        new L1Trade().tradeOK(player);
                    }
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
