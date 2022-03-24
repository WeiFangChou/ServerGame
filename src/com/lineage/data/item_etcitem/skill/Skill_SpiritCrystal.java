package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_SpiritCrystal extends ItemExecutor {
    private Skill_SpiritCrystal() {
    }

    public static ItemExecutor get() {
        return new Skill_SpiritCrystal();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null) {
            if (!pc.isElf()) {
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }
            int itemId = item.getItemId();
            int skillid = 0;
            int magicLv = 0;
            if (itemId == 40232) {
                skillid = 129;
                magicLv = 11;
            } else if (itemId == 40233) {
                skillid = L1SkillId.BODY_TO_MIND;
                magicLv = 11;
            } else if (itemId == 40234) {
                skillid = 131;
                magicLv = 11;
            } else if (itemId == 40235) {
                skillid = 137;
                magicLv = 12;
            } else if (itemId == 40236) {
                skillid = 138;
                magicLv = 12;
            } else if (itemId == 40240) {
                skillid = L1SkillId.TRIPLE_ARROW;
                magicLv = 13;
            } else if (itemId == 40237) {
                skillid = 145;
                magicLv = 13;
            } else if (itemId == 40238) {
                skillid = L1SkillId.BLOODY_SOUL;
                magicLv = 13;
            } else if (itemId == 40239) {
                skillid = L1SkillId.ELEMENTAL_PROTECTION;
                magicLv = 13;
            } else if (itemId == 40241) {
                skillid = 133;
                magicLv = 14;
            } else if (itemId == 40242) {
                skillid = 153;
                magicLv = 14;
            } else if (itemId == 40243) {
                skillid = 154;
                magicLv = 14;
            } else if (itemId == 40246) {
                skillid = 134;
                magicLv = 15;
            } else if (itemId == 40244) {
                skillid = 161;
                magicLv = 15;
            } else if (itemId == 40245) {
                skillid = 162;
                magicLv = 15;
            }
            Skill_Check.check(pc, item, skillid, magicLv, 3);
        }
    }
}
