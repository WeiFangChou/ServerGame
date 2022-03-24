package com.lineage.data.npc.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv45_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Roje extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Roje.class);

    private Npc_Roje() {
    }

    public static NpcExecutor get() {
        return new Npc_Roje();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje16"));
            } else if (pc.isKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje16"));
            } else if (pc.isElf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje16"));
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje16"));
            } else if (pc.isDarkelf()) {
                if (pc.getQuest().isEnd(DarkElfLv45_1.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje15"));
                } else if (pc.getLevel() >= DarkElfLv45_1.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(DarkElfLv45_1.QUEST.get_id())) {
                        case 2:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje11"));
                            return;
                        case 3:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje12"));
                            return;
                        case 4:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje13"));
                            return;
                        default:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje15"));
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje16"));
                }
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje16"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje16"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje16"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        boolean isCloseList = false;
        if (pc.isDarkelf()) {
            if (!pc.getQuest().isEnd(DarkElfLv45_1.QUEST.get_id())) {
                switch (pc.getQuest().get_step(DarkElfLv45_1.QUEST.get_id())) {
                    case 2:
                        if (cmd.equalsIgnoreCase("quest 19 roje12")) {
                            pc.getQuest().set_step(DarkElfLv45_1.QUEST.get_id(), 3);
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje12"));
                            break;
                        }
                        break;
                    case 3:
                        if (cmd.equalsIgnoreCase("request mark of assassin")) {
                            L1ItemInstance item = pc.getInventory().checkItemX(40584, 1);
                            if (item == null) {
                                isCloseList = true;
                                pc.sendPackets(new S_ServerMessage(337, "$2424 (1)"));
                                break;
                            } else {
                                pc.getInventory().removeItem(item, 1);
                                CreateNewItem.getQuestItem(pc, npc, 40572, 1);
                                pc.getQuest().set_step(DarkElfLv45_1.QUEST.get_id(), 4);
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje13"));
                                break;
                            }
                        }
                        break;
                    case 4:
                        if (cmd.equalsIgnoreCase("quest 21 roje14")) {
                            pc.getQuest().set_step(DarkElfLv45_1.QUEST.get_id(), 5);
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje14"));
                            break;
                        }
                        break;
                    default:
                        isCloseList = true;
                        break;
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
