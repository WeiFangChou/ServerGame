package com.lineage.server.serverpackets;

import com.lineage.data.event.GamblingSet;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Gambling;
import java.util.Map;

public class S_ShopBuyListGam extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ShopBuyListGam(L1PcInstance pc, L1NpcInstance npc, Map<Integer, L1Gambling> sellList) {
        writeC(170);
        writeD(npc.getId());
        if (sellList.isEmpty()) {
            writeH(0);
        } else if (sellList.size() <= 0) {
            writeH(0);
        } else {
            writeH(sellList.size());
            for (Integer itemobjid : sellList.keySet()) {
                writeD(itemobjid.intValue());
                writeD((int) (((double) GamblingSet.GAMADENA) * sellList.get(itemobjid).get_rate()));
            }
        }
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = getBytes();
        }
        return this._byte;
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
