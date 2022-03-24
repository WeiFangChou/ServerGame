package com.lineage.data.item_etcitem.teleport;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.lock.CharBookReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1BookMark;

public class Move_Reel extends ItemExecutor {
    private Move_Reel() {
    }

    public static ItemExecutor get() {
        return new Move_Reel();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int btele = data[0];
        if (!pc.getMap().isTeleportable()) {
            pc.sendPackets(new S_ServerMessage(647));
            pc.sendPackets(new S_Paralysis(7, false));
            return;
        }
        L1BookMark bookm = CharBookReading.get().getBookMark(pc, btele);
        if (bookm != null) {
            pc.getInventory().removeItem(item, 1);
            L1Teleport.teleport(pc, bookm.getLocX(), bookm.getLocY(), bookm.getMapId(), 5, true);
        } else {
            pc.getInventory().removeItem(item, 1);
            L1Teleport.randomTeleport(pc, true);
        }
        if (pc.hasSkillEffect(78)) {
            pc.killSkillEffectTimer(78);
            pc.startHpRegeneration();
            pc.startMpRegeneration();
        }
    }
}
