package com.lineage.data.item_etcitem.teleport;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.lock.UpdateLocReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Unfreezing_Reel extends ItemExecutor {
    private Unfreezing_Reel() {
    }

    public static ItemExecutor get() {
        return new Unfreezing_Reel();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        UpdateLocReading.get().setPcLoc(pc.getAccountName());
        pc.sendPackets(new S_ServerMessage("帳號內其他人物傳送回指定位置！"));
    }
}
