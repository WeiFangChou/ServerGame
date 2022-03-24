package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class Skill_SpellbookLv4 extends ItemExecutor {
    private Skill_SpellbookLv4() {
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv4();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null) {
            int itemId = item.getItemId();
            int skillid = 0;
            int attribute = 0;
            if (itemId == 40170) {
                skillid = 25;
                attribute = 0;
            } else if (itemId == 40171) {
                skillid = 26;
                attribute = 1;
            } else if (itemId == 40172) {
                skillid = 27;
                attribute = 2;
            } else if (itemId == 40173) {
                skillid = 28;
                attribute = 2;
            } else if (itemId == 40174) {
                skillid = 29;
                attribute = 0;
            } else if (itemId == 40177) {
                skillid = 30;
                attribute = 0;
            } else if (itemId == 40175) {
                skillid = 31;
                attribute = 1;
            } else if (itemId == 40176) {
                skillid = 32;
                attribute = 0;
            }
            Skill_Check.check(pc, item, skillid, 4, attribute);
        }
    }
}
