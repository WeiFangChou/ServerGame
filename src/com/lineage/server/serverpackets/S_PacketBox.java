package com.lineage.server.serverpackets;

public class S_PacketBox extends ServerBasePacket {
    public static final int ADD_EXCLUDE = 18;
    public static final int ADD_EXCLUDE2 = 17;
    public static final int CHARACTER_CONFIG = 41;
    public static final int CLAN = 62;
    public static final int CLAN_EXP = 165;
    public static final int DMG = 74;
    public static final int DOLL = 56;
    public static final int DRAGON = 101;
    public static final int FISHING = 55;
    public static final int FOOD = 11;
    public static final int GUI_VISUAL_EFFECT = 83;
    public static final int HTML_CLAN1 = 38;
    public static final int HTML_CLAN2 = 51;
    public static final int HTML_UB = 14;
    public static final int ICONS1 = 20;
    public static final int ICONS2 = 21;
    public static final int ICON_AURA = 22;
    public static final int ICON_BLUEPOTION = 34;
    public static final int ICON_CHATBAN = 36;
    public static final int ICON_I2H = 40;
    public static final int ICON_POLYMORPH = 35;
    public static final int LEAVES = 82;
    public static final int LOGOUT = 42;
    public static final int MAP_TIME = 159;
    public static final int MAP_TIMER = 153;
    public static final int MSG_CANT_LOGOUT = 43;
    public static final int MSG_CLANUSER = 29;
    public static final int MSG_COLOSSEUM = 49;
    public static final int MSG_DUEL = 5;
    public static final int MSG_ELF = 15;
    public static final int MSG_FEEL_GOOD = 31;
    public static final int MSG_MARRIED = 9;
    public static final int MSG_RANK_CHANGED = 27;
    public static final int MSG_SMS_SENT = 6;
    public static final int MSG_TOWN_LEADER = 23;
    public static final int POISON_ICON = 161;
    public static final int REM_EXCLUDE = 19;
    public static final int SKILL_WEAPON_ICON = 154;
    public static final int SOMETHING1 = 33;
    public static final int SOMETHING2 = 37;
    public static final int TGDMG = 75;
    public static final int WEAPON_DURABILITY = 138;
    public static final int WEIGHT = 10;
    public static final int WISDOM_POTION = 57;
    private byte[] _byte = null;

    public S_PacketBox(int subCode, String msg, int value) {
        writeC(40);
        writeC(82);
        writeS(msg);
        writeH(value);
    }

    public S_PacketBox(int subCode, int type, int time, boolean show) {
        int i = 0;
        writeC(40);
        writeC(subCode);
        switch (subCode) {
            case 154:
                writeH(time);
                writeH(type);
                writeH(0);
                writeH(show ? 1 : i);
                return;
            default:
                return;
        }
    }

    public S_PacketBox(int subCode) {
        writeC(40);
        writeC(subCode);
        switch (subCode) {
        }
    }

    public S_PacketBox(int subCode, int value) {
        writeC(40);
        writeC(subCode);
        switch (subCode) {
            case 6:
            case 10:
            case 11:
                writeC(value);
                return;
            case 15:
            case 27:
            case 49:
                writeC(value);
                return;
            case 34:
            case 35:
            case 36:
            case 40:
                writeH(value);
                return;
            case 56:
                writeH(value);
                return;
            case 57:
                writeC(44);
                writeH(value);
                return;
            case 74:
            case 75:
                writeC(value >> 2);
                return;
            case 83:
                writeC(value);
                return;
            case 138:
                writeC(value);
                return;
            case 165:
                writeH(value);
                return;
            default:
                return;
        }
    }

    public S_PacketBox(int subCode, int type, int time) {
        writeC(40);
        writeC(subCode);
        switch (subCode) {
            case 5:
                writeD(type);
                writeD(time);
                return;
            default:
                return;
        }
    }

    public S_PacketBox(int subCode, String name) {
        writeC(40);
        writeC(subCode);
        switch (subCode) {
            case 18:
            case 19:
            case 23:
                writeS(name);
                return;
            case 20:
            case 21:
            case 22:
            default:
                return;
        }
    }

    public S_PacketBox(int subCode, Object[] names) {
        writeC(40);
        writeC(subCode);
        switch (subCode) {
            case 17:
                writeC(names.length);
                for (Object name : names) {
                    writeS(name.toString());
                }
                return;
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
