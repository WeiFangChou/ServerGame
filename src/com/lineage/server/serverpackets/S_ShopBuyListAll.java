package com.lineage.server.serverpackets;

import com.lineage.server.datatables.ShopTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.item.L1ItemId;
import java.util.HashMap;
import java.util.Map;

public class S_ShopBuyListAll extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ShopBuyListAll(L1PcInstance pc, L1NpcInstance npc) {
        Map<L1ItemInstance, Integer> assessedItems = assessItems(pc.getInventory());
        if (assessedItems.isEmpty()) {
            pc.sendPackets(new S_NoSell(npc));
        } else if (assessedItems.size() <= 0) {
            pc.sendPackets(new S_NoSell(npc));
        } else {
            writeC(170);
            writeD(npc.getId());
            writeH(assessedItems.size());
            for (L1ItemInstance key : assessedItems.keySet()) {
                writeD(key.getId());
                writeD(assessedItems.get(key).intValue());
            }
            writeH(7);
        }
    }

    private Map<L1ItemInstance, Integer> assessItems(L1PcInventory inv) {
        int price;
        Map<L1ItemInstance, Integer> result = new HashMap<>();
        for (L1ItemInstance item : inv.getItems()) {
            switch (item.getItem().getItemId()) {
                case L1ItemId.ADENA:
                case 40314:
                case 40316:
                case 44070:
                    break;
                default:
                    if (!item.isEquipped() && item.getBless() < 128 && item.get_time() == null && item.getItem().isTradable() && (price = ShopTable.get().getPrice(item.getItemId())) > 0) {
                        result.put(item, Integer.valueOf(price));
                        break;
                    }
            }
        }
        return result;
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
