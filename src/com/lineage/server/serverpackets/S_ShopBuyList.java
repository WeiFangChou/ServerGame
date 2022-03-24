package com.lineage.server.serverpackets;

import com.lineage.server.datatables.ShopTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.shop.L1AssessedItem;
import com.lineage.server.model.shop.L1Shop;
import com.lineage.server.world.World;
import java.util.List;

public class S_ShopBuyList extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ShopBuyList(int objid, L1PcInstance pc) {
        L1Object object = World.get().findObject(objid);
        if (object instanceof L1NpcInstance) {
            L1NpcInstance npc = (L1NpcInstance) object;
            L1Shop shop = ShopTable.get().get(npc.getNpcTemplate().get_npcId());
            if (shop == null) {
                pc.sendPackets(new S_NoSell(npc));
                return;
            }
            List<L1AssessedItem> assessedItems = shop.assessItems(pc.getInventory());
            if (assessedItems.isEmpty()) {
                pc.sendPackets(new S_NoSell(npc));
            } else if (assessedItems.size() <= 0) {
                pc.sendPackets(new S_NoSell(npc));
            } else {
                writeC(170);
                writeD(objid);
                writeH(assessedItems.size());
                for (L1AssessedItem item : assessedItems) {
                    writeD(item.getTargetId());
                    writeD(item.getAssessedPrice());
                }
                writeH(7);
            }
        }
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
