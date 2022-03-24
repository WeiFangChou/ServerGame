package com.lineage.server.model.Instance;

import com.lineage.data.quest.EWLv40_1;
import com.lineage.data.quest.ElfLv50_1;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1Npc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1QuestInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1QuestInstance.class);
    private static final long serialVersionUID = 1;

    public L1QuestInstance(L1Npc template) {
        super(template);
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onNpcAI() {
        if (!isAiRunning()) {
            switch (getNpcTemplate().get_npcId()) {
                case EWLv40_1._roi2id:
                case 71075:
                case ElfLv50_1._npcId:
                case EWLv40_1._roiid:
                    return;
                default:
                    setActived(false);
                    startAI();
                    return;
            }
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance pc) {
        try {
            L1AttackMode attack = new L1AttackPc(pc, this);
            attack.action();
            attack.commit();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onTalkAction(L1PcInstance pc) {
        int npcId = getNpcTemplate().get_npcId();
        setHeading(targetDirection(pc.getX(), pc.getY()));
        broadcastPacketAll(new S_ChangeHeading(this));
        if (npcId == 71062) {
            if (pc.getQuest().get_step(31) == 2) {
                pc.sendPackets(new S_NPCTalkReturn(getId(), "kamit1b"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(getId(), "kamit1"));
            }
        } else if (npcId == 71075) {
            if (pc.getQuest().get_step(34) == 1) {
                pc.sendPackets(new S_NPCTalkReturn(getId(), "llizard1b"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(getId(), "llizard1a"));
            }
        }
        set_stop_time(10);
        setRest(true);
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onFinalAction(L1PcInstance pc, String action) {
        if (action.equalsIgnoreCase("start")) {
            int npcId = getNpcTemplate().get_npcId();
            if (npcId == 71062 && pc.getQuest().get_step(31) == 2) {
                new L1FollowerInstance(NpcTable.get().getTemplate(71062), this, pc);
                pc.sendPackets(new S_NPCTalkReturn(getId(), ""));
            } else if (npcId == 71075 && pc.getQuest().get_step(34) == 1) {
                new L1FollowerInstance(NpcTable.get().getTemplate(71075), this, pc);
                pc.sendPackets(new S_NPCTalkReturn(getId(), ""));
            }
        }
    }
}
