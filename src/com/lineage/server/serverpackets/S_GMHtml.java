package com.lineage.server.serverpackets;

public class S_GMHtml extends ServerBasePacket {
    public S_GMHtml(int _objid, String html) {
        writeC(OpcodesServer.S_OPCODE_SHOWHTML);
        writeD(_objid);
        writeS(html);
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        return getBytes();
    }
}
