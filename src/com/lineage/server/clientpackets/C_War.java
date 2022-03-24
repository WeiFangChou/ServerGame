package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.echo.OpcodesClient;
import com.lineage.server.datatables.lock.ClanEmblemReading;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1War;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_HelpMessage;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_War;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldWar;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_War extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_War.class);

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance player = client.getActiveChar();
            String playerName = player.getName();
            String clanName = player.getClanname();
            int clanId = player.getClanid();
            if (!player.isCrown()) {
                player.sendPackets(new S_ServerMessage(478));
            } else if (clanId == 0) {
                player.sendPackets(new S_ServerMessage(272));
                over();
            } else {
                L1Clan clan = WorldClan.get().getClan(clanName);
                if (clan == null) {
                    over();
                } else if (player.getId() != clan.getLeaderId()) {
                    player.sendPackets(new S_ServerMessage(478));
                    over();
                } else {
                    int type = readC();
                    String tgname = readS();
                    if (clanName.toLowerCase().equals(tgname.toLowerCase())) {
                        over();
                        return;
                    }
                    L1Clan enemyClan = null;
                    String enemyClanName = null;
                    Iterator<L1Clan> iter = WorldClan.get().getAllClans().iterator();
                    while (true) {
                        if (iter.hasNext()) {
                            L1Clan checkClan = iter.next();
                            if (checkClan.getClanName().toLowerCase().equals(tgname.toLowerCase())) {
                                enemyClan = checkClan;
                                enemyClanName = checkClan.getClanName();
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (enemyClan == null) {
                        if (tgname.equals(" ")) {
                            war_castle(player, clan, null, type);
                        }
                        over();
                        return;
                    }
                    boolean inWar = false;
                    List<L1War> warList = WorldWar.get().getWarList();
                    if (enemyClan.getCastleId() == 0) {
                        Iterator<L1War> it = warList.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            } else if (it.next().checkClanInWar(clanName)) {
                                if (type == 0) {
                                    player.sendPackets(new S_ServerMessage(234));
                                    over();
                                    return;
                                }
                                inWar = true;
                            }
                        }
                    }
                    if (inWar || !(type == 2 || type == 3)) {
                        if (clan.getCastleId() != 0) {
                            if (type == 0) {
                                player.sendPackets(new S_ServerMessage(474));
                                over();
                                return;
                            } else if (type == 2 || type == 3) {
                                over();
                                return;
                            }
                        }
                        if (enemyClan.getCastleId() != 0 || player.getLevel() > 25) {
                            if (enemyClan.getCastleId() != 0) {
                                war_castle(player, clan, enemyClan, type);
                            } else if (ServerWarExecutor.get().checkCastleWar() > 0) {
                                player.sendPackets(new S_HelpMessage("\\fS攻城戰期間，暫停血盟宣戰。"));
                                over();
                                return;
                            } else {
                                boolean enemyInWar = false;
                                Iterator<L1War> it2 = warList.iterator();
                                while (true) {
                                    if (!it2.hasNext()) {
                                        break;
                                    }
                                    L1War war = it2.next();
                                    if (war.checkClanInWar(enemyClanName)) {
                                        switch (type) {
                                            case 0:
                                                player.sendPackets(new S_ServerMessage((int) OpcodesClient.C_OPCODE_CHARRESET, enemyClanName));
                                                over();
                                                return;
                                            case 2:
                                            case 3:
                                                if (!war.checkClanInSameWar(clanName, enemyClanName)) {
                                                    over();
                                                    return;
                                                }
                                                break;
                                        }
                                        enemyInWar = true;
                                    }
                                }
                                if (enemyInWar || !(type == 2 || type == 3)) {
                                    L1PcInstance enemyLeader = World.get().getPlayer(enemyClan.getLeaderName());
                                    if (enemyLeader != null) {
                                        switch (type) {
                                            case 0:
                                                enemyLeader.setTempID(player.getId());
                                                enemyLeader.sendPackets(new S_Message_YN(217, clanName, playerName));
                                                break;
                                            case 2:
                                                enemyLeader.setTempID(player.getId());
                                                enemyLeader.sendPackets(new S_Message_YN(OpcodesClient.C_OPCODE_BOARDBACK, clanName));
                                                break;
                                            case 3:
                                                enemyLeader.setTempID(player.getId());
                                                enemyLeader.sendPackets(new S_Message_YN(222, clanName));
                                                break;
                                        }
                                    } else {
                                        player.sendPackets(new S_ServerMessage(218, enemyClanName));
                                        over();
                                        return;
                                    }
                                } else {
                                    over();
                                    return;
                                }
                            }
                            over();
                            return;
                        }
                        player.sendPackets(new S_ServerMessage((int) OpcodesServer.S_OPCODE_SKILLSOUNDGFX));
                        over();
                        return;
                    }
                    over();
                }
            }
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    private void war_castle(L1PcInstance player, L1Clan clan, L1Clan enemyClan, int type) {
        L1PcInstance[] clanMember;
        try {
            if (player.getLevel() < 25) {
                player.sendPackets(new S_ServerMessage(475));
            } else if (ClanEmblemReading.get().get(clan.getClanId()) != null || player.isGm()) {
                String clanName = player.getClanname();
                if (enemyClan == null) {
                    L1Object object = World.get().findObject(player.getTempID());
                    if (object instanceof L1NpcInstance) {
                        int id = L1CastleLocation.getCastleIdByArea((L1NpcInstance) object);
                        if (ServerWarExecutor.get().isNowWar(id)) {
                            L1PcInstance[] clanMember2 = clan.getOnlineClanMember();
                            for (L1PcInstance l1PcInstance : clanMember2) {
                                if (L1CastleLocation.checkInWarArea(id, l1PcInstance)) {
                                    player.sendPackets(new S_ServerMessage(477));
                                    return;
                                }
                            }
                            for (L1PcInstance l1PcInstance2 : clanMember2) {
                                l1PcInstance2.sendPackets(new S_War(1, clanName, "NPC"));
                            }
                            player.sendPackets(new S_CloseList(player.getId()));
                            return;
                        }
                        if (type == 0) {
                            player.sendPackets(new S_ServerMessage(476));
                        }
                        player.sendPackets(new S_CloseList(player.getId()));
                        return;
                    }
                    return;
                }
                String enemyClanName = enemyClan.getClanName();
                if (WorldWar.get().isWar(clan.getClanName(), enemyClanName)) {
                    player.sendPackets(new S_ServerMessage(234));
                    return;
                }
                int castle_id = enemyClan.getCastleId();
                if (ServerWarExecutor.get().isNowWar(castle_id)) {
                    for (L1PcInstance l1PcInstance3 : clan.getOnlineClanMember()) {
                        if (L1CastleLocation.checkInWarArea(castle_id, l1PcInstance3)) {
                            player.sendPackets(new S_ServerMessage(477));
                            return;
                        }
                    }
                    boolean enemyInWar = false;
                    Iterator<L1War> it = WorldWar.get().getWarList().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        L1War war = it.next();
                        if (war.checkClanInWar(enemyClanName)) {
                            if (type == 0) {
                                war.declareWar(clanName, enemyClanName);
                                war.addAttackClan(clanName);
                                player.sendPackets(new S_CloseList(player.getId()));
                            } else if (type == 2 || type == 3) {
                                if (!war.checkClanInSameWar(clanName, enemyClanName)) {
                                    return;
                                }
                                if (type == 2) {
                                    war.surrenderWar(clanName, enemyClanName);
                                } else if (type == 3) {
                                    war.ceaseWar(clanName, enemyClanName);
                                }
                            }
                            enemyInWar = true;
                        }
                    }
                    if (!enemyInWar && type == 0) {
                        new L1War().handleCommands(1, clanName, enemyClanName);
                        player.sendPackets(new S_CloseList(player.getId()));
                        return;
                    }
                    return;
                }
                if (type == 0) {
                    player.sendPackets(new S_ServerMessage(476));
                }
                player.sendPackets(new S_CloseList(player.getId()));
            } else {
                player.sendPackets(new S_ServerMessage(812));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
