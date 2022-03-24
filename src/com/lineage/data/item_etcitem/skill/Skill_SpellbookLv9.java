package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_SpellbookLv9 extends ItemExecutor {
    private Skill_SpellbookLv9() {
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv9();
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
            if (itemId == 40210) {
                skillid = 65;
                attribute = 0;
            } else if (itemId == 40211) {
                skillid = 66;
                attribute = 2;
            } else if (itemId == 40212) {
                skillid = 67;
                attribute = 2;
            } else if (itemId == 40213) {
                skillid = 68;
                attribute = 1;
            } else if (itemId == 40214) {
                skillid = 69;
                attribute = 0;
            } else if (itemId == 40215) {
                skillid = 70;
                attribute = 0;
            } else if (itemId == 40216) {
                skillid = 71;
                attribute = 0;
            } else if (itemId == 40217) {
                skillid = 72;
                attribute = 0;
            }
            Skill_Check.check(pc, item, skillid, 9, attribute);
        }
    }
}
