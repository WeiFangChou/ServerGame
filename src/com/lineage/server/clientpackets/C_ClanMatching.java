package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1ClanJoin;
import com.lineage.server.model.L1ClanMatching;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_ClanMatching;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_ClanMatching extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_ClanMatching.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) throws Throwable {
        L1Clan clan;
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGhost()) {
                if (pc.isDead()) {
                    over();
                } else if (pc.isTeleport()) {
                    over();
                } else {
                    int type = readC();
                    if (type == 0) {
                        L1ClanMatching cml = L1ClanMatching.getInstance();
                        int htype = readC();
                        String text = readS();
                        if (!cml.isClanMatchingList(pc.getClanname())) {
                            cml.writeClanMatching(pc.getClanname(), text, htype);
                        } else {
                            cml.updateClanMatching(pc.getClanname(), text, htype);
                        }
                        pc.sendPackets(new S_ClanMatching(true, pc.getClanname()));
                        pc.sendPackets(new S_ServerMessage(3261));
                    } else if (type == 1) {
                        L1ClanMatching cml2 = L1ClanMatching.getInstance();
                        if (cml2.isClanMatchingList(pc.getClanname())) {
                            cml2.deleteClanMatching(pc);
                            cml2.deleteClanMatchingApcList(pc.getClanname());
                        }
                        pc.sendPackets(new S_ClanMatching(false, pc.getClanname()));
                        pc.sendPackets(new S_ServerMessage(3262));
                    } else if (type >= 2 && type <= 4) {
                        pc.sendPackets(new S_ClanMatching(pc, type, 0, 0));
                    } else if (type == 5) {
                        int objid = readD();
                        L1Clan clan2 = WorldClan.get().getClan(objid);
                        if (clan2 != null && !pc.getCMAList().contains(clan2.getClanName())) {
                            L1ClanMatching.getInstance().writeClanMatchingApcList_User(pc, clan2);
                        }
                        pc.sendPackets(new S_ClanMatching(pc, type, objid, 0));
                    } else if (type == 6) {
                        int objid2 = readD();
                        int htype2 = readC();
                        L1ClanMatching cml3 = L1ClanMatching.getInstance();
                        if (htype2 == 1) {
                            L1Object target = World.get().findObject(objid2);
                            if (target != null && (target instanceof L1PcInstance)) {
                                L1PcInstance joinpc = (L1PcInstance) target;
                                if (!pc.getInviteList().contains(joinpc.getName())) {
                                    pc.sendPackets(new S_SystemMessage("此玩家已取消加入請求。"));
                                } else if (L1ClanJoin.getInstance().ClanJoin(pc, joinpc)) {
                                    cml3.deleteClanMatchingApcList(joinpc);
                                }
                            } else if (target == null) {
                                pc.sendPackets(new S_SystemMessage("此玩家已離線。"));
                            }
                        } else if (htype2 == 2) {
                            L1Object target2 = World.get().findObject(objid2);
                            if (target2 == null) {
                                cml3.deleteClanMatchingApcList(pc.getClan(), CharacterTable.get().getCharName(objid2));
                            } else if (target2 instanceof L1PcInstance) {
                                L1PcInstance user = (L1PcInstance) target2;
                                cml3.deleteClanMatchingApcList(user, user.getId(), pc.getClan());
                                user.sendPackets(new S_ServerMessage(3267));
                            }
                        } else if (htype2 == 3 && (clan = WorldClan.get().getClan(objid2)) != null && pc.getCMAList().contains(clan.getClanName())) {
                            cml3.deleteClanMatchingApcList(pc, clan);
                        }
                        pc.sendPackets(new S_ClanMatching(pc, type, objid2, htype2));
                    }
                    over();
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            over();
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
