package com.lineage.data.npc.quest;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CrownLv15_1;
import com.lineage.data.quest.KnightLv30_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Qunter extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Qunter.class);

    private Npc_Qunter() {
    }

    public static NpcExecutor get() {
        return new Npc_Qunter();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {
                if (pc.getQuest().isEnd(CrownLv15_1.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterp11"));
                } else if (pc.getLevel() >= CrownLv15_1.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(CrownLv15_1.QUEST.get_id())) {
                        case 0:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterp9"));
                            return;
                        case 1:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterp9"));
                            return;
                        case 2:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterp1"));
                            return;
                        case 3:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterpev1"));
                            return;
                        default:
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterp12"));
                }
            } else if (pc.isKnight()) {
                if (pc.getLawful() < 0) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterk12"));
                } else if (pc.getQuest().isEnd(KnightLv30_1.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterkEcg"));
                } else if (pc.getLevel() >= KnightLv30_1.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(KnightLv30_1.QUEST.get_id())) {
                        case 0:
                        case 1:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterk9"));
                            return;
                        case 2:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterkE1"));
                            return;
                        case 3:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterkE2"));
                            return;
                        default:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterkE3"));
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterk9"));
                }
            } else if (pc.isElf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "guntere1"));
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterw1"));
            } else if (pc.isDarkelf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterdw1"));
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterdwk1"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunteriw1"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        boolean isCloseList = false;
        if (pc.isCrown()) {
            if (cmd.equalsIgnoreCase("guntertest")) {
                if (!pc.getQuest().isEnd(CrownLv15_1.QUEST.get_id())) {
                    switch (pc.getQuest().get_step(CrownLv15_1.QUEST.get_id())) {
                        case 0:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterp9"));
                            break;
                        case 1:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterp9"));
                            break;
                        case 2:
                            pc.getQuest().set_step(CrownLv15_1.QUEST.get_id(), 3);
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterpev1"));
                            break;
                    }
                }
            } else if (cmd.equalsIgnoreCase("request spellbook112")) {
                if (pc.getQuest().get_step(CrownLv15_1.QUEST.get_id()) != 3) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterp9"));
                } else if (CreateNewItem.checkNewItem(pc, 40564, 1) < 1) {
                    isCloseList = true;
                } else {
                    CreateNewItem.createNewItem(pc, 40564, 1, 40226, 1);
                    QuestClass.get().endQuest(pc, CrownLv15_1.QUEST.get_id());
                    isCloseList = true;
                }
            }
        } else if (pc.isKnight()) {
            if (cmd.equalsIgnoreCase("quest 14 gunterkE2")) {
                pc.getQuest().set_step(KnightLv30_1.QUEST.get_id(), 3);
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterkE2"));
            } else if (cmd.equalsIgnoreCase("request sword of red knights")) {
                if (pc.getQuest().get_step(KnightLv30_1.QUEST.get_id()) != 3) {
                    isCloseList = true;
                } else if (CreateNewItem.checkNewItem(pc, 40590, 1) < 1) {
                    isCloseList = true;
                } else {
                    CreateNewItem.createNewItem(pc, 40590, 1, 30, 1);
                    pc.getQuest().set_step(KnightLv30_1.QUEST.get_id(), 4);
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterkE3"));
                }
            }
        } else if (pc.isElf() && cmd.equalsIgnoreCase("guntertest")) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "guntereev1"));
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
