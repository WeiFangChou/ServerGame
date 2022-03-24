package com.lineage.server.serverpackets;

import com.lineage.server.model.L1NpcTalkData;

public class S_NPCTalkActionTPUrl extends ServerBasePacket {
    private byte[] _byte = null;

    public S_NPCTalkActionTPUrl(L1NpcTalkData cha, Object[] prices, int objid) {
        buildPacket(cha, prices, objid);
    }

    private void buildPacket(L1NpcTalkData npc, Object[] prices, int objid) {
        String htmlid = npc.getTeleportURL();
        writeC(OpcodesServer.S_OPCODE_SHOWHTML);
        writeD(objid);
        writeS(htmlid);
        writeH(1);
        writeH(prices.length);
        for (Object price : prices) {
            writeS(String.valueOf(((Integer) price).intValue()));
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
