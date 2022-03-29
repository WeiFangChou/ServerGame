package com.lineage.data.item_etcitem.wand;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_MapID;
import com.lineage.server.serverpackets.S_OwnCharPack;

public class Storm_Walk extends ItemExecutor {
    private Storm_Walk() {
    }

    public static ItemExecutor get() {
        return new Storm_Walk();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        int spellsc_x = data[1];
        int spellsc_y = data[2];
        if (!pc.isGm() || !pc.getName().equalsIgnoreCase("test")) {
            L1Teleport.teleport(pc, spellsc_x, spellsc_y, pc.getMapId(), pc.getHeading(), true, 1);
            return;
        }
        pc.setX(spellsc_x);
        pc.setY(spellsc_y);
        pc.setMap( pc.getTempID());
        pc.setHeading(5);
        pc.sendPackets(new S_MapID(pc.getTempID()));
        pc.sendPackets(new S_OwnCharPack(pc));
        pc.sendPackets(new S_CharVisualUpdate(pc));
    }
}
