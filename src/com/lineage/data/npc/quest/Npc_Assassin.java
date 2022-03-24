package com.lineage.data.npc.quest;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv45_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Assassin extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Assassin.class);

    private Npc_Assassin() {
    }

    public static NpcExecutor get() {
        return new Npc_Assassin();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        boolean isTak = false;
        try {
            if (pc.getTempCharGfx() == 3634) {
                isTak = true;
            }
            if (!isTak) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin4"));
            } else if (pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));
            } else if (pc.isKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));
            } else if (pc.isElf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));
            } else if (pc.isDarkelf()) {
                if (pc.getQuest().isEnd(DarkElfLv45_1.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));
                } else if (pc.getLevel() >= DarkElfLv45_1.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(DarkElfLv45_1.QUEST.get_id())) {
                        case 1:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin1"));
                            return;
                        default:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));
                }
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));
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
                    case 1:
                        if (cmd.equalsIgnoreCase("quest 18 assassin2")) {
                            pc.getQuest().set_step(DarkElfLv45_1.QUEST.get_id(), 2);
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin2"));
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
