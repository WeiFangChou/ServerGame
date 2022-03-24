package com.lineage.data.npc.quest;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv30_1;
import com.lineage.data.quest.DarkElfLv45_1;
import com.lineage.data.quest.DarkElfLv50_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Bluedika extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Bluedika.class);

    private Npc_Bluedika() {
    }

    public static NpcExecutor get() {
        return new Npc_Bluedika();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq4"));
            } else if (pc.isKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq4"));
            } else if (pc.isElf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq4"));
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq4"));
            } else if (pc.isDarkelf()) {
                if (!pc.getQuest().isEnd(DarkElfLv30_1.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq3"));
                } else if (pc.getQuest().isEnd(DarkElfLv45_1.QUEST.get_id())) {
                    if (pc.getQuest().isEnd(DarkElfLv50_1.QUEST.get_id())) {
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq8"));
                    } else if (pc.getLevel() >= DarkElfLv50_1.QUEST.get_questlevel()) {
                        switch (pc.getQuest().get_step(DarkElfLv50_1.QUEST.get_id())) {
                            case 0:
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq6"));
                                return;
                            default:
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq7"));
                                return;
                        }
                    } else {
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq5"));
                    }
                } else if (pc.getLevel() >= DarkElfLv45_1.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(DarkElfLv45_1.QUEST.get_id())) {
                        case 0:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq1"));
                            return;
                        default:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq2"));
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde7"));
                }
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq4"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq4"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq4"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (pc.isDarkelf()) {
            if (!pc.getQuest().isEnd(DarkElfLv45_1.QUEST.get_id())) {
                switch (pc.getQuest().get_step(DarkElfLv45_1.QUEST.get_id())) {
                    case 0:
                        if (cmd.equalsIgnoreCase("quest 17 bluedikaq2")) {
                            QuestClass.get().startQuest(pc, DarkElfLv45_1.QUEST.get_id());
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq2"));
                            break;
                        }
                        break;
                    default:
                        if (cmd.equalsIgnoreCase("request bluedikabag")) {
                            if (CreateNewItem.checkNewItem(pc, new int[]{40572, 40595}, new int[]{1, 1}) >= 1) {
                                CreateNewItem.createNewItem(pc, new int[]{40572, 40595}, new int[]{1, 1}, new int[]{40553}, 1, new int[]{1});
                                QuestClass.get().endQuest(pc, DarkElfLv45_1.QUEST.get_id());
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq3"));
                                break;
                            } else {
                                isCloseList = true;
                                break;
                            }
                        }
                        break;
                }
            } else {
                isCloseList = true;
                if (!pc.getQuest().isEnd(DarkElfLv50_1.QUEST.get_id())) {
                    switch (pc.getQuest().get_step(DarkElfLv50_1.QUEST.get_id())) {
                        case 0:
                            if (cmd.equalsIgnoreCase("quest 24 bluedikaq7")) {
                                QuestClass.get().startQuest(pc, DarkElfLv50_1.QUEST.get_id());
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq7"));
                                isCloseList = false;
                                break;
                            }
                            break;
                        default:
                            if (cmd.equalsIgnoreCase("request finger of death")) {
                                if (CreateNewItem.checkNewItem(pc, new int[]{20037, 40603, 40541}, new int[]{1, 1, 1}) >= 1) {
                                    CreateNewItem.createNewItem(pc, new int[]{20037, 40603, 40541}, new int[]{1, 1, 1}, new int[]{13}, 1, new int[]{1});
                                    QuestClass.get().endQuest(pc, DarkElfLv50_1.QUEST.get_id());
                                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq8"));
                                    isCloseList = false;
                                    break;
                                } else {
                                    isCloseList = true;
                                    break;
                                }
                            }
                            break;
                    }
                } else {
                    isCloseList = true;
                }
            }
        } else {
            isCloseList = true;
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
