package com.lineage.data.npc.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.KnightLv45_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Giant extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Giant.class);

    private Npc_Giant() {
    }

    public static NpcExecutor get() {
        return new Npc_Giant();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk4"));
            } else if (pc.isKnight()) {
                if (pc.getQuest().isEnd(KnightLv45_1.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk3"));
                } else if (pc.getLevel() >= KnightLv45_1.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(KnightLv45_1.QUEST.get_id())) {
                        case 0:
                        case 1:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk4"));
                            return;
                        case 2:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk1"));
                            return;
                        case 3:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk2"));
                            return;
                        default:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk3"));
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk4"));
                }
            } else if (pc.isElf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk4"));
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk4"));
            } else if (pc.isDarkelf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk4"));
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk4"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk4"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk4"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        boolean isCloseList = false;
        if (!pc.isKnight()) {
            isCloseList = true;
        } else if (!pc.getQuest().isEnd(KnightLv45_1.QUEST.get_id())) {
            if (cmd.equalsIgnoreCase("quest 23 giantk2")) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk2"));
                pc.getQuest().set_step(KnightLv45_1.QUEST.get_id(), 3);
            } else if (cmd.equalsIgnoreCase("request head part of ancient key")) {
                if (CreateNewItem.checkNewItem(pc, 40537, 1) < 1) {
                    isCloseList = true;
                } else {
                    CreateNewItem.createNewItem(pc, 40537, 1, 40534, 1);
                    pc.getQuest().set_step(KnightLv45_1.QUEST.get_id(), 4);
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk3"));
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
