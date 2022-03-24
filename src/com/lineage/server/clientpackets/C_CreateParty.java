package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Party;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_CreateParty extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_CreateParty.class);

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
                    switch (readC()) {
                        case 0:
                        case 1:
                            L1Object temp = World.get().findObject(readD());
                            if (temp instanceof L1PcInstance) {
                                L1PcInstance targetPc = (L1PcInstance) temp;
                                if (pc.getId() == targetPc.getId()) {
                                    over();
                                    return;
                                } else if (!targetPc.isInParty()) {
                                    if (pc.isInParty()) {
                                        if (pc.getParty().isLeader(pc)) {
                                            targetPc.setPartyID(pc.getId());
                                            targetPc.sendPackets(new S_Message_YN(953, pc.getName()));
                                            break;
                                        } else {
                                            pc.sendPackets(new S_ServerMessage(416));
                                            break;
                                        }
                                    } else {
                                        targetPc.setPartyID(pc.getId());
                                        targetPc.sendPackets(new S_Message_YN(953, pc.getName()));
                                        break;
                                    }
                                } else {
                                    pc.sendPackets(new S_ServerMessage(415));
                                    over();
                                    return;
                                }
                            }
                            break;
                        case 2:
                            L1PcInstance targetPc2 = World.get().getPlayer(readS());
                            if (targetPc2 == null) {
                                pc.sendPackets(new S_ServerMessage(109));
                                over();
                                return;
                            } else if (pc.getId() == targetPc2.getId()) {
                                over();
                                return;
                            } else if (!targetPc2.isInChatParty()) {
                                if (pc.isInChatParty()) {
                                    if (pc.getChatParty().isLeader(pc)) {
                                        targetPc2.setPartyID(pc.getId());
                                        targetPc2.sendPackets(new S_Message_YN(951, pc.getName()));
                                        break;
                                    } else {
                                        pc.sendPackets(new S_ServerMessage(416));
                                        break;
                                    }
                                } else {
                                    targetPc2.setPartyID(pc.getId());
                                    targetPc2.sendPackets(new S_Message_YN(951, pc.getName()));
                                    break;
                                }
                            } else {
                                pc.sendPackets(new S_ServerMessage(415));
                                over();
                                return;
                            }
                        case 3:
                            if (pc.isInParty()) {
                                if (pc.getParty().isLeader(pc)) {
                                    L1Object object = World.get().findObject(readD());
                                    if (object instanceof L1PcInstance) {
                                        L1PcInstance tgpc = (L1PcInstance) object;
                                        if (tgpc.getMapId() != pc.getMapId()) {
                                            pc.sendPackets(new S_Message_YN(1695));
                                        }
                                        if (pc.getLocation().isInScreen(tgpc.getLocation())) {
                                            HashMap<Integer, L1PcInstance> map = new HashMap<>();
                                            map.putAll(pc.getParty().partyUsers());
                                            ArrayList<L1PcInstance> newList = new ArrayList<>();
                                            for (L1PcInstance newpc : map.values()) {
                                                if (!newpc.equals(tgpc)) {
                                                    newList.add(newpc);
                                                }
                                            }
                                            map.clear();
                                            pc.getParty().breakup();
                                            L1Party party = new L1Party();
                                            party.addMember(tgpc);
                                            Iterator<L1PcInstance> it = newList.iterator();
                                            while (it.hasNext()) {
                                                L1PcInstance newpc2 = it.next();
                                                party.addMember(newpc2);
                                                tgpc.sendPackets(new S_ServerMessage(424, newpc2.getName()));
                                            }
                                            party.msgToAll();
                                            newList.clear();
                                            break;
                                        } else {
                                            pc.sendPackets(new S_ServerMessage(1695));
                                            break;
                                        }
                                    }
                                } else {
                                    pc.sendPackets(new S_ServerMessage(1697));
                                    break;
                                }
                            }
                            break;
                    }
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
