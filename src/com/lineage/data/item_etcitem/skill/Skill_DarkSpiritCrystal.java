package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_DarkSpiritCrystal extends ItemExecutor {
    private Skill_DarkSpiritCrystal() {
    }

    public static ItemExecutor get() {
        return new Skill_DarkSpiritCrystal();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null) {
            if (!pc.isDarkelf()) {
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }
            int itemId = item.getItemId();
            int skillid = 0;
            int magicLv = 0;
            if (itemId == 40265) {
                skillid = 97;
                magicLv = 41;
            } else if (itemId == 40266) {
                skillid = 98;
                magicLv = 41;
            } else if (itemId == 40267) {
                skillid = 99;
                magicLv = 41;
            } else if (itemId == 40268) {
                skillid = 100;
                magicLv = 41;
            } else if (itemId == 40269) {
                skillid = 109;
                magicLv = 41;
            } else if (itemId == 40270) {
                skillid = 101;
                magicLv = 42;
            } else if (itemId == 40271) {
                skillid = L1SkillId.BURNING_SPIRIT;
                magicLv = 42;
            } else if (itemId == 40272) {
                skillid = 103;
                magicLv = 42;
            } else if (itemId == 40273) {
                skillid = 104;
                magicLv = 42;
            } else if (itemId == 40274) {
                skillid = 110;
                magicLv = 42;
            } else if (itemId == 40275) {
                skillid = 105;
                magicLv = 43;
            } else if (itemId == 40276) {
                skillid = 106;
                magicLv = 43;
            } else if (itemId == 40277) {
                skillid = 107;
                magicLv = 43;
            } else if (itemId == 40278) {
                skillid = L1SkillId.FINAL_BURN;
                magicLv = 43;
            } else if (itemId == 40279) {
                skillid = 111;
                magicLv = 43;
            } else if (itemId == 80038) {
                skillid = 112;
                magicLv = 44;
            }
            Skill_Check.check(pc, item, skillid, magicLv, 6);
        }
    }
}
