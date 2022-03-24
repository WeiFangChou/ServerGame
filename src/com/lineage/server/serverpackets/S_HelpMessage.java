package com.lineage.server.serverpackets;

public class S_HelpMessage extends ServerBasePacket {
    private byte[] _byte = null;

    public S_HelpMessage(String name, String info) {
        writeC(133);
        writeC(0);
        writeD(0);
        writeS(String.valueOf(name) + " --> \\f4" + info);
    }

    public S_HelpMessage(String string) {
        writeC(133);
        writeC(0);
        writeD(0);
        writeS(string);
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
