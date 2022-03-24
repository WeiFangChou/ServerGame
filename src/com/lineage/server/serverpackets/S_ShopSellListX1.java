package com.lineage.server.serverpackets;

import com.lineage.config.ConfigRate;
import com.lineage.server.datatables.ShopTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1TaxCalculator;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ShopItem;
import com.lineage.server.world.World;
import java.util.List;

public class S_ShopSellListX1 extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ShopSellListX1(int objId) {
        writeC(0);
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
            writeH(i);
            writeH(shopItem.getItem().getGfxId());
            writeH(price);
            if (shopItem.getPackCount() > 1) {
                writeS(String.valueOf(item.getName()) + " (" + shopItem.getPackCount() + ")");
            } else {
                writeS(item.getName());
            }
            writeC(0);
        }
        writeH(0);
    }

    public S_ShopSellListX1(L1NpcInstance npc) {
        writeC(0);
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
        writeH(0);
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
