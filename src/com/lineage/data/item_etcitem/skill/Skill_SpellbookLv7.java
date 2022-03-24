package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_SpellbookLv7 extends ItemExecutor {
    private Skill_SpellbookLv7() {
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv7();
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
            if (itemId == 40194) {
                skillid = 49;
                attribute = 1;
            } else if (itemId == 40195) {
                skillid = 50;
                attribute = 0;
            } else if (itemId == 40196) {
                skillid = 51;
                attribute = 2;
            } else if (itemId == 40197) {
                skillid = 52;
                attribute = 1;
            } else if (itemId == 40198) {
                skillid = 53;
                attribute = 0;
            } else if (itemId == 40199) {
                skillid = 54;
                attribute = 0;
            } else if (itemId == 40200) {
                skillid = 55;
                attribute = 0;
            } else if (itemId == 40201) {
                skillid = 56;
                attribute = 2;
            }
            Skill_Check.check(pc, item, skillid, 7, attribute);
        }
    }
}
