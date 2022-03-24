package com.lineage.data.item_weapon;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class KenRauhelBaton extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(KenRauhelBaton.class);

    private KenRauhelBaton() {
    }

    public static ItemExecutor get() {
        return new KenRauhelBaton();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        try {
            switch (data[0]) {
                case 0:
                    if (pc.hasSkillEffect(L1SkillId.I_LV30)) {
                        pc.removeSkillEffect(L1SkillId.I_LV30);
                        return;
                    }
                    return;
                case 1:
                    pc.setSkillEffect(L1SkillId.I_LV30, -1);
                    return;
                default:
                    return;
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
