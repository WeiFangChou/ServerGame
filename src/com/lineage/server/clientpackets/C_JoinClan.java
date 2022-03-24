package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.FaceToFace;
import com.lineage.server.world.WorldClan;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_JoinClan extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_JoinClan.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGhost()) {
                L1PcInstance target = FaceToFace.faceToFace(pc);
                if (target != null) {
                    joinClan(pc, target);
                }
                over();
            }
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    private void joinClan(L1PcInstance pc, L1PcInstance target) {
        if (!target.isCrown()) {
            pc.sendPackets(new S_ServerMessage(92, target.getName()));
            return;
        }
        int clan_id = target.getClanid();
        String clan_name = target.getClanname();
        if (clan_id == 0) {
            pc.sendPackets(new S_ServerMessage(90, target.getName()));
            return;
        }
        L1Clan clan = WorldClan.get().getClan(clan_name);
        if (clan == null) {
            return;
        }
        if (target.getId() != clan.getLeaderId()) {
            pc.sendPackets(new S_ServerMessage(92, target.getName()));
            return;
        }
        if (pc.getClanid() != 0) {
            if (pc.isCrown()) {
                L1Clan player_clan = WorldClan.get().getClan(pc.getClanname());
                if (player_clan == null) {
                    return;
                }
                if (pc.getId() != player_clan.getLeaderId()) {
                    pc.sendPackets(new S_ServerMessage(89));
                    return;
                } else if (!(player_clan.getCastleId() == 0 && player_clan.getHouseId() == 0)) {
                    pc.sendPackets(new S_ServerMessage(665));
                    return;
                }
            } else {
                pc.sendPackets(new S_ServerMessage(89));
                return;
            }
        }
        target.setTempID(pc.getId());
        target.sendPackets(new S_Message_YN(97, pc.getName()));
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
