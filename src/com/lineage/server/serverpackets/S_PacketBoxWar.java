package com.lineage.server.serverpackets;

import com.lineage.server.world.WorldClan;
import java.util.HashMap;

public class S_PacketBoxWar extends ServerBasePacket {
    public static final int MSG_WAR_BEGIN = 0;
    public static final int MSG_WAR_END = 1;
    public static final int MSG_WAR_GOING = 2;
    public static final int MSG_WAR_INITIATIVE = 3;
    public static final int MSG_WAR_OCCUPY = 4;
    public static final int MSG_WAR_OVER = 79;
    public static final int MSG_WIN_LASTAVARD = 30;
    private byte[] _byte = null;

    public S_PacketBoxWar() {
        HashMap<Integer, String> map = WorldClan.get().castleClanMap();
        writeC(40);
        writeC(79);
        writeC(7);
        for (int key = 1; key < 7; key++) {
            String clanName = map.get(Integer.valueOf(key));
            if (clanName != null) {
                writeS(clanName);
            } else {
                writeS(" ");
            }
        }
        map.clear();
    }

    public S_PacketBoxWar(int subCode) {
        writeC(40);
        writeC(subCode);
    }

    public S_PacketBoxWar(int subCode, int value) {
        writeC(40);
        writeC(subCode);
        writeC(value);
        writeH(0);
    }

    public S_PacketBoxWar(int subCode, int id, String name, String clanName) {
        writeC(40);
        writeC(30);
        writeD(id);
        writeS(name);
        writeS(clanName);
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
