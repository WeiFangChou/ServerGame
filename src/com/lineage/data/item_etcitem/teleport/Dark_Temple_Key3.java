package com.lineage.data.item_etcitem.teleport;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.ServerBasePacket;

public class Dark_Temple_Key3 extends ItemExecutor {
    public static ItemExecutor get() {
        return new Dark_Temple_Key3();
    }

    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        if (pc.getX() >= 32698 && pc.getX() <= 32702 &&
                pc.getY() >= 32894 && pc.getY() <= 32898 &&
                pc.getMapId() == 523) {
            L1Teleport.teleport(pc, 32691, 32894, 524, 5, true);
        } else {
            pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
        }
    }
}
