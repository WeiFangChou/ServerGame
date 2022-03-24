package com.lineage.data.npc.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ElfLv45_2;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Zybril extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Zybril.class);

    private Npc_Zybril() {
    }

    public static NpcExecutor get() {
        return new Npc_Zybril();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril16"));
            } else if (pc.isKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril16"));
            } else if (pc.isElf()) {
                if (pc.getQuest().isEnd(ElfLv45_2.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril19"));
                } else if (pc.getLevel() >= ElfLv45_2.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(ElfLv45_2.QUEST.get_id())) {
                        case 0:
                        case 1:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril15"));
                            return;
                        case 2:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril1"));
                            return;
                        case 3:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril7"));
                            return;
                        case 4:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril8"));
                            return;
                        case 5:
                            if (pc.getInventory().checkItem(41349, 1)) {
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril18"));
                                return;
                            } else {
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril17"));
                                return;
                            }
                        default:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril19"));
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril16"));
                }
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril16"));
            } else if (pc.isDarkelf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril16"));
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril16"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril16"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril16"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        boolean isCloseList = false;
        if (pc.isElf()) {
            if (!pc.getQuest().isEnd(ElfLv45_2.QUEST.get_id())) {
                switch (pc.getQuest().get_step(ElfLv45_2.QUEST.get_id())) {
                    case 0:
                    case 1:
                        break;
                    case 2:
                        if (cmd.equals("A")) {
                            L1ItemInstance item = pc.getInventory().findItemId(41348);
                            if (item == null) {
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril11"));
                                break;
                            } else {
                                pc.getInventory().removeItem(item, 1);
                                pc.getQuest().set_step(ElfLv45_2.QUEST.get_id(), 3);
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril3"));
                                break;
                            }
                        }
                        break;
                    case 3:
                        if (cmd.equals("B")) {
                            if (CreateNewItem.checkNewItem(pc, new int[]{40048, 40049, 40050, 40051}, new int[]{10, 10, 10, 10}) >= 1) {
                                CreateNewItem.createNewItem(pc, new int[]{40048, 40049, 40050, 40051}, new int[]{10, 10, 10, 10}, new int[]{41353}, 1, new int[]{1});
                                pc.getQuest().set_step(ElfLv45_2.QUEST.get_id(), 4);
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril8"));
                                break;
                            } else {
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril12"));
                                break;
                            }
                        }
                        break;
                    case 4:
                        if (cmd.equals("C")) {
                            if (CreateNewItem.checkNewItem(pc, new int[]{40514, 41353}, new int[]{10, 1}) >= 1) {
                                CreateNewItem.createNewItem(pc, new int[]{40514, 41353}, new int[]{10, 1}, new int[]{41354}, 1, new int[]{1});
                                pc.getQuest().set_step(ElfLv45_2.QUEST.get_id(), 5);
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril9"));
                                break;
                            } else {
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril13"));
                                break;
                            }
                        }
                        break;
                    case 5:
                        if (cmd.equals("D")) {
                            if (CreateNewItem.checkNewItem(pc, new int[]{41349}, new int[]{1}) >= 1) {
                                CreateNewItem.createNewItem(pc, new int[]{41349}, new int[]{1}, new int[]{41351}, 1, new int[]{1});
                                pc.getQuest().set_step(ElfLv45_2.QUEST.get_id(), 6);
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril10"));
                                break;
                            } else {
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril14"));
                                break;
                            }
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
