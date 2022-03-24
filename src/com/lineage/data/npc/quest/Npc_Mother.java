package com.lineage.data.npc.quest;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ElfLv15_2;
import com.lineage.data.quest.ElfLv30_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Mother extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Mother.class);

    private Npc_Mother() {
    }

    public static NpcExecutor get() {
        return new Npc_Mother();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherm1"));
            } else if (pc.isKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherm1"));
            } else if (pc.isElf()) {
                if (!pc.getQuest().isEnd(ElfLv15_2.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mothere1"));
                } else if (pc.getQuest().isEnd(ElfLv30_1.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherEE3"));
                } else if (pc.getLevel() >= ElfLv30_1.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(ElfLv30_1.QUEST.get_id())) {
                        case 0:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherEE1"));
                            return;
                        default:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherEE2"));
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mothere1"));
                }
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherm1"));
            } else if (pc.isDarkelf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherm1"));
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherm1"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherm1"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherm1"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (!pc.isElf()) {
            isCloseList = true;
        } else if (!pc.getQuest().isEnd(ElfLv30_1.QUEST.get_id())) {
            if (cmd.equalsIgnoreCase("quest 12 motherEE2")) {
                QuestClass.get().startQuest(pc, ElfLv30_1.QUEST.get_id());
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherEE2"));
            } else if (cmd.equalsIgnoreCase("request questitem2")) {
                if (CreateNewItem.checkNewItem(pc, new int[]{40592}, new int[]{1}) < 1) {
                    isCloseList = true;
                } else {
                    CreateNewItem.createNewItem(pc, new int[]{40592}, new int[]{1}, new int[]{40588}, 1, new int[]{1});
                    QuestClass.get().endQuest(pc, ElfLv30_1.QUEST.get_id());
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherEE3"));
                }
            }
        } else {
            return;
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
