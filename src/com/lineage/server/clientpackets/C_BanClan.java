package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ClanUpdate;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_BanClan extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_BanClan.class);

    public C_BanClan() {
    }

    public void start(byte[] decrypt, ClientExecutor client) throws Exception {
        try {
            this.read(decrypt);
            String s = this.readS();
            L1PcInstance pc = client.getActiveChar();
            L1Clan clan = WorldClan.get().getClan(pc.getClanname());
            if (clan != null) {
                String[] clanMemberName = clan.getAllMembers();
                if (pc.isCrown() && pc.getId() == clan.getLeaderId()) {
                    for(int i = 0; i < clanMemberName.length; ++i) {
                        if (pc.getName().toLowerCase().equals(s.toLowerCase())) {
                            return;
                        }
                    }

                    L1PcInstance tempPc = World.get().getPlayer(s);
                    L1PcInstance clanMembers;
                    if (tempPc != null) {
                        try {
                            if (tempPc.getClanid() == pc.getClanid()) {
                                tempPc.setClanid(0);
                                tempPc.setClanname("");
                                tempPc.setClanRank(0);
                                tempPc.save();
                                clan.delMemberName(tempPc.getName());
                                tempPc.sendPackets(new S_ServerMessage(238, pc.getClanname()));
                                tempPc.sendPackets(new S_ClanUpdate(tempPc.getId()));
                                L1PcInstance[] var12;
                                int var11 = (var12 = clan.getOnlineClanMember()).length;

                                for(int var10 = 0; var10 < var11; ++var10) {
                                    clanMembers = var12[var10];
                                    clanMembers.sendPackets(new S_ClanUpdate(tempPc.getId()));
                                }

                                pc.sendPackets(new S_ServerMessage(240, tempPc.getName()));
                            } else {
                                pc.sendPackets(new S_ServerMessage(109, s));
                            }
                        } catch (Exception var19) {
                            _log.error(var19.getLocalizedMessage(), var19);
                        }
                    } else {
                        try {
                            clanMembers = CharacterTable.get().restoreCharacter(s);
                            if (clanMembers != null && clanMembers.getClanid() == pc.getClanid()) {
                                clanMembers.setClanid(0);
                                clanMembers.setClanname("");
                                clanMembers.setClanRank(0);
                                clanMembers.save();
                                clan.delMemberName(clanMembers.getName());
                                pc.sendPackets(new S_ServerMessage(240, clanMembers.getName()));
                            } else {
                                pc.sendPackets(new S_ServerMessage(109, s));
                            }
                        } catch (Exception var18) {
                            _log.error(var18.getLocalizedMessage(), var18);
                        }
                    }
                } else {
                    pc.sendPackets(new S_ServerMessage(518));
                }
            }
        } catch (Exception var20) {
        } finally {
            this.over();
        }

    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
}
