package com.lineage.server.serverpackets;

import java.util.ArrayList;
import java.util.Iterator;

public class S_PacketBoxGame extends ServerBasePacket {
    public static final int GAMECLEAR = 70;
    public static final int GAMEINFO = 66;
    public static final int GAMEOVER = 69;
    public static final int GAMESTART = 64;
    public static final int STARTTIME = 71;
    public static final int STARTTIMECLEAR = 72;
    public static final int TIMESTART = 65;
    private byte[] _byte = null;

    public S_PacketBoxGame(int subCode) {
        writeC(40);
        writeC(subCode);
    }

    public S_PacketBoxGame(int subCode, int value) {
        writeC(40);
        writeC(subCode);
        switch (subCode) {
            case 64:
            case 69:
                writeC(value);
                return;
            case 71:
                writeH(value);
                return;
            default:
                return;
        }
    }

    public S_PacketBoxGame(ArrayList<StringBuilder> list) {
        writeC(40);
        writeC(66);
        writeC(list.size());
        writeC(0);
        writeC(0);
        writeC(0);
        if (list != null) {
            Iterator<StringBuilder> it = list.iterator();
            while (it.hasNext()) {
                writeS(it.next().toString());
            }
        }
    }

    public S_PacketBoxGame(StringBuilder title, ArrayList<StringBuilder> list) {
        writeC(40);
        writeC(66);
        writeC(list.size() + 1);
        writeC(0);
        writeC(0);
        writeC(0);
        writeS(title.toString());
        if (list != null) {
            Iterator<StringBuilder> it = list.iterator();
            while (it.hasNext()) {
                writeS(it.next().toString());
            }
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
