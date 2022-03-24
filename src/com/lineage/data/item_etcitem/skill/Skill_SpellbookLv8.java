package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_SpellbookLv8 extends ItemExecutor {
    private Skill_SpellbookLv8() {
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv8();
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
            if (itemId == 40202) {
                skillid = 57;
                attribute = 1;
            } else if (itemId == 40203) {
                skillid = 58;
                attribute = 0;
            } else if (itemId == 40204) {
                skillid = 59;
                attribute = 2;
            } else if (itemId == 40205) {
                skillid = 60;
                attribute = 0;
            } else if (itemId == 40206) {
                skillid = 61;
                attribute = 1;
            } else if (itemId == 40207) {
                skillid = 62;
                attribute = 0;
            } else if (itemId == 40208) {
                skillid = 63;
                attribute = 0;
            } else if (itemId == 40209) {
                skillid = 64;
                attribute = 0;
            }
            Skill_Check.check(pc, item, skillid, 8, attribute);
        }
    }
}
