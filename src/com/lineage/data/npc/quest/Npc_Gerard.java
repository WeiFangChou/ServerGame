package com.lineage.data.npc.quest;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.KnightLv30_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Gerard extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Gerard.class);

    private Npc_Gerard() {
    }

    public static NpcExecutor get() {
        return new Npc_Gerard();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardp1"));
            } else if (pc.isKnight()) {
                if (pc.getQuest().isEnd(KnightLv30_1.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardkEcg"));
                } else if (pc.getLevel() >= KnightLv30_1.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(KnightLv30_1.QUEST.get_id())) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardk7"));
                            return;
                        case 4:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardkE1"));
                            return;
                        case 5:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardkE2"));
                            return;
                        case 6:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardkE3"));
                            return;
                        case 7:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardkE4"));
                            return;
                        default:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardkE3"));
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardk7"));
                }
            } else if (pc.isElf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerarde1"));
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardw1"));
            } else if (pc.isDarkelf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardde1"));
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardk4"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardk4"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardk4"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        boolean isCloseList = false;
        if (pc.isKnight()) {
            if (!pc.getQuest().isEnd(KnightLv30_1.QUEST.get_id())) {
                if (pc.getQuest().isStart(KnightLv30_1.QUEST.get_id())) {
                    switch (pc.getQuest().get_step(KnightLv30_1.QUEST.get_id())) {
                        case 4:
                            if (cmd.equalsIgnoreCase("quest 16 gerardkE2")) {
                                pc.getQuest().set_step(KnightLv30_1.QUEST.get_id(), 5);
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardkE2"));
                                break;
                            }
                            break;
                        case 5:
                            if (cmd.equalsIgnoreCase("request potion of rebirth")) {
                                if (CreateNewItem.checkNewItem(pc, 40544, 1) >= 1) {
                                    CreateNewItem.createNewItem(pc, 40544, 1, 40607, 1);
                                    pc.getQuest().set_step(KnightLv30_1.QUEST.get_id(), 6);
                                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardkE3"));
                                    break;
                                } else {
                                    isCloseList = true;
                                    break;
                                }
                            }
                            break;
                        case 6:
                            if (cmd.equalsIgnoreCase("quest 18 gerardkE4")) {
                                pc.getQuest().set_step(KnightLv30_1.QUEST.get_id(), 7);
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardkE4"));
                                break;
                            }
                            break;
                        case 7:
                            if (cmd.equalsIgnoreCase("request shield of red knights")) {
                                if (CreateNewItem.checkNewItem(pc, 40529, 1) >= 1) {
                                    CreateNewItem.createNewItem(pc, 40529, 1, 20230, 1);
                                    QuestClass.get().endQuest(pc, KnightLv30_1.QUEST.get_id());
                                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardkE5"));
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
