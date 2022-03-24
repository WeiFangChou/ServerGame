package com.lineage.data.npc.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv50_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Kima extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Kima.class);

    private Npc_Kima() {
    }

    public static NpcExecutor get() {
        return new Npc_Kima();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));
            } else if (pc.isKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));
            } else if (pc.isElf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));
            } else if (pc.isDarkelf()) {
                if (pc.getQuest().isEnd(DarkElfLv50_1.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));
                } else if (pc.getLevel() >= DarkElfLv50_1.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(DarkElfLv50_1.QUEST.get_id())) {
                        case 0:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));
                            return;
                        case 1:
                        case 2:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kimaq1"));
                            return;
                        case 3:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kimaq3"));
                            return;
                        default:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kimaq4"));
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));
                }
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        boolean isCloseList = false;
        if (pc.isDarkelf()) {
            if (!pc.getQuest().isEnd(DarkElfLv50_1.QUEST.get_id())) {
                switch (pc.getQuest().get_step(DarkElfLv50_1.QUEST.get_id())) {
                    case 1:
                    case 2:
                        if (cmd.equalsIgnoreCase("request mask of true")) {
                            L1ItemInstance item = pc.getInventory().checkItemX(40583, 1);
                            if (item != null) {
                                pc.getInventory().removeItem(item, 1);
                                pc.getQuest().set_step(DarkElfLv50_1.QUEST.get_id(), 3);
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kimaq3"));
                                break;
                            } else {
                                pc.sendPackets(new S_ServerMessage(337, "$2654 (1)"));
                                isCloseList = true;
                                break;
                            }
                        }
                        break;
                    case 3:
                        if (cmd.equalsIgnoreCase("quest 26 kimaq4")) {
                            CreateNewItem.getQuestItem(pc, npc, 20037, 1);
                            pc.getQuest().set_step(DarkElfLv50_1.QUEST.get_id(), 4);
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kimaq4"));
                            break;
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
            isCloseList = true;
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
