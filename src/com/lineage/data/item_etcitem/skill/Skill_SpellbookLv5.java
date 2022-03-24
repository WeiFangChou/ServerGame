package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class Skill_SpellbookLv5 extends ItemExecutor {
    private Skill_SpellbookLv5() {
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv5();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null) {
            int itemId = item.getItemId();
            int skillid = 0;
            int attribute = 0;
            if (itemId == 40178) {
                skillid = 33;
                attribute = 2;
            } else if (itemId == 40179) {
                skillid = 34;
                attribute = 1;
            } else if (itemId == 40180) {
                skillid = 35;
                attribute = 1;
            } else if (itemId == 40181) {
                skillid = 36;
                attribute = 0;
            } else if (itemId == 40182) {
                skillid = 37;
                attribute = 1;
            } else if (itemId == 40183) {
                skillid = 38;
                attribute = 0;
            } else if (itemId == 40184) {
                skillid = 39;
                attribute = 0;
            } else if (itemId == 40185) {
                skillid = 40;
                attribute = 0;
            }
            Skill_Check.check(pc, item, skillid, 5, attribute);
        }
    }
}
