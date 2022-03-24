package com.lineage.server.model.Instance;

import com.lineage.server.model.L1AttackPc;
import com.lineage.server.templates.L1Npc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1CnInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1CnInstance.class);
    private static final long serialVersionUID = 1;

    public L1CnInstance(L1Npc template) {
        super(template);
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance pc) {
        try {
            new L1AttackPc(pc, this).action();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onNpcAI() {
    }

    @Override // com.lineage.server.model.L1Object
    public void onTalkAction(L1PcInstance player) {
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onFinalAction(L1PcInstance player, String action) {
    }

    public void doFinalAction(L1PcInstance player) {
    }
}
