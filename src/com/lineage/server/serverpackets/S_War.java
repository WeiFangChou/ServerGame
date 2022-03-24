package com.lineage.server.serverpackets;

public class S_War extends ServerBasePacket {
    private byte[] _byte = null;

    public S_War(int type, String clan_name1, String clan_name2) {
        buildPacket(type, clan_name1, clan_name2);
    }

    private void buildPacket(int type, String clan_name1, String clan_name2) {
        writeC(OpcodesServer.S_OPCODE_WAR);
        writeC(type);
        writeS(clan_name1);
        writeS(clan_name2);
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
