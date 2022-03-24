package com.lineage.server.serverpackets;

import com.lineage.server.datatables.ShopCnTable;
import com.lineage.server.model.Instance.L1ItemStatus;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ShopItem;
import java.util.ArrayList;
import java.util.Iterator;

public class S_ShopSellListCn extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ShopSellListCn(L1PcInstance pc, L1NpcInstance npc) {
        buildPacket(pc, npc.getId(), npc.getNpcId());
    }

    private void buildPacket(L1PcInstance pc, int tgObjid, int npcid) {
        writeC(OpcodesServer.S_OPCODE_SHOWSHOPBUYLIST);
        writeD(tgObjid);
        ArrayList<L1ShopItem> shopItems = ShopCnTable.get().get(npcid);
        if (shopItems.size() <= 0) {
            writeH(0);
            return;
        }
        writeH(shopItems.size());
        int i = 0;
        Iterator<L1ShopItem> it = shopItems.iterator();
        while (it.hasNext()) {
            L1ShopItem shopItem = it.next();
            i++;
            pc.get_otherList().add_cnList(shopItem, i);
            L1Item item = shopItem.getItem();
            writeD(i);
            writeH(item.getGfxId());
            writeD(shopItem.getPrice());
            int enchant = shopItem.getEnchantLevel();
            int daily = shopItem.getDailyBuyingCount();
            String s1 = "";
            String s2 = "";
            if (enchant != 0) {
                s1 = "+" + enchant;
            }
            if (daily != 0) {
                s2 = "[" + daily + " 數量上限]";
            }
            if (shopItem.getPackCount() > 1) {
                writeS(String.valueOf(item.getName()) + " (" + shopItem.getPackCount() + ")" + s1 + s2);
            } else {
                writeS(String.valueOf(item.getName()) + s1 + s2);
            }
            byte[] status = new L1ItemStatus(item).getStatusBytes().getBytes();
            writeC(status.length);
            for (byte b : status) {
                writeC(b);
            }
        }
        writeH(6100);
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
