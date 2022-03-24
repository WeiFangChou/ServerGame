package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.ServerBasePacket;

public class Elf_Crystallization extends ItemExecutor {
    public static ItemExecutor get() {
        return new Elf_Crystallization();
    }

    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        if (pc.getX() >= 32665 && pc.getX() <= 32674 &&
                pc.getY() >= 32976 && pc.getY() <= 32985 &&
                pc.getMapId() == 440) {
            short mapid = 430;
            L1Teleport.teleport(pc, 32922, 32812, (short)430, 5, true);
        } else {
            pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
        }
    }
}
