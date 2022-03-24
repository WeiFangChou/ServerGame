package com.lineage.data.quest;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1Quest;
import com.lineage.server.timecontroller.quest.AD80_2_Timer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ADLv80_2 extends QuestExecutor {
    public static final int MAPID = 1011;
    public static L1Quest QUEST = null;
    private static final String _html = "y_q_ad80_2";
    private static final Log _log = LogFactory.getLog(ADLv80_2.class);

    private ADLv80_2() {
    }

    public static QuestExecutor get() {
        return new ADLv80_2();
    }

    @Override // com.lineage.data.executor.QuestExecutor
    public void execute(L1Quest quest) {
        try {
            QUEST = quest;
            new AD80_2_Timer().start();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.QuestExecutor
    public void startQuest(L1PcInstance pc) {
        try {
            if (!QUEST.check(pc)) {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "y_q_not2"));
            } else if (pc.getLevel() < QUEST.get_questlevel()) {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "y_q_not1"));
            } else if (pc.getQuest().get_step(QUEST.get_id()) != 1) {
                pc.getQuest().set_step(QUEST.get_id(), 1);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.QuestExecutor
    public void endQuest(L1PcInstance pc) {
        try {
            if (!pc.getQuest().isEnd(QUEST.get_id())) {
                pc.getQuest().set_end(QUEST.get_id());
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.QuestExecutor
    public void showQuest(L1PcInstance pc) {
        if (_html != null) {
            try {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), _html));
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    @Override // com.lineage.data.executor.QuestExecutor
    public void stopQuest(L1PcInstance pc) {
    }
}
