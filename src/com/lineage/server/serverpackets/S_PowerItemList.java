package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import java.util.List;

public class S_PowerItemList extends ServerBasePacket {
    private byte[] _byte = null;

    public S_PowerItemList(L1PcInstance pc, int objid, List<L1ItemInstance> items) {
        writeC(250);
        writeD(objid);
        writeH(items.size());
        writeC(10);
        for (L1ItemInstance item : items) {
            writeD(item.getId());
            writeC(0);
            writeH(item.get_gfxid());
            writeC(item.getBless());
            writeD(1);
            writeC(item.isIdentified() ? 1 : 0);
            writeS(item.getViewName());
        }
        items.clear();
    }

    public S_PowerItemList(int objid, List<L1ItemInstance> items) {
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
        items.clear();
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
