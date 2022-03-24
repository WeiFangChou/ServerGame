package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_DragonKnigh extends ItemExecutor {
    private Skill_DragonKnigh() {
    }

    public static ItemExecutor get() {
        return new Skill_DragonKnigh();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null) {
            if (!pc.isDragonKnight()) {
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }
            int itemId = item.getItemId();
            int skillid = 0;
            int magicLv = 0;
            if (itemId == 49102) {
                skillid = L1SkillId.DRAGON_SKIN;
                magicLv = 51;
            } else if (itemId == 49103) {
                skillid = 182;
                magicLv = 51;
            } else if (itemId == 49104) {
                skillid = L1SkillId.GUARD_BRAKE;
                magicLv = 51;
            } else if (itemId == 49105) {
                skillid = 184;
                magicLv = 51;
            } else if (itemId == 49106) {
                skillid = 185;
                magicLv = 52;
            } else if (itemId == 49107) {
                skillid = L1SkillId.BLOODLUST;
                magicLv = 52;
            } else if (itemId == 49108) {
                skillid = L1SkillId.FOE_SLAYER;
                magicLv = 52;
            } else if (itemId == 49109) {
                skillid = 188;
                magicLv = 52;
            } else if (itemId == 49110) {
                skillid = L1SkillId.SHOCK_SKIN;
                magicLv = 52;
            } else if (itemId == 49111) {
                skillid = 190;
                magicLv = 52;
            } else if (itemId == 49112) {
                skillid = L1SkillId.MORTAL_BODY;
                magicLv = 52;
            } else if (itemId == 49113) {
                skillid = 192;
                magicLv = 52;
            } else if (itemId == 49114) {
                skillid = 193;
                magicLv = 53;
            } else if (itemId == 49115) {
                skillid = 194;
                magicLv = 53;
            } else if (itemId == 49116) {
                skillid = 195;
                magicLv = 53;
            }
            Skill_Check.check(pc, item, skillid, magicLv, 6);
        }
    }
}
