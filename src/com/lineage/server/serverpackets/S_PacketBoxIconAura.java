package com.lineage.server.serverpackets;

public class S_PacketBoxIconAura extends ServerBasePacket {
    public static final int ICON_AURA = 22;
    public static final int ICON_E3 = 227;
    public static final int ICON_OS = 125;
    private byte[] _byte = null;

    public S_PacketBoxIconAura(int iconid, int time) {
        writeC(40);
        writeC(22);
        writeC(iconid);
        writeH(time);
    }

    public S_PacketBoxIconAura(int iconid, int time, int type) {
        writeC(40);
        writeC(22);
        writeC(iconid);
        writeH(time);
        writeC(type);
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
