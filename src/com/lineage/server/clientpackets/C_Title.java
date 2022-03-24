package com.lineage.server.clientpackets;

import com.lineage.config.ConfigOther;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.serverpackets.S_CharTitle;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Title extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Title.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            String charName = readS();
            StringBuilder title = new StringBuilder();
            title.append(readS());
            if (charName.isEmpty() || title.length() <= 0) {
                pc.sendPackets(new S_ServerMessage(196));
                return;
            }
            L1PcInstance target = World.get().getPlayer(charName);
            if (target == null) {
                over();
            } else if (pc.isGm()) {
                changeTitle(target, title);
                over();
            } else {
                if (isClanLeader(pc)) {
                    if (pc.getId() == target.getId()) {
                        if (pc.getLevel() < 10) {
                            pc.sendPackets(new S_ServerMessage(197));
                            over();
                            return;
                        }
                        changeTitle(pc, title);
                    } else if (pc.getClanid() != target.getClanid()) {
                        pc.sendPackets(new S_ServerMessage(199));
                        over();
                        return;
                    } else if (target.getLevel() < 10) {
                        pc.sendPackets(new S_ServerMessage(202, charName));
                        over();
                        return;
                    } else {
                        changeTitle(target, title);
                        L1Clan clan = WorldClan.get().getClan(pc.getClanname());
                        if (clan != null) {
                            for (L1PcInstance clanPc : clan.getOnlineClanMember()) {
                                clanPc.sendPackets(new S_ServerMessage(203, pc.getName(), charName, title.toString()));
                            }
                        }
                    }
                } else if (pc.getId() == target.getId()) {
                    if (!ConfigOther.CLANTITLE && pc.getClanid() != 0) {
                        pc.sendPackets(new S_ServerMessage(198));
                        over();
                        return;
                    } else if (target.getLevel() < 40) {
                        pc.sendPackets(new S_ServerMessage(200));
                        over();
                        return;
                    } else {
                        changeTitle(pc, title);
                    }
                } else if (pc.isCrown() && pc.getClanid() == target.getClanid()) {
                    pc.sendPackets(new S_ServerMessage(201, pc.getClanname()));
                    over();
                    return;
                }
                over();
            }
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    private void changeTitle(L1PcInstance pc, StringBuilder title) {
        int objectId = pc.getId();
        pc.setTitle(title.toString());
        pc.sendPacketsAll(new S_CharTitle(objectId, title));
        try {
            pc.save();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private boolean isClanLeader(L1PcInstance pc) {
        L1Clan clan;
        if (pc.getClanid() == 0 || (clan = WorldClan.get().getClan(pc.getClanname())) == null || !pc.isCrown() || pc.getId() != clan.getLeaderId()) {
            return false;
        }
        return true;
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
