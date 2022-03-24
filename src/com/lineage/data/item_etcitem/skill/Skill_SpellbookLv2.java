package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class Skill_SpellbookLv2 extends ItemExecutor {
    private Skill_SpellbookLv2() {
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv2();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null) {
            int itemId = item.getItemId();
            int skillid = 0;
            int attribute = 0;
            if (itemId == 45008) {
                skillid = 9;
                attribute = 1;
            } else if (itemId == 45009) {
                skillid = 10;
                attribute = 2;
            } else if (itemId == 45010) {
                skillid = 11;
                attribute = 2;
            } else if (itemId == 45011) {
                skillid = 12;
                attribute = 0;
            } else if (itemId == 45012) {
                skillid = 13;
                attribute = 0;
            } else if (itemId == 45013) {
                skillid = 14;
                attribute = 0;
            } else if (itemId == 45015) {
                skillid = 15;
                attribute = 0;
            } else if (itemId == 45014) {
                skillid = 16;
                attribute = 0;
            }
            Skill_Check.check(pc, item, skillid, 2, attribute);
        }
    }
}
