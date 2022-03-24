package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.CastleReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.serverpackets.S_WarTime;
import com.lineage.server.world.WorldClan;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_ChangeWarTime extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Buddy.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        int castle_id;
        try {
            L1PcInstance pc = client.getActiveChar();
            L1Clan clan = WorldClan.get().getClan(pc.getClanname());
            if (!(clan == null || (castle_id = clan.getCastleId()) == 0)) {
                pc.sendPackets(new S_WarTime(CastleReading.get().getCastleTable(castle_id).getWarTime()));
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
