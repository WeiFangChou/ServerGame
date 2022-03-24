package com.lineage.server.serverpackets;

public class S_ShopSellListTmp extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ShopSellListTmp(int objId, int tmp) {
        System.out.println("objId: " + objId + " tmp: " + tmp);
        writeC(OpcodesServer.S_OPCODE_SHOWSHOPBUYLIST);
        writeD(objId);
        writeH(100);
        int i = 0;
        int tmp2 = tmp;
        while (i < 100) {
            writeD(i);
            writeH(tmp2);
            writeD(0);
            writeS("gfxid = " + tmp2);
            System.out.println("gfxid = " + tmp2);
            writeC(0);
            i++;
            tmp2++;
        }
        writeH(7);
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
