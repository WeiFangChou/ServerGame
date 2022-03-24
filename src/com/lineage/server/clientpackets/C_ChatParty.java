package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1ChatParty;
import com.lineage.server.serverpackets.S_Party;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_ChatParty extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_ChatParty.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (decrypt.length > 108) {
                _log.warn("人物:" + pc.getName() + "對話(隊伍)長度超過限制:" + client.getIp().toString());
                client.set_error(client.get_error() + 1);
            } else if (pc.isGhost()) {
                over();
            } else if (pc.isTeleport()) {
                over();
            } else {
                switch (readC()) {
                    case 0:
                        String name = readS();
                        if (!pc.isInChatParty()) {
                            pc.sendPackets(new S_ServerMessage(425));
                            over();
                            return;
                        } else if (pc.getChatParty().isLeader(pc)) {
                            L1PcInstance targetPc = World.get().getPlayer(name);
                            if (targetPc == null) {
                                pc.sendPackets(new S_ServerMessage(109));
                                over();
                                return;
                            } else if (pc.getId() != targetPc.getId()) {
                                L1PcInstance[] members = pc.getChatParty().getMembers();
                                for (L1PcInstance member : members) {
                                    if (member.getName().toLowerCase().equals(name.toLowerCase())) {
                                        pc.getChatParty().kickMember(member);
                                        over();
                                        return;
                                    }
                                }
                                pc.sendPackets(new S_ServerMessage(426, name));
                                break;
                            } else {
                                over();
                                return;
                            }
                        } else {
                            pc.sendPackets(new S_ServerMessage(427));
                            over();
                            return;
                        }
                    case 1:
                        if (pc.isInChatParty()) {
                            pc.getChatParty().leaveMember(pc);
                            break;
                        }
                        break;
                    case 2:
                        L1ChatParty chatParty = pc.getChatParty();
                        if (pc.isInChatParty()) {
                            pc.sendPackets(new S_Party("party", pc.getId(), chatParty.getLeader().getName(), chatParty.getMembersNameList()));
                            break;
                        } else {
                            pc.sendPackets(new S_ServerMessage(425));
                            break;
                        }
                }
                over();
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
