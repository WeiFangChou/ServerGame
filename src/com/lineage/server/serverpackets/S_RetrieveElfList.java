package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class S_RetrieveElfList extends ServerBasePacket {
    private byte[] _byte = null;

    public S_RetrieveElfList(int objid, L1PcInstance pc) {
        if (pc.getInventory().getSize() < 180) {
            int size = pc.getDwarfForElfInventory().getSize();
            if (size > 0) {
                writeC(250);
                writeD(objid);
                writeH(size);
                writeC(9);
                for (L1ItemInstance item : pc.getDwarfForElfInventory().getItems()) {
                    writeD(item.getId());
                    int i = item.getItem().getUseType();
                    writeC(i < 0 ? 0 : i);
                    writeH(item.get_gfxid());
                    writeC(item.getBless());
                    writeD((int) Math.min(item.getCount(), 2000000000L));
                    writeC(item.isIdentified() ? 1 : 0);
                    writeS(item.getViewName());
                }
                return;
            }
            pc.sendPackets(new S_ServerMessage(1625));
            return;
        }
        pc.sendPackets(new S_ServerMessage(263));
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
