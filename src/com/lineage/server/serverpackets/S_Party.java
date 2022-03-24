package com.lineage.server.serverpackets;

public class S_Party extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Party(String htmlid, int objid) {
        buildPacket(htmlid, objid, "", "", 0);
    }

    public S_Party(String htmlid, int objid, String partyname, String partymembers) {
        buildPacket(htmlid, objid, partyname, partymembers, 1);
    }

    private void buildPacket(String htmlid, int objid, String partyname, String partymembers, int type) {
        writeC(OpcodesServer.S_OPCODE_SHOWHTML);
        writeD(objid);
        writeS(htmlid);
        writeH(type);
        writeH(2);
        writeS(partyname);
        writeS(partymembers);
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
