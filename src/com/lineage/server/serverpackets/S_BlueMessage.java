package com.lineage.server.serverpackets;

public class S_BlueMessage extends ServerBasePacket {
    private byte[] _byte = null;

    public S_BlueMessage(int type) {
        buildPacket(type, null);
    }

    public S_BlueMessage(int type, String msg1) {
        buildPacket(type, new String[]{msg1});
    }

    public S_BlueMessage(int type, String msg1, String msg2) {
        buildPacket(type, new String[]{msg1, msg2});
    }

    public S_BlueMessage(int type, String[] info) {
        buildPacket(type, info);
    }

    private void buildPacket(int type, String[] info) {
        writeC(59);
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
