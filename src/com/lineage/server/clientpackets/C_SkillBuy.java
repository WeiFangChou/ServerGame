package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.list.PcLvSkillList;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_SkillBuy;
import com.lineage.server.world.World;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_SkillBuy extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_SkillBuy.class);

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
                } else if (pc.isPrivateShop()) {
                    over();
                } else {
                    L1Object obj = World.get().findObject(readD());
                    if (obj == null) {
                        over();
                        return;
                    }
                    L1NpcInstance npc = null;
                    if (obj instanceof L1NpcInstance) {
                        npc = (L1NpcInstance) obj;
                    }
                    if (npc == null) {
                        over();
                        return;
                    }
                    ArrayList<Integer> skillList = PcLvSkillList.scount(pc);
                    ArrayList<Integer> newSkillList = new ArrayList<>();
                    Iterator<Integer> it = skillList.iterator();
                    while (it.hasNext()) {
                        Integer integer = it.next();
                        if (!CharSkillReading.get().spellCheck(pc.getId(), integer.intValue() + 1)) {
                            newSkillList.add(integer);
                        }
                    }
                    switch (npc.getNpcId()) {
                        case 70009:
                            if (pc.getLawful() >= 0) {
                                if (newSkillList.size() > 0) {
                                    pc.sendPackets(new S_SkillBuy(pc, newSkillList));
                                    break;
                                } else {
                                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerengEv3"));
                                    over();
                                    return;
                                }
                            } else {
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerengEv2"));
                                over();
                                return;
                            }
                        case 70087:
                            if (pc.getLawful() >= 0) {
                                if (newSkillList.size() > 0) {
                                    pc.sendPackets(new S_SkillBuy(pc, newSkillList));
                                    break;
                                } else {
                                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "sedia1"));
                                    over();
                                    return;
                                }
                            } else {
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "sedia3"));
                                over();
                                return;
                            }
                        default:
                            pc.sendPackets(new S_SkillBuy(pc, newSkillList));
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
