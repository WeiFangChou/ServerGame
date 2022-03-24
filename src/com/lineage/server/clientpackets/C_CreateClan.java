package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ClanUpdate;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.WorldClan;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_CreateClan extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_CreateClan.class);

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
                    String s = readS();
                    if (s.length() > 16) {
                        pc.sendPackets(new S_ServerMessage(98));
                        over();
                        return;
                    }
                    if (!pc.isCrown()) {
                        pc.sendPackets(new S_ServerMessage(85));
                    } else if (pc.getClanid() == 0) {
                        for (L1Clan clan : WorldClan.get().getAllClans()) {
                            if (clan.getClanName().toLowerCase().equals(s.toLowerCase())) {
                                pc.sendPackets(new S_ServerMessage(99));
                                over();
                                return;
                            }
                        }
                        if (pc.getInventory().consumeItem(L1ItemId.ADENA, 30000)) {
                            L1Clan clan2 = ClanReading.get().createClan(pc, s);
                            if (clan2 != null) {
                                pc.sendPackets(new S_ServerMessage(84, s));
                                pc.sendPackets(new S_ClanUpdate(pc.getId(), pc.getClanname(), pc.getClanRank()));
                                CharObjidTable.get().addClan(clan2.getClanId(), clan2.getClanName());
                            }
                        } else {
                            pc.sendPackets(new S_ServerMessage((int) L1SkillId.SHOCK_SKIN));
                        }
                    } else {
                        pc.sendPackets(new S_ServerMessage(86));
                    }
                    pc.sendPackets(new S_CloseList(pc.getId()));
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
