package com.lineage.data.npc.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ElfLv45_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_NpcChat;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Heit extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Heit.class);

    private Npc_Heit() {
    }

    public static NpcExecutor get() {
        return new Npc_Heit();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.getLawful() < 0) {
                if (new Random().nextInt(100) < 20) {
                    npc.broadcastPacketX8(new S_NpcChat(npc, "$4991"));
                }
            } else if (pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));
            } else if (pc.isKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));
            } else if (pc.isElf()) {
                if (pc.getQuest().isEnd(ElfLv45_1.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));
                } else if (pc.getLevel() >= ElfLv45_1.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(ElfLv45_1.QUEST.get_id())) {
                        case 0:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));
                            return;
                        case 1:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit1"));
                            return;
                        case 2:
                        case 3:
                        case 4:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit2"));
                            return;
                        case 5:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit3"));
                            return;
                        default:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit5"));
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));
                }
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));
            } else if (pc.isDarkelf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        boolean isCloseList = false;
        if (pc.isElf()) {
            if (!pc.getQuest().isEnd(ElfLv45_1.QUEST.get_id())) {
                if (pc.getQuest().isStart(ElfLv45_1.QUEST.get_id())) {
                    switch (pc.getQuest().get_step(ElfLv45_1.QUEST.get_id())) {
                        case 1:
                            if (cmd.equalsIgnoreCase("quest 15 heit2")) {
                                pc.getQuest().set_step(ElfLv45_1.QUEST.get_id(), 2);
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit2"));
                                break;
                            }
                            break;
                        case 2:
                        case 3:
                        case 4:
                            if (cmd.equalsIgnoreCase("request mystery shell")) {
                                L1ItemInstance item = pc.getInventory().checkItemX(40602, 1);
                                if (item == null) {
                                    isCloseList = true;
                                    break;
                                } else {
                                    pc.getInventory().removeItem(item, 1);
                                    pc.getQuest().set_step(ElfLv45_1.QUEST.get_id(), 5);
                                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit3"));
                                    break;
                                }
                            }
                            break;
                        case 5:
                            if (cmd.equalsIgnoreCase("quest 17 heit5")) {
                                if (!pc.getInventory().checkItem(40566)) {
                                    pc.getQuest().set_step(ElfLv45_1.QUEST.get_id(), 6);
                                    CreateNewItem.getQuestItem(pc, npc, 40566, 1);
                                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit5"));
                                    break;
                                } else {
                                    isCloseList = true;
                                    break;
                                }
                            }
                            break;
                        default:
                            isCloseList = true;
                            break;
                    }
                } else {
                    isCloseList = true;
                }
            } else {
                return;
            }
        } else {
            isCloseList = true;
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
