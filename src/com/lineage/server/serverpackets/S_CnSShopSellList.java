package com.lineage.server.serverpackets;

import com.lineage.server.datatables.lock.DwarfShopReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1ShopS;
import java.util.HashMap;
import java.util.Map;

public class S_CnSShopSellList extends ServerBasePacket {
    private byte[] _byte = null;

    public S_CnSShopSellList(L1PcInstance pc, L1NpcInstance npc) {
        buildPacket(pc, npc.getId());
    }

    private void buildPacket(L1PcInstance pc, int tgObjid) {
        Map<L1ShopS, L1ItemInstance> shopItems = new HashMap<>();
        Map<Integer, L1ItemInstance> srcMap = DwarfShopReading.get().allItems();
        for (Integer key : srcMap.keySet()) {
            L1ShopS info = DwarfShopReading.get().getShopS(key.intValue());
            if (!(info == null || info.get_end() != 0 || info.get_item() == null)) {
                shopItems.put(info, srcMap.get(key));
            }
        }
        writeC(OpcodesServer.S_OPCODE_SHOWSHOPBUYLIST);
        writeD(tgObjid);
        if (shopItems.size() <= 0) {
            writeH(0);
            return;
        }
        writeH(shopItems.size());
        int i = 0;
        for (L1ShopS key2 : shopItems.keySet()) {
            i++;
            L1ItemInstance item = shopItems.get(key2);
            pc.get_otherList().add_cnSList(item, i);
            writeD(i);
            writeH(item.getItem().getGfxId());
            writeD(key2.get_adena());
            writeS(item.getLogName());
            byte[] status = item.getStatusBytes();
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
