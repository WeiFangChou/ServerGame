package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.CastleReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.templates.L1Castle;
import com.lineage.server.types.ULong32;
import com.lineage.server.world.WorldClan;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Deposit extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Deposit.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        L1Clan clan;
        int castle_id;
        try {
            read(decrypt);
            int objid = readD();
            long count = (long) readD();
            if (count > ULong32.MAX_UNSIGNEDLONG_VALUE) {
                count = ULong32.MAX_UNSIGNEDLONG_VALUE;
            }
            long count2 = Math.max(0L, count);
            L1PcInstance player = client.getActiveChar();
            if (!(objid != player.getId() || (clan = WorldClan.get().getClan(player.getClanname())) == null || (castle_id = clan.getCastleId()) == 0)) {
                L1Castle l1castle = CastleReading.get().getCastleTable(castle_id);
                synchronized (l1castle) {
                    long money = l1castle.getPublicMoney();
                    if (player.getInventory().consumeItem(L1ItemId.ADENA, count2)) {
                        l1castle.setPublicMoney(money + count2);
                        CastleReading.get().updateCastle(l1castle);
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
