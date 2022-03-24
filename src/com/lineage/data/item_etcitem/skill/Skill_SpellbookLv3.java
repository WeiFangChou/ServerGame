package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class Skill_SpellbookLv3 extends ItemExecutor {
    private Skill_SpellbookLv3() {
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv3();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null) {
            int itemId = item.getItemId();
            int skillid = 0;
            int attribute = 0;
            if (itemId == 45016) {
                skillid = 17;
                attribute = 0;
            } else if (itemId == 45017) {
                skillid = 18;
                attribute = 1;
            } else if (itemId == 45018) {
                skillid = 19;
                attribute = 1;
            } else if (itemId == 45019) {
                skillid = 20;
                attribute = 2;
            } else if (itemId == 45020) {
                skillid = 21;
                attribute = 0;
            } else if (itemId == 45021) {
                skillid = 22;
                attribute = 0;
            } else if (itemId == 45022) {
                skillid = 23;
                attribute = 0;
            }
            Skill_Check.check(pc, item, skillid, 3, attribute);
        }
    }
}
