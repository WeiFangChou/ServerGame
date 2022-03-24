package com.lineage.data.item_etcitem.magicreel;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillUse;

public class ImmuntToHarm extends ItemExecutor {
    private ImmuntToHarm() {
    }

    public static ItemExecutor get() {
        return new ImmuntToHarm();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc != null && item != null && pc.getInventory().removeItem(item, 1) >= 1) {
            L1BuffUtil.cancelAbsoluteBarrier(pc);
            new L1SkillUse().handleCommands(pc, 68, pc.getId(), 0, 0, 0, 2);
        }
    }
}
