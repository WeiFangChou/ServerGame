package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_SpiritCrystal_Water extends ItemExecutor {
    private Skill_SpiritCrystal_Water() {
    }

    public static ItemExecutor get() {
        return new Skill_SpiritCrystal_Water();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null) {
            if (!pc.isElf()) {
                pc.sendPackets(new S_ServerMessage(79));
            } else if (pc.getElfAttr() != 4) {
                pc.sendPackets(new S_ServerMessage(684));
            } else {
                int itemId = item.getItemId();
                int skillid = 0;
                int magicLv = 0;
                if (itemId == 40253) {
                    skillid = 170;
                    magicLv = 13;
                } else if (itemId == 40254) {
                    skillid = L1SkillId.NATURES_TOUCH;
                    magicLv = 14;
                } else if (itemId == 41151) {
                    skillid = L1SkillId.AQUA_PROTECTER;
                    magicLv = 14;
                } else if (itemId == 40255) {
                    skillid = 164;
                    magicLv = 15;
                } else if (itemId == 80039) {
                    skillid = 165;
                    magicLv = 15;
                } else if (itemId == 41152) {
                    skillid = 173;
                    magicLv = 15;
                }
                Skill_Check.check(pc, item, skillid, magicLv, 3);
            }
        }
    }
}
