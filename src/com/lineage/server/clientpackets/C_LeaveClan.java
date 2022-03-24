package com.lineage.server.clientpackets;

import com.lineage.config.ConfigOther;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.ClanEmblemReading;
import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1ClanMatching;
import com.lineage.server.model.L1War;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CharTitle;
import com.lineage.server.serverpackets.S_ClanUpdate;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldWar;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import william.L1PcUnlock;

public class C_LeaveClan extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_LeaveClan.class);

    public C_LeaveClan() {
    }

    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            try {
                L1PcInstance player = client.getActiveChar();
                String player_name = player.getName();
                String clan_name = player.getClanname();
                int clan_id = player.getClanid();
                if (clan_id == 0) {
                    return;
                }

                L1Clan clan = WorldClan.get().getClan(clan_name);
                if (clan == null) {
                    player.setClanid(0);
                    player.setClanname("");
                    player.setClanRank(0);
                    player.setTitle("");
                    player.sendPacketsAll(new S_CharTitle(player.getId()));
                    player.save();
                    player.sendPackets(new S_ServerMessage(178, player_name, clan_name));
                    player.sendPackets(new S_ClanUpdate(player.getId()));
                    return;
                }

                String[] clan_member_name = clan.getAllMembers();
                int i;
                if (player.isCrown() && player.getId() == clan.getLeaderId()) {
                    if (!ConfigOther.CLANDEL) {
                        player.sendPackets(new S_ServerMessage(302));
                        player.sendPackets(new S_NPCTalkReturn(player.getId(), "y_clanD"));
                        return;
                    }

                    int castleId = clan.getCastleId();
                    int houseId = clan.getHouseId();
                    if (castleId != 0) {
                        player.sendPackets(new S_ServerMessage(665));
                        return;
                    }

                    if (houseId != 0) {
                        player.sendPackets(new S_ServerMessage(665));
                        return;
                    }

                    Iterator var13 = WorldWar.get().getWarList().iterator();

                    L1War war;
                    do {
                        if (!var13.hasNext()) {
                            for(i = 0; i < clan_member_name.length; ++i) {
                                L1PcInstance online_pc = World.get().getPlayer(clan_member_name[i]);
                                if (online_pc != null) {
                                    online_pc.setClanid(0);
                                    online_pc.setClanname("");
                                    online_pc.setClanRank(0);
                                    online_pc.setTitle("");
                                    online_pc.sendPacketsAll(new S_CharTitle(online_pc.getId()));
                                    online_pc.save();
                                    L1PcUnlock.Pc_Unlock(online_pc);
                                    online_pc.sendPackets(new S_ServerMessage(269, player_name, clan_name));
                                    online_pc.sendPackets(new S_ClanUpdate(online_pc.getId()));
                                } else {
                                    try {
                                        L1PcInstance offline_pc = CharacterTable.get().restoreCharacter(clan_member_name[i]);
                                        offline_pc.setClanid(0);
                                        offline_pc.setClanname("");
                                        offline_pc.setClanRank(0);
                                        offline_pc.setTitle("");
                                        offline_pc.save();
                                    } catch (Exception var18) {
                                        _log.error(var18.getLocalizedMessage(), var18);
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                }
                            }

                            ClanEmblemReading.get().deleteIcon(clan_id);
                            L1ClanMatching cml = L1ClanMatching.getInstance();
                            cml.deleteClanMatching(clan_name);
                            cml.deleteClanMatchingApcList(clan);
                            ClanReading.get().deleteClan(clan_name);
                            return;
                        }

                        war = (L1War)var13.next();
                    } while(!war.checkClanInWar(clan_name));

                    player.sendPackets(new S_ServerMessage(302));
                    return;
                }

                L1PcInstance[] clanMember = clan.getOnlineClanMember();

                for(i = 0; i < clanMember.length; ++i) {
                    clanMember[i].sendPackets(new S_ServerMessage(178, player_name, clan_name));
                    clanMember[i].sendPackets(new S_ClanUpdate(player.getId()));
                }

                if (clan.getWarehouseUsingChar() == player.getId()) {
                    clan.setWarehouseUsingChar(0);
                }

                player.setClanid(0);
                player.setClanname("");
                player.setClanRank(0);
                player.setTitle("");
                player.sendPacketsAll(new S_CharTitle(player.getId()));
                player.save();
                clan.delMemberName(player_name);
            } catch (Exception var19) {
            }

        } finally {
            this.over();
        }
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
}
