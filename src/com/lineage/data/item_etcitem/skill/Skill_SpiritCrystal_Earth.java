package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_SpiritCrystal_Earth extends ItemExecutor {
    private Skill_SpiritCrystal_Earth() {
    }

    public static ItemExecutor get() {
        return new Skill_SpiritCrystal_Earth();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null) {
            if (!pc.isElf()) {
                pc.sendPackets(new S_ServerMessage(79));
            } else if (pc.getElfAttr() != 1) {
                pc.sendPackets(new S_ServerMessage(684));
            } else {
                int itemId = item.getItemId();
                int skillid = 0;
                int magicLv = 0;
                if (itemId == 40247) {
                    skillid = 151;
                    magicLv = 13;
                } else if (itemId == 40248) {
                    skillid = L1SkillId.ENTANGLE;
                    magicLv = 13;
                } else if (itemId == 40249) {
                    skillid = 157;
                    magicLv = 14;
                } else if (itemId == 40250) {
                    skillid = 159;
                    magicLv = 14;
                } else if (itemId == 40251) {
                    skillid = L1SkillId.IRON_SKIN;
                    magicLv = 15;
                } else if (itemId == 40252) {
                    skillid = L1SkillId.EXOTIC_VITALIZE;
                    magicLv = 15;
                }
                Skill_Check.check(pc, item, skillid, magicLv, 3);
            }
        }
    }
}
