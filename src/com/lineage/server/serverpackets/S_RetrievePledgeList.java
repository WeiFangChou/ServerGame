package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.world.WorldClan;

public class S_RetrievePledgeList extends ServerBasePacket {
    private byte[] _byte = null;

    public S_RetrievePledgeList(int objid, L1PcInstance pc) {
        L1Clan clan = WorldClan.get().getClan(pc.getClanname());
        if (clan != null) {
            if (clan.getWarehouseUsingChar() != 0 && clan.getWarehouseUsingChar() != pc.getId()) {
                pc.sendPackets(new S_ServerMessage(209));
            } else if (pc.getInventory().getSize() < 180) {
                int size = clan.getDwarfForClanInventory().getSize();
                if (size > 0) {
                    clan.setWarehouseUsingChar(pc.getId());
                    writeC(250);
                    writeD(objid);
                    writeH(size);
                    writeC(5);
                    for (L1ItemInstance item : clan.getDwarfForClanInventory().getItems()) {
                        writeD(item.getId());
                        int i = item.getItem().getUseType();
                        writeC(i < 0 ? 0 : i);
                        writeH(item.get_gfxid());
                        writeC(item.getBless());
                        writeD((int) Math.min(item.getCount(), 2000000000L));
                        writeC(item.isIdentified() ? 1 : 0);
                        writeS(item.getViewName());
                    }
                    writeD(30);
                    writeD(0);
                    writeH(0);
                    return;
                }
                pc.sendPackets(new S_ServerMessage(1625));
            } else {
                pc.sendPackets(new S_ServerMessage(263));
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
