package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class Skill_SpellbookLv6 extends ItemExecutor {
    private Skill_SpellbookLv6() {
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv6();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null) {
            int itemId = item.getItemId();
            int skillid = 0;
            int attribute = 0;
            if (itemId == 40186) {
                skillid = 41;
                attribute = 2;
            } else if (itemId == 40187) {
                skillid = 42;
                attribute = 0;
            } else if (itemId == 40188) {
                skillid = 43;
                attribute = 0;
            } else if (itemId == 40189) {
                skillid = 44;
                attribute = 1;
            } else if (itemId == 40190) {
                skillid = 45;
                attribute = 0;
            } else if (itemId == 40191) {
                skillid = 46;
                attribute = 0;
            } else if (itemId == 40192) {
                skillid = 47;
                attribute = 2;
            } else if (itemId == 40193) {
                skillid = 48;
                attribute = 0;
            }
            Skill_Check.check(pc, item, skillid, 6, attribute);
        }
    }
}
