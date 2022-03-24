package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_Illusionist extends ItemExecutor {
    private Skill_Illusionist() {
    }

    public static ItemExecutor get() {
        return new Skill_Illusionist();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null) {
            if (!pc.isIllusionist()) {
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }
            int itemId = item.getItemId();
            int skillid = 0;
            int magicLv = 0;
            if (itemId == 49117) {
                skillid = 201;
                magicLv = 61;
            } else if (itemId == 49118) {
                skillid = 202;
                magicLv = 61;
            } else if (itemId == 49119) {
                skillid = 203;
                magicLv = 61;
            } else if (itemId == 49120) {
                skillid = 204;
                magicLv = 61;
            } else if (itemId == 49121) {
                skillid = L1SkillId.CUBE_IGNITION;
                magicLv = 61;
            } else if (itemId == 49122) {
                skillid = L1SkillId.CONCENTRATION;
                magicLv = 61;
            } else if (itemId == 49123) {
                skillid = 207;
                magicLv = 61;
            } else if (itemId == 49124) {
                skillid = 208;
                magicLv = 61;
            } else if (itemId == 49125) {
                skillid = 209;
                magicLv = 62;
            } else if (itemId == 49126) {
                skillid = 210;
                magicLv = 62;
            } else if (itemId == 49127) {
                skillid = 211;
                magicLv = 62;
            } else if (itemId == 49128) {
                skillid = 212;
                magicLv = 62;
            } else if (itemId == 49129) {
                skillid = 213;
                magicLv = 63;
            } else if (itemId == 49130) {
                skillid = L1SkillId.ILLUSION_DIA_GOLEM;
                magicLv = 63;
            } else if (itemId == 49131) {
                skillid = L1SkillId.CUBE_SHOCK;
                magicLv = 63;
            } else if (itemId == 49132) {
                skillid = 216;
                magicLv = 63;
            } else if (itemId == 49133) {
                skillid = 217;
                magicLv = 64;
            } else if (itemId == 49134) {
                skillid = 218;
                magicLv = 64;
            } else if (itemId == 49135) {
                skillid = L1SkillId.ILLUSION_AVATAR;
                magicLv = 64;
            } else if (itemId == 49136) {
                skillid = 220;
                magicLv = 64;
            }
            Skill_Check.check(pc, item, skillid, magicLv, 6);
        }
    }
}
