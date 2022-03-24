package com.lineage.server.serverpackets;

import com.lineage.server.datatables.ShopCnTable;
import com.lineage.server.model.Instance.L1ItemStatus;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ShopItem;
import java.util.ArrayList;
import java.util.Iterator;

public class S_ShopSellListCnX extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ShopSellListCnX(L1PcInstance pc, int tgObjid) {
        buildPacket(pc, tgObjid);
    }

    private void buildPacket(L1PcInstance pc, int tgObjid) {
        writeC(OpcodesServer.S_OPCODE_SHOWSHOPBUYLIST);
        writeD(tgObjid);
        ArrayList<L1ShopItem> shopItems = ShopCnTable.get().get(1);
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
            if (shopItem.getPackCount() > 1) {
                writeS(String.valueOf(item.getName()) + " (" + shopItem.getPackCount() + ")");
            } else {
                writeS(item.getName());
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
