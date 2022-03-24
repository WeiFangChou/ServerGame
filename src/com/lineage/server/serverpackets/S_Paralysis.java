package com.lineage.server.serverpackets;

public class S_Paralysis extends ServerBasePacket {
    public static final int TYPE_BIND = 6;
    public static final int TYPE_FREEZE = 4;
    public static final int TYPE_PARALYSIS = 1;
    public static final int TYPE_PARALYSIS2 = 2;
    public static final int TYPE_SLEEP = 3;
    public static final int TYPE_STUN = 5;
    public static final int TYPE_TELEPORT_UNLOCK = 7;
    private byte[] _byte = null;

    public S_Paralysis(int type, boolean flag) {
        writeC(165);
        switch (type) {
            case 1:
                if (flag) {
                    writeC(2);
                    return;
                } else {
                    writeC(3);
                    return;
                }
            case 2:
                if (flag) {
                    writeC(4);
                    return;
                } else {
                    writeC(5);
                    return;
                }
            case 3:
                if (flag) {
                    writeC(10);
                    return;
                } else {
                    writeC(11);
                    return;
                }
            case 4:
                if (flag) {
                    writeC(12);
                    return;
                } else {
                    writeC(13);
                    return;
                }
            case 5:
                if (flag) {
                    writeC(22);
                    return;
                } else {
                    writeC(23);
                    return;
                }
            case 6:
                if (flag) {
                    writeC(24);
                    return;
                } else {
                    writeC(25);
                    return;
                }
            case 7:
                if (flag) {
                    writeC(6);
                    return;
                } else {
                    writeC(7);
                    return;
                }
            default:
                return;
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
