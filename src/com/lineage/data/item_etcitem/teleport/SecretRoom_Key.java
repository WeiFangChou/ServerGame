package com.lineage.data.item_etcitem.teleport;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.data.quest.KnightLv30_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.ServerBasePacket;

public class SecretRoom_Key extends ItemExecutor {
    public static ItemExecutor get() {
        return new SecretRoom_Key();
    }

    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        short mapid = 13;
        if (pc.isKnight() && pc.getX() >= 32806 && pc.getX() <= 32814 &&
                pc.getY() >= 32798 && pc.getY() <= 32807 &&
                pc.getMapId() == 13) {
            if (pc.isKnight()) {
                if (pc.getQuest().isEnd(KnightLv30_1.QUEST.get_id())) {
                    pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                    return;
                }
                if (!pc.getQuest().isStart(KnightLv30_1.QUEST.get_id())) {
                    pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
                } else {
                    L1Teleport.teleport(pc, 32815, 32810, (short)13, 5, false);
                }
            }
        } else {
            pc.sendPackets((ServerBasePacket)new S_ServerMessage(79));
        }
    }
}
