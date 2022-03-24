package com.lineage.server.serverpackets;

import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigRate;
import com.lineage.server.datatables.ShopTable;
import com.lineage.server.model.Instance.L1ItemStatus;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1TaxCalculator;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ShopItem;
import com.lineage.server.world.World;
import java.util.List;

public class S_ShopSellList extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ShopSellList(int objId) {
        writeC(OpcodesServer.S_OPCODE_SHOWSHOPBUYLIST);
        writeD(objId);
        L1Object npcObj = World.get().findObject(objId);
        if (!(npcObj instanceof L1NpcInstance)) {
            writeH(0);
            return;
        }
        int npcId = ((L1NpcInstance) npcObj).getNpcTemplate().get_npcId();
        L1TaxCalculator calc = new L1TaxCalculator(npcId);
        List<L1ShopItem> shopItems = ShopTable.get().get(npcId).getSellingItems();
        if (shopItems.size() <= 0) {
            writeH(0);
            return;
        }
        writeH(shopItems.size());
        for (int i = 0; i < shopItems.size(); i++) {
            L1ShopItem shopItem = shopItems.get(i);
            L1Item item = shopItem.getItem();
            int price = calc.layTax((int) (((double) shopItem.getPrice()) * ConfigRate.RATE_SHOP_SELLING_PRICE));
            int enchant = shopItem.getEnchantLevel();
            int daily = shopItem.getDailyBuyingCount();
            int level = shopItem.getlevel();
            int remain_time = shopItem.get_remain_time();
            writeD(i);
            writeH(shopItem.getItem().getGfxId());
            writeD(price);
            String s2 = "";
            String s3 = "";
            String s1 = enchant != 0 ? "+" + enchant : "";
            s2 = (level == 0 && daily == 0) ? s2 : "[" + level + "級 (" + daily + ")]";
            s3 = remain_time != 0 ? "[" + remain_time + " 天]" : s3;
            if (shopItem.getPackCount() > 1) {
                writeS(String.valueOf(item.getName()) + " (" + shopItem.getPackCount() + ")" + s1 + s2 + s3);
            } else {
                writeS(String.valueOf(item.getName()) + s1 + s2 + s3);
            }
            if (ConfigOther.SHOPINFO) {
                byte[] status = new L1ItemStatus(item).getStatusBytes().getBytes();
                writeC(status.length);
                int length = status.length;
                for (int i2 = 0; i2 < length; i2++) {
                    writeC(status[i2]);
                }
            } else {
                writeC(0);
            }
        }
        writeH(7);
    }

    public S_ShopSellList(L1NpcInstance npc) {
        writeC(OpcodesServer.S_OPCODE_SHOWSHOPBUYLIST);
        writeD(npc.getId());
        List<L1ShopItem> shopItems = ShopTable.get().get(npc.getNpcTemplate().get_npcId()).getSellingItems();
        if (shopItems.size() <= 0) {
            writeH(0);
            return;
        }
        writeH(shopItems.size());
        for (int i = 0; i < shopItems.size(); i++) {
            L1ShopItem shopItem = shopItems.get(i);
            L1Item item = shopItem.getItem();
            int price = shopItem.getPrice();
            writeD(i);
            writeH(shopItem.getItem().getGfxId());
            writeD(price);
            if (shopItem.getPackCount() > 1) {
                writeS(String.valueOf(item.getName()) + " (" + shopItem.getPackCount() + ")");
            } else {
                writeS(item.getName());
            }
            writeC(0);
        }
        writeH(7);
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = getBytes();
        }
        return this._byte;
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
