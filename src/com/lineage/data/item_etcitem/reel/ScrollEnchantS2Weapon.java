package com.lineage.data.item_etcitem.reel;

import com.lineage.config.ConfigRate;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.util.Random;

public class ScrollEnchantS2Weapon extends ItemExecutor {
    private ScrollEnchantS2Weapon() {
    }

    public static ItemExecutor get() {
        return new ScrollEnchantS2Weapon();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        L1ItemInstance tgItem = pc.getInventory().getItem(data[0]);
        if (tgItem != null) {
            int oldAttrEnchantKind = tgItem.getAttrEnchantKind();
            int oldAttrEnchantLevel = tgItem.getAttrEnchantLevel();
            boolean isErr = false;
            switch (tgItem.getItem().getUseType()) {
                case 1:
                    if (oldAttrEnchantKind == 32 && oldAttrEnchantLevel >= 3) {
                        isErr = true;
                        break;
                    }
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
            pc.getInventory().removeItem(item, 1);
            if (ConfigRate.ATTR_ENCHANT_CHANCE >= new Random().nextInt(100) + 1) {
                pc.sendPackets(new S_ServerMessage(1410, tgItem.getLogName()));
                int newAttrEnchantLevel = oldAttrEnchantLevel + 1;
                if (oldAttrEnchantKind != 32) {
                    newAttrEnchantLevel = 1;
                }
                tgItem.setAttrEnchantKind(32);
                pc.getInventory().updateItem(tgItem, 1024);
                pc.getInventory().saveItem(tgItem, 1024);
                tgItem.setAttrEnchantLevel(newAttrEnchantLevel);
                pc.getInventory().updateItem(tgItem, L1PcInventory.COL_ATTR_ENCHANT_LEVEL);
                pc.getInventory().saveItem(tgItem, L1PcInventory.COL_ATTR_ENCHANT_LEVEL);
                return;
            }
            pc.sendPackets(new S_ServerMessage(1411, tgItem.getLogName()));
        }
    }
}
