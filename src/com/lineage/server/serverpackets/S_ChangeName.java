package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_ChangeName extends ServerBasePacket {
    private byte[] _byte = null;

    public S_ChangeName(int objectId, String name) {
        writeC(81);
        writeD(objectId);
        writeS(name);
    }

    public S_ChangeName(int objectId, String name, int mode) {
        String color = "";
        switch (mode) {
            case 0:
                color = "\\f=\\f1";
                break;
            case 1:
                color = "\\f=\\f2";
                break;
            case 2:
                color = "\\f=\\f3";
                break;
            case 3:
                color = "\\f=\\f4";
                break;
            case 4:
                color = "\\f=\\f5";
                break;
            case 5:
                color = "\\f=\\f6";
                break;
            case 6:
                color = "\\f=\\f7";
                break;
            case 7:
                color = "\\f=\\f8";
                break;
            case 8:
                color = "\\f=\\f9";
                break;
            case 9:
                color = "\\f=\\f=";
                break;
            case 10:
                color = "\\f=\\f<";
                break;
        }
        writeC(81);
        writeD(objectId);
        writeS(String.valueOf(color) + "GM \\f=" + name);
    }

    public S_ChangeName(L1PcInstance pc, boolean isName) {
        writeC(81);
        writeD(pc.getId());
        StringBuilder stringBuilder = new StringBuilder();
        if (isName && pc.get_other().get_color() != 0) {
            stringBuilder.append(pc.get_other().color());
        }
        stringBuilder.append(pc.getName());
        writeS(stringBuilder.toString());
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
