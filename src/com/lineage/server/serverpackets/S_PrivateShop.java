package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1PrivateShopBuyList;
import com.lineage.server.templates.L1PrivateShopSellList;
import com.lineage.server.world.World;
import java.util.ArrayList;

public class S_PrivateShop extends ServerBasePacket {
    public S_PrivateShop(L1PcInstance pc, int objectId, int type) {
        if (World.get().findObject(objectId) instanceof L1PcInstance) {
            isPc(pc, objectId, type);
        }
    }

    private void isPc(L1PcInstance pc, int objectId, int type) {
        L1PcInstance shopPc = (L1PcInstance) World.get().findObject(objectId);
        if (shopPc != null) {
            writeC(190);
            writeC(type);
            writeD(objectId);
            if (type == 0) {
                ArrayList<?> list = shopPc.getSellList();
                if (list.isEmpty()) {
                    writeH(0);
                    return;
                }
                int size = list.size();
                if (size <= 0) {
                    writeH(0);
                    return;
                }
                pc.setPartnersPrivateShopItemCount(size);
                writeH(size);
                for (int i = 0; i < size; i++) {
                    L1PrivateShopSellList pssl = (L1PrivateShopSellList) list.get(i);
                    int itemObjectId = pssl.getItemObjectId();
                    int count = pssl.getSellTotalCount() - pssl.getSellCount();
                    int price = pssl.getSellPrice();
                    L1ItemInstance item = shopPc.getInventory().getItem(itemObjectId);
                    if (item != null) {
                        writeC(i);
                        writeC(item.getBless());
                        writeH(item.getItem().getGfxId());
                        writeD(count);
                        writeD(price);
                        writeS(item.getNumberedViewName((long) count));
                        writeC(0);
                    }
                }
            } else if (type == 1) {
                ArrayList<?> list2 = shopPc.getBuyList();
                if (list2.isEmpty()) {
                    writeH(0);
                    return;
                }
                int size2 = list2.size();
                if (size2 <= 0) {
                    writeH(0);
                    return;
                }
                writeH(size2);
                for (int i2 = 0; i2 < size2; i2++) {
                    L1PrivateShopBuyList psbl = (L1PrivateShopBuyList) list2.get(i2);
                    int itemObjectId2 = psbl.getItemObjectId();
                    int count2 = psbl.getBuyTotalCount();
                    int price2 = psbl.getBuyPrice();
                    L1ItemInstance item2 = shopPc.getInventory().getItem(itemObjectId2);
                    for (L1ItemInstance pcItem : pc.getInventory().getItems()) {
                        if (item2.getItemId() == pcItem.getItemId() && item2.getEnchantLevel() == pcItem.getEnchantLevel()) {
                            writeC(i2);
                            writeD(pcItem.getId());
                            writeD(count2);
                            writeD(price2);
                        }
                    }
                }
            }
        }
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        return getBytes();
    }
}
