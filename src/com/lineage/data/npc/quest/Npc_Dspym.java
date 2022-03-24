package com.lineage.data.npc.quest;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.WizardLv50_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Dspym extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Dspym.class);

    private Npc_Dspym() {
    }

    public static NpcExecutor get() {
        return new Npc_Dspym();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            npc.onTalkAction(pc);
            if (pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym5"));
            } else if (pc.isKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym5"));
            } else if (pc.isElf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym5"));
            } else if (pc.isWizard()) {
                if (pc.getQuest().isEnd(WizardLv50_1.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym4"));
                } else if (pc.getLevel() >= WizardLv50_1.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(WizardLv50_1.QUEST.get_id())) {
                        case 0:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym5"));
                            return;
                        case 1:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym1"));
                            return;
                        default:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym3"));
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym5"));
                }
            } else if (pc.isDarkelf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym5"));
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym5"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym5"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym5"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (npc.getMaster() == null) {
            if (!pc.isWizard()) {
                isCloseList = true;
            } else if (pc.getQuest().isEnd(WizardLv50_1.QUEST.get_id())) {
                return;
            } else {
                if (cmd.equalsIgnoreCase("quest 27 dspym2")) {
                    pc.getQuest().set_step(WizardLv50_1.QUEST.get_id(), 2);
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym2"));
                }
            }
            if (isCloseList) {
                pc.sendPackets(new S_CloseList(pc.getId()));
            }
        }
    }
}
