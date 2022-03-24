package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_SpellbookCrown extends ItemExecutor {
    private Skill_SpellbookCrown() {
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookCrown();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null) {
            if (!pc.isCrown()) {
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }
            int itemId = item.getItemId();
            int skillid = 0;
            int magicLv = 0;
            if (itemId == 40226) {
                skillid = 113;
                magicLv = 21;
            } else if (itemId == 40228) {
                skillid = 116;
                magicLv = 22;
            } else if (itemId == 40227) {
                skillid = L1SkillId.GLOWING_AURA;
                magicLv = 23;
            } else if (itemId == 40231) {
                skillid = L1SkillId.RUN_CLAN;
                magicLv = 24;
            } else if (itemId == 40230) {
                skillid = 117;
                magicLv = 25;
            } else if (itemId == 40229) {
                skillid = 115;
                magicLv = 26;
            }
            Skill_Check.check(pc, item, skillid, magicLv, 4);
        }
    }
}
