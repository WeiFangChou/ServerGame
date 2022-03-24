package com.lineage.data.item_etcitem.shop;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_PacketBoxHpMsg;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import java.util.Random;

public class Power_HPMPX extends ItemExecutor {
    private Power_HPMPX() {
    }

    public static ItemExecutor get() {
        return new Power_HPMPX();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        boolean isError = false;
        L1ItemInstance adena = pc.getInventory().checkItemX(L1ItemId.ADENA, 7000);
        if (adena != null) {
            pc.getInventory().removeItem(adena, 7000);
        } else {
            isError = true;
        }
        if (isError) {
            pc.sendPackets(new S_ServerMessage((int) L1SkillId.SHOCK_SKIN));
        } else if (L1BuffUtil.stopPotion(pc)) {
            L1BuffUtil.cancelAbsoluteBarrier(pc);
            Random random = new Random();
            pc.sendPacketsX8(new S_SkillSound(pc.getId(), 197));
            pc.sendPacketsX8(new S_SkillSound(pc.getId(), 190));
            int healMp = random.nextInt(50) + 25;
            int healHp = random.nextInt(30) + 10;
            if (pc.get_up_hp_potion() > 0) {
                healHp += (pc.get_up_hp_potion() * healHp) / 100;
            }
            if (pc.hasSkillEffect(173)) {
                healHp >>= 1;
                healMp >>= 1;
            }
            if (pc.hasSkillEffect(L1SkillId.ADLV80_2_2)) {
                healHp >>= 1;
                healMp >>= 1;
            }
            if (pc.hasSkillEffect(L1SkillId.ADLV80_2_1)) {
                healHp *= -1;
                healMp *= -1;
            }
            if (healHp > 0) {
                pc.sendPackets(new S_PacketBoxHpMsg());
            }
            pc.setCurrentMp(pc.getCurrentMp() + healMp);
            pc.setCurrentHp(pc.getCurrentHp() + healHp);
        }
    }
}
