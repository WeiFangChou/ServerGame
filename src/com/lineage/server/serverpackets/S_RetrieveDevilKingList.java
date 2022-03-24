package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import java.util.List;

public class S_RetrieveDevilKingList extends ServerBasePacket {
    private byte[] _byte = null;

    public S_RetrieveDevilKingList(L1PcInstance pc, int objid, List<L1ItemInstance> items) {
        writeC(250);
        writeD(objid);
        writeH(items.size());
        writeC(12);
        for (L1ItemInstance item : items) {
            writeD(item.getId());
            writeC(0);
            writeH(item.get_gfxid());
            writeC(item.getBless());
            writeD(1);
            writeC(item.isIdentified() ? 1 : 0);
            writeS(item.getViewName());
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
