package com.lineage.server.serverpackets;

public class S_CharPacks extends ServerBasePacket {
    private byte[] _byte = null;

    public S_CharPacks(String name, String clanName, int type, int sex, int lawful, int hp, int mp, int ac, int lv, int str, int dex, int con, int wis, int cha, int intel, int time) {
        writeC(184);
        writeS(name);
        writeS(clanName);
        writeC(type);
        writeC(sex);
        writeH(lawful);
        writeH(hp);
        writeH(mp);
        if (ac > 10) {
            writeC(10);
        } else {
            writeC(ac);
        }
        if (lv > 127) {
            writeC(127);
        } else {
            writeC(lv);
        }
        if (str > 127) {
            writeC(127);
        } else {
            writeC(str);
        }
        if (dex > 127) {
            writeC(127);
        } else {
            writeC(dex);
        }
        if (con > 127) {
            writeC(127);
        } else {
            writeC(con);
        }
        if (wis > 127) {
            writeC(127);
        } else {
            writeC(wis);
        }
        if (cha > 127) {
            writeC(127);
        } else {
            writeC(cha);
        }
        if (intel > 127) {
            writeC(127);
        } else {
            writeC(intel);
        }
        writeC(0);
        String times = Integer.toHexString(time);
        times = times.length() < 8 ? "0" + times : times;
        writeC(Integer.decode("0x" + times.substring(6, 8)).intValue());
        writeC(Integer.decode("0x" + times.substring(4, 6)).intValue());
        writeC(Integer.decode("0x" + times.substring(2, 4)).intValue());
        writeC(Integer.decode("0x" + times.substring(0, 2)).intValue());
        writeC(((((((lv ^ str) ^ dex) ^ con) ^ wis) ^ cha) ^ intel) & 255);
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
