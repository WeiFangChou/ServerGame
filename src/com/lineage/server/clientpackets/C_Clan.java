package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.ClanEmblemReading;
import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.serverpackets.S_Emblem;
import com.lineage.server.templates.L1EmblemIcon;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Clan extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Clan.class);

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
                    L1Clan clan = ClanReading.get().getTemplate(readD());
                    if (clan == null) {
                        over();
                        return;
                    }
                    L1EmblemIcon emblemIcon = ClanEmblemReading.get().get(clan.getClanId());
                    if (emblemIcon != null) {
                        pc.sendPackets(new S_Emblem(emblemIcon));
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
