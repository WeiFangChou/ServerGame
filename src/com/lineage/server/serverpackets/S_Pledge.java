package com.lineage.server.serverpackets;

public class S_Pledge extends ServerBasePacket {
    private byte[] _byte = null;

    public S_Pledge(int objid, String clanName, StringBuilder olmembers, StringBuilder allmembers) {
        buildPacket(objid, clanName, olmembers, allmembers);
    }

    private void buildPacket(int objid, String clanname, StringBuilder olmembers, StringBuilder allmembers) {
        writeC(OpcodesServer.S_OPCODE_SHOWHTML);
        writeD(objid);
        writeS("pledgeM");
        writeH(2);
        writeH(3);
        writeS(clanname);
        writeS(olmembers.toString());
        writeS(allmembers.toString());
    }

    public S_Pledge(int objid, String clanName, StringBuilder olmembers) {
        buildPacket(objid, clanName, olmembers);
    }

    private void buildPacket(int objid, String clanname, StringBuilder olmembers) {
        writeC(OpcodesServer.S_OPCODE_SHOWHTML);
        writeD(objid);
        writeS("pledge");
        writeH(1);
        writeH(2);
        writeS(clanname);
        writeS(olmembers.toString());
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
