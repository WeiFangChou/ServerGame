package com.lineage.data.quest;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;
import com.lineage.server.timecontroller.quest.DE50A_Timer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DarkElfLv50_1 extends QuestExecutor {
    public static final int MAPID = 306;
    public static L1Quest QUEST = null;
    public static final int _endiaId = 71094;
    private static final String _html = "y_q_d50_1";
    private static final Log _log = LogFactory.getLog(DarkElfLv50_1.class);
    public static final int _tgid = 70811;

    private DarkElfLv50_1() {
    }

    public static QuestExecutor get() {
        return new DarkElfLv50_1();
    }

    @Override // com.lineage.data.executor.QuestExecutor
    public void execute(L1Quest quest) {
        try {
            QUEST = quest;
            new DE50A_Timer().start();
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
                pc.sendPackets(new S_ServerMessage("\\fT" + QUEST.get_questname() + "任務完成！"));
                if (QUEST.is_del()) {
                    pc.sendPackets(new S_ServerMessage("\\fT請注意這個任務可以重複執行，需要重複任務，請在任務管理員中執行解除。"));
                } else {
                    new S_ServerMessage("\\fR請注意這個任務不能重複執行，無法在任務管理員中解除執行。");
                }
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
