package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_SpiritCrystal_Fire extends ItemExecutor {
    private Skill_SpiritCrystal_Fire() {
    }

    public static ItemExecutor get() {
        return new Skill_SpiritCrystal_Fire();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null) {
            if (!pc.isElf()) {
                pc.sendPackets(new S_ServerMessage(79));
            } else if (pc.getElfAttr() != 2) {
                pc.sendPackets(new S_ServerMessage(684));
            } else {
                int itemId = item.getItemId();
                int skillid = 0;
                int magicLv = 0;
                if (itemId == 40256) {
                    skillid = 148;
                    magicLv = 13;
                } else if (itemId == 40257) {
                    skillid = 155;
                    magicLv = 14;
                } else if (itemId == 40258) {
                    skillid = L1SkillId.BURNING_WEAPON;
                    magicLv = 15;
                } else if (itemId == 40259) {
                    skillid = L1SkillId.ELEMENTAL_FIRE;
                    magicLv = 15;
                } else if (itemId == 41149) {
                    skillid = L1SkillId.SOUL_OF_FLAME;
                    magicLv = 15;
                } else if (itemId == 41150) {
                    skillid = L1SkillId.ADDITIONAL_FIRE;
                    magicLv = 15;
                }
                Skill_Check.check(pc, item, skillid, magicLv, 3);
            }
        }
    }
}
