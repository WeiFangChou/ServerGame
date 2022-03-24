package com.lineage.server.model;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1Quest;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ActionShowHtml {
    private static final Log _log = LogFactory.getLog(L1ActionShowHtml.class);
    private final L1PcInstance _pc;

    public L1ActionShowHtml(L1PcInstance pc) {
        this._pc = pc;
    }

    public void showQuestMap(int page) {
        try {
            Map<Integer, L1Quest> list = this._pc.get_otherList().QUESTMAP;
            if (list != null) {
                int allpage = list.size() / 10;
                if (page > allpage || page < 0) {
                    page = 0;
                }
                if (list.size() % 10 != 0) {
                    allpage++;
                }
                this._pc.get_other().set_page(page);
                int showId = page * 10;
                StringBuilder stringBuilder = new StringBuilder();
                for (int key = showId; key < showId + 10; key++) {
                    L1Quest quest = list.get(Integer.valueOf(key));
                    if (quest != null) {
                        stringBuilder.append(String.valueOf(quest.get_questname()) + ",");
                    } else {
                        stringBuilder.append(" ,");
                    }
                }
                String[] clientStrAry = stringBuilder.toString().split(",");
                if (allpage == 1) {
                    this._pc.sendPackets(new S_NPCTalkReturn(this._pc.getId(), "y_qp0", clientStrAry));
                } else if (page < 1) {
                    this._pc.sendPackets(new S_NPCTalkReturn(this._pc.getId(), "y_qp1", clientStrAry));
                } else if (page >= allpage - 1) {
                    this._pc.sendPackets(new S_NPCTalkReturn(this._pc.getId(), "y_qp3", clientStrAry));
                } else {
                    this._pc.sendPackets(new S_NPCTalkReturn(this._pc.getId(), "y_qp2", clientStrAry));
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
