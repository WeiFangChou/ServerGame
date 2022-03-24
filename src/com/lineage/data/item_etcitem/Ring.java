package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

public class Ring extends ItemExecutor {
    private Ring() {
    }

    public static ItemExecutor get() {
        return new Ring();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        boolean partner_stat = false;
        pc.getMapId();
        if (!QuestMapTable.get().isQuestMap(pc.getMapId())) {
            if (pc.getPartnerId() != 0) {
                L1PcInstance partner = (L1PcInstance) World.get().findObject(pc.getPartnerId());
                if (partner != null && partner.getPartnerId() != 0 && pc.getPartnerId() == partner.getId() && partner.getPartnerId() == pc.getId()) {
                    partner_stat = true;
                }
                if (partner_stat) {
                    boolean castle_area = L1CastleLocation.checkInAllWarArea(partner.getX(), partner.getY(), partner.getMapId());
                    if ((partner.getMapId() == 0 || partner.getMapId() == 4 || partner.getMapId() == 304) && !castle_area) {
                        L1Teleport.teleport(pc, partner.getX(), partner.getY(), partner.getMapId(), 5, true);
                    } else {
                        pc.sendPackets(new S_ServerMessage(547));
                    }
                } else {
                    pc.sendPackets(new S_ServerMessage(546));
                }
            } else {
                pc.sendPackets(new S_ServerMessage(662));
            }
        }
    }
}
