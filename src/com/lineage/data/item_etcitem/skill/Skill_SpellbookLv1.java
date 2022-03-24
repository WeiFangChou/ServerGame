package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class Skill_SpellbookLv1 extends ItemExecutor {
    private Skill_SpellbookLv1() {
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv1();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null) {
            int itemId = item.getItemId();
            int skillid = 0;
            int attribute = 0;
            if (itemId == 45000) {
                skillid = 1;
                attribute = 1;
            } else if (itemId == 45001) {
                skillid = 2;
                attribute = 0;
            } else if (itemId == 45002) {
                skillid = 3;
                attribute = 0;
            } else if (itemId == 45003) {
                skillid = 4;
                attribute = 0;
            } else if (itemId == 45004) {
                skillid = 5;
                attribute = 0;
            } else if (itemId == 45005) {
                skillid = 6;
                attribute = 0;
            } else if (itemId == 45006) {
                skillid = 7;
                attribute = 0;
            } else if (itemId == 45007) {
                skillid = 8;
                attribute = 0;
            }
            Skill_Check.check(pc, item, skillid, 1, attribute);
        }
    }
}
