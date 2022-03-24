package com.lineage.server.serverpackets;

import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.world.WorldQuest;

public class S_MapID extends ServerBasePacket {
    public S_MapID(L1PcInstance pc, int mapid, boolean isUnderwater) {
        int i;
        if (!QuestMapTable.get().isQuestMap(pc.getMapId())) {
            if (pc.get_showId() != -1 && WorldQuest.get().isQuest(pc.get_showId())) {
                WorldQuest.get().remove(pc.get_showId(), pc);
            }
            pc.set_showId(-1);
        }
        writeC(150);
        writeH(mapid);
        if (isUnderwater) {
            i = 1;
        } else {
            i = 0;
        }
        writeC(i);
    }

    public S_MapID(int mapid) {
        writeC(150);
        writeH(mapid);
        writeC(0);
        writeC(16);
        writeH(248);
        writeC(0);
        writeC(0);
        writeC(0);
        writeC(0);
        writeC(0);
        writeC(16);
        writeC(13);
        writeC(53);
        writeC(197);
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        return getBytes();
    }
}
