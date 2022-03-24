package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.CastleReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.templates.L1Castle;
import com.lineage.server.world.WorldClan;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_TaxRate extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_TaxRate.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        L1Clan clan;
        int castle_id;
        try {
            read(decrypt);
            int i = readD();
            int j = readC();
            L1PcInstance player = client.getActiveChar();
            if (!(i != player.getId() || (clan = WorldClan.get().getClan(player.getClanname())) == null || (castle_id = clan.getCastleId()) == 0)) {
                L1Castle l1castle = CastleReading.get().getCastleTable(castle_id);
                if (j >= 10 && j <= 50) {
                    l1castle.setTaxRate(j);
                    CastleReading.get().updateCastle(l1castle);
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
