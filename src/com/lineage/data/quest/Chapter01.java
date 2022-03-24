package com.lineage.data.quest;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Chapter01 extends QuestExecutor {
    public static final int MAPID = 9000;
    public static L1Quest QUEST = null;
    private static final String _html = "q_cha1_1";
    private static final Log _log = LogFactory.getLog(Chapter01.class);

    private Chapter01() {
    }

    public static QuestExecutor get() {
        return new Chapter01();
    }

    @Override // com.lineage.data.executor.QuestExecutor
    public void execute(L1Quest quest) {
        try {
            QUEST = quest;
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
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.QuestExecutor
    public void endQuest(L1PcInstance pc) {
        try {
            pc.sendPackets(new S_ServerMessage("\\fT" + QUEST.get_questname() + "任務結束！"));
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
