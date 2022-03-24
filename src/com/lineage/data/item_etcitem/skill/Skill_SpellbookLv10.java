package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_SpellbookLv10 extends ItemExecutor {
    private Skill_SpellbookLv10() {
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv10();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null) {
            if (!pc.isWizard()) {
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }
            int itemId = item.getItemId();
            int skillid = 0;
            int attribute = 0;
            if (itemId == 40218) {
                skillid = 73;
                attribute = 0;
            } else if (itemId == 40219) {
                skillid = 74;
                attribute = 0;
            } else if (itemId == 40220) {
                skillid = 75;
                attribute = 1;
            } else if (itemId == 40221) {
                skillid = 76;
                attribute = 2;
            } else if (itemId == 40222) {
                skillid = 77;
                attribute = 1;
            } else if (itemId == 40223) {
                skillid = 78;
                attribute = 0;
            } else if (itemId == 40224) {
                skillid = 79;
                attribute = 0;
            } else if (itemId == 40225) {
                skillid = 80;
                attribute = 2;
            }
            Skill_Check.check(pc, item, skillid, 10, attribute);
        }
    }
}
