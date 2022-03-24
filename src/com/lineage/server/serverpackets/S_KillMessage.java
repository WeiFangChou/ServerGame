package com.lineage.server.serverpackets;

import com.lineage.config.ConfigKill;
import java.util.Random;

public class S_KillMessage extends ServerBasePacket {
    private static final int GREEN_MESSAGE = 84;
    private static final Random _random = new Random();
    private byte[] _byte = null;

    public S_KillMessage(String winName, String EnchantLevel, String Weapon, String deathName) {
        writeC(133);
        writeC(0);
        writeD(0);
        writeS(String.format(ConfigKill.KILL_TEXT_LIST.get(_random.nextInt(ConfigKill.KILL_TEXT_LIST.size()) + 1), winName, EnchantLevel, Weapon, deathName));
    }

    public S_KillMessage(int type, String winName, String EnchantLevel, String Weapon, String deathName) {
        writeC(40);
        writeC(84);
        writeC(type);
        writeS(String.format(ConfigKill.KILL_TEXT_LIST.get(_random.nextInt(ConfigKill.KILL_TEXT_LIST.size()) + 1), winName, EnchantLevel, Weapon, deathName));
    }

    public S_KillMessage(String name, String msg, int i) {
        writeC(133);
        writeC(0);
        writeD(0);
        writeS(" \\fY[" + name + "] " + msg);
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
