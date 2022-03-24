package com.lineage.server.serverpackets;

public class S_GreenMessage extends ServerBasePacket {
    private static final String S_GREEN_MESSAGE = "[S] S_GreenMessage";
    private byte[] _byte = null;

    public S_GreenMessage(String msg) {
        writeC(40);
        writeC(84);
        writeC(2);
        writeS(msg);
    }

    public S_GreenMessage(int numbers) {
        writeC(40);
        writeC(84);
        writeC(4);
        writeS(new StringBuilder().append(numbers).toString());
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
        return S_GREEN_MESSAGE;
    }
}
