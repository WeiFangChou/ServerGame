package com.lineage.server.serverpackets;

import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1ClanMatching;
import com.lineage.server.model.L1War;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldWar;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class S_ClanMatching extends ServerBasePacket {
    private static final Log _log = LogFactory.getLog(S_ClanMatching.class);
    private byte[] _byte = null;

    public S_ClanMatching(boolean postStatus, String clanname) {
        writeC(192);
        writeC(postStatus ? 0 : 1);
        writeC(0);
        writeD(0);
        writeC(0);
    }

    public S_ClanMatching(L1PcInstance pc, int type, int objid, int htype) throws Throwable {
        try {
            L1ClanMatching cml = L1ClanMatching.getInstance();
            String clanname = null;
            writeC(192);
            writeC(type);
            if (type == 2) {
                ArrayList<L1ClanMatching.ClanMatchingList> showcmalist = new ArrayList<>();
                for (int i = 0; i < cml.getMatchingList().size(); i++) {
                    clanname = cml.getMatchingList().get(i)._clanname;
                    if (!pc.getCMAList().contains(clanname)) {
                        showcmalist.add(cml.getMatchingList().get(i));
                    }
                }
                int size = showcmalist.size();
                writeC(0);
                writeC(size);
                for (int i2 = 0; i2 < size; i2++) {
                    String text = showcmalist.get(i2)._text;
                    int type2 = showcmalist.get(i2)._type;
                    L1Clan clan = WorldClan.get().getClan(showcmalist.get(i2)._clanname);
                    writeD(clan.getClanId());
                    writeS(clan.getClanName());
                    writeS(clan.getLeaderName());
                    writeD(clan.getOnlineMaxUser());
                    writeC(type2);
                    if (clan.getHouseId() != 0) {
                        writeC(1);
                    } else {
                        writeC(0);
                    }
                    boolean inWar = false;
                    Iterator<L1War> it = WorldWar.get().getWarList().iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (it.next().checkClanInWar(clanname)) {
                                inWar = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (inWar) {
                        writeC(1);
                    } else {
                        writeC(0);
                    }
                    writeC(0);
                    writeS(text);
                }
                showcmalist.clear();
            } else if (type == 3) {
                int size2 = pc.getCMAList().size();
                writeC(0);
                writeC(size2);
                for (int i3 = 0; i3 < size2; i3++) {
                    String clanname2 = pc.getCMAList().get(i3);
                    L1Clan clan2 = WorldClan.get().getClan(clanname2);
                    writeD(clan2.getClanId());
                    writeC(0);
                    writeD(clan2.getClanId());
                    writeS(clan2.getClanName());
                    writeS(clan2.getLeaderName());
                    writeD(clan2.getOnlineMaxUser());
                    writeC(cml.getClanMatchingList(clanname2)._type);
                    if (clan2.getHouseId() != 0) {
                        writeC(1);
                    } else {
                        writeC(0);
                    }
                    boolean inWar2 = false;
                    Iterator<L1War> it2 = WorldWar.get().getWarList().iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            if (it2.next().checkClanInWar(clanname2)) {
                                inWar2 = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (inWar2) {
                        writeC(1);
                    } else {
                        writeC(0);
                    }
                    writeC(0);
                    writeS(cml.getClanMatchingList(clanname2)._text);
                }
            } else if (type == 4) {
                if (!cml.isClanMatchingList(pc.getClanname())) {
                    writeC(L1SkillId.BODY_TO_MIND);
                    return;
                }
                int size3 = pc.getInviteList().size();
                writeC(0);
                writeC(2);
                writeC(0);
                writeC(size3);
                for (int i4 = 0; i4 < size3; i4++) {
                    String username = pc.getInviteList().get(i4);
                    L1PcInstance user = World.get().getPlayer(username);
                    if (user == null) {
                        try {
                            user = CharacterTable.get().restoreCharacter(username);
                            if (user == null) {
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    writeD(user.getId());
                    writeC(0);
                    writeC(user.getOnlineStatus());
                    writeS(username);
                    writeC(user.getType());
                    writeH(user.getLawful());
                    writeC(user.getLevel());
                    writeC(1);
                }
            } else if (type == 5 || type == 6) {
                writeC(0);
                writeD(objid);
                writeC(htype);
            }
        } catch (Exception e2) {
            _log.error(e2.getLocalizedMessage(), e2);
        }
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = getBytes();
        }
        return this._byte;
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
