package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.serverpackets.S_Pledge;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.WorldClan;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Pledge extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Pledge.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            L1PcInstance pc = client.getActiveChar();
            if (pc.getClanid() > 0) {
                L1Clan clan = WorldClan.get().getClan(pc.getClanname());
                if (!pc.isCrown() || pc.getId() != clan.getLeaderId()) {
                    pc.sendPackets(new S_Pledge(pc.getId(), clan.getClanName(), clan.getOnlineMembersFP()));
                } else {
                    pc.sendPackets(new S_Pledge(pc.getId(), clan.getClanName(), clan.getOnlineMembersFPWithRank(), clan.getAllMembersFPWithRank()));
                }
            } else {
                pc.sendPackets(new S_ServerMessage(1064));
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
