package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_SpiritCrystal_Wind extends ItemExecutor {
    private Skill_SpiritCrystal_Wind() {
    }

    public static ItemExecutor get() {
        return new Skill_SpiritCrystal_Wind();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null) {
            if (!pc.isElf()) {
                pc.sendPackets(new S_ServerMessage(79));
            } else if (pc.getElfAttr() != 8) {
                pc.sendPackets(new S_ServerMessage(684));
            } else {
                int itemId = item.getItemId();
                int skillid = 0;
                int magicLv = 0;
                if (itemId == 40260) {
                    skillid = 149;
                    magicLv = 13;
                } else if (itemId == 40261) {
                    skillid = 150;
                    magicLv = 13;
                } else if (itemId == 40262) {
                    skillid = L1SkillId.STORM_EYE;
                    magicLv = 14;
                } else if (itemId == 40263) {
                    skillid = 166;
                    magicLv = 15;
                } else if (itemId == 40264) {
                    skillid = 167;
                    magicLv = 15;
                } else if (itemId == 41153) {
                    skillid = 174;
                    magicLv = 15;
                }
                Skill_Check.check(pc, item, skillid, magicLv, 3);
            }
        }
    }
}
