package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Fishing;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.timecontroller.p002pc.PcFishingTimer;

public class FishingPole extends ItemExecutor {
    private FishingPole() {
    }

    public static ItemExecutor get() {
        return new FishingPole();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        startFishing(pc, item.getItemId(), data[0], data[1]);
    }

    private void startFishing(L1PcInstance pc, int itemId, int fishX, int fishY) {
        if (pc.getMapId() != 5300) {
            pc.sendPackets(new S_ServerMessage(1138));
            return;
        }
        int rodLength = 0;
        if (itemId == 41293) {
            rodLength = 5;
        } else if (itemId == 41294) {
            rodLength = 3;
        }
        if (!pc.getMap().isFishingZone(fishX, fishY)) {
            pc.sendPackets(new S_ServerMessage(1138));
        } else if (!pc.getMap().isFishingZone(fishX + 1, fishY) || !pc.getMap().isFishingZone(fishX - 1, fishY) || !pc.getMap().isFishingZone(fishX, fishY + 1) || !pc.getMap().isFishingZone(fishX, fishY - 1)) {
            pc.sendPackets(new S_ServerMessage(1138));
        } else if (fishX > pc.getX() + rodLength || fishX < pc.getX() - rodLength) {
            pc.sendPackets(new S_ServerMessage(1138));
        } else if (fishY > pc.getY() + rodLength || fishY < pc.getY() - rodLength) {
            pc.sendPackets(new S_ServerMessage(1138));
        } else if (pc.getInventory().checkItem(41295, 2)) {
            pc.sendPacketsAll(new S_Fishing(pc.getId(), 71, fishX, fishY));
            pc.setFishing(true, fishX, fishY);
            PcFishingTimer.addMember(pc);
        } else {
            pc.sendPackets(new S_ServerMessage(1137));
        }
    }
}
