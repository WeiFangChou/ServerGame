package com.lineage.data.item_etcitem.shop;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_SkillSound;
import java.util.Random;

public class Power_MP extends ItemExecutor {
    private Power_MP() {
    }

    public static ItemExecutor get() {
        return new Power_MP();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (L1BuffUtil.stopPotion(pc) && pc.getInventory().removeItem(item, 1) == 1) {
            L1BuffUtil.cancelAbsoluteBarrier(pc);
            Random random = new Random();
            pc.sendPacketsX8(new S_SkillSound(pc.getId(), 190));
            int healMp = random.nextInt(30) + 20;
            if (pc.hasSkillEffect(173)) {
                healMp >>= 1;
            }
            if (pc.hasSkillEffect(L1SkillId.ADLV80_2_2)) {
                healMp >>= 1;
            }
            if (pc.hasSkillEffect(L1SkillId.ADLV80_2_1)) {
                healMp *= -1;
            }
            pc.setCurrentMp(pc.getCurrentMp() + healMp);
        }
    }
}
