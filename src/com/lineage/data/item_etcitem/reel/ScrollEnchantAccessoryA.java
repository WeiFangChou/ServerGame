package com.lineage.data.item_etcitem.reel;

import com.lineage.data.cmd.EnchantArmor;
import com.lineage.data.cmd.EnchantExecutor;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.util.Random;

public class ScrollEnchantAccessoryA extends ItemExecutor {
    private ScrollEnchantAccessoryA() {
    }

    public static ItemExecutor get() {
        return new ScrollEnchantAccessoryA();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        L1ItemInstance tgItem = pc.getInventory().getItem(data[0]);
        if (tgItem != null) {
            int safe_enchant = tgItem.getItem().get_safeenchant();
            boolean isErr = false;
            switch (tgItem.getItem().getUseType()) {
                case 23:
                case 24:
                case 37:
                case 40:
                    if (tgItem.getItem().get_greater() == 3) {
                        isErr = true;
                    }
                    if (safe_enchant < 0) {
                        isErr = true;
                        break;
                    }
                    break;
                default:
                    isErr = true;
                    break;
            }
            if (tgItem.getBless() >= 128) {
                isErr = true;
            }
            if (isErr) {
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }
            int enchant_level = tgItem.getEnchantLevel();
            if (enchant_level >= 8) {
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }
            EnchantExecutor enchantExecutor = new EnchantArmor();
            pc.getInventory().removeItem(item, 1);
            boolean isEnchant = false;
            Random random = new Random();
            int rand = L1SkillId.STATUS_BRAVE;
            if (enchant_level > -6 && enchant_level <= 5) {
                rand = L1SkillId.STATUS_BRAVE;
            } else if (enchant_level >= 6 && enchant_level <= 9) {
                rand = 250;
            } else if (enchant_level >= 10 && enchant_level <= 11) {
                rand = 50;
            } else if (enchant_level >= 12 && enchant_level <= 13) {
                rand = 10;
            } else if (enchant_level >= 14) {
                rand = 5;
            }
            if (random.nextInt(L1SkillId.STATUS_BRAVE) < rand) {
                isEnchant = true;
            }
            if (isEnchant) {
                enchantExecutor.successEnchant(pc, tgItem, 1);
            } else {
                enchantExecutor.successEnchant(pc, tgItem, 0);
            }
        }
    }
}
