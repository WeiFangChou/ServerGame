package com.lineage.data.item_etcitem.reel;

import com.lineage.data.cmd.EnchantArmor;
import com.lineage.data.cmd.EnchantExecutor;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1ItemUpdata;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.util.Random;

public class ScrollEnchantAccessory extends ItemExecutor {
    private ScrollEnchantAccessory() {
    }

    public static ItemExecutor get() {
        return new ScrollEnchantAccessory();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int enchant_level_tmp;
        int enchant_chance_armor;
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
            int randomELevel = enchantExecutor.randomELevel(tgItem, item.getBless());
            pc.getInventory().removeItem(item, 1);
            boolean isEnchant = true;
            if (enchant_level < -6) {
                isEnchant = false;
            } else if (enchant_level < safe_enchant) {
                isEnchant = true;
            } else {
                int rnd = new Random().nextInt(100) + 1;
                if (safe_enchant == 0) {
                    enchant_level_tmp = enchant_level + 2;
                } else {
                    enchant_level_tmp = enchant_level;
                }
                if (enchant_level >= 9) {
                    enchant_chance_armor = (int) L1ItemUpdata.enchant_armor_up9((double) enchant_level_tmp);
                } else {
                    enchant_chance_armor = (int) L1ItemUpdata.enchant_armor_dn9((double) enchant_level_tmp);
                }
                if (rnd < enchant_chance_armor) {
                    isEnchant = true;
                } else if (enchant_level < 9 || rnd >= enchant_chance_armor * 2) {
                    isEnchant = false;
                } else {
                    randomELevel = 0;
                }
            }
            if (randomELevel <= 0 && enchant_level > -6) {
                isEnchant = true;
            }
            if (isEnchant) {
                enchantExecutor.successEnchant(pc, tgItem, randomELevel);
            } else {
                enchantExecutor.failureEnchant(pc, tgItem);
            }
        }
    }
}
