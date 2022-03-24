package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;
import java.io.IOException;

public class S_HowManyKey extends ServerBasePacket {
    public S_HowManyKey(L1NpcInstance npc, int price, int min, int max, String htmlId) {
        writeC(253);
        writeD(npc.getId());
        writeD(price);
        writeD(min);
        writeD(min);
        writeD(max);
        writeH(0);
        writeS(htmlId);
        writeC(0);
        writeH(2);
        writeS(npc.getName());
        writeS(String.valueOf(price));
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() throws IOException {
        return getBytes();
    }
}
