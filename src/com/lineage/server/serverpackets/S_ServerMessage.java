package com.lineage.server.serverpackets;

public class S_ServerMessage extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ServerMessage(String name) {
        writeC(133);
        writeC(0);
        writeD(0);
        writeS(name);
    }

    public S_ServerMessage(int type) {
        writeC(14);
        writeH(type);
        writeC(0);
    }

    public S_ServerMessage(int type, String msg1) {
        buildPacket(type, new String[]{msg1});
    }

    public S_ServerMessage(int type, String msg1, String msg2) {
        buildPacket(type, new String[]{msg1, msg2});
    }

    public S_ServerMessage(int type, String msg1, String msg2, String msg3) {
        buildPacket(type, new String[]{msg1, msg2, msg3});
    }

    public S_ServerMessage(int type, String[] info) {
        buildPacket(type, info);
    }

    private void buildPacket(int type, String[] info) {
        writeC(14);
        writeH(type);
        if (info == null) {
            writeC(0);
            return;
        }
        writeC(info.length);
        for (String str : info) {
            writeS(str);
        }
    }

    public S_ServerMessage(String name, int color) {
        writeC(10);
        writeC(color);
        writeS(name);
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
