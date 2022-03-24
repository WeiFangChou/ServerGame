package com.lineage.server.types;

public class UChar8 {
    public static char[] fromArray(long[] buff) {
        char[] charBuff = new char[(buff.length * 4)];
        for (int i = 0; i < buff.length; i++) {
            charBuff[(i * 4) + 0] = (char) ((int) (buff[i] & 255));
            charBuff[(i * 4) + 1] = (char) ((int) ((buff[i] >> 8) & 255));
            charBuff[(i * 4) + 2] = (char) ((int) ((buff[i] >> 16) & 255));
            charBuff[(i * 4) + 3] = (char) ((int) ((buff[i] >> 24) & 255));
        }
        return charBuff;
    }

    public static char[] fromArray(byte[] buff) {
        char[] charBuff = new char[buff.length];
        for (int i = 0; i < buff.length; i++) {
            charBuff[i] = (char) (buff[i] & 255);
        }
        return charBuff;
    }

    public static char[] fromArray(byte[] buff, int length) {
        char[] charBuff = new char[length];
        for (int i = 0; i < length; i++) {
            charBuff[i] = (char) (buff[i] & 255);
        }
        return charBuff;
    }

    public static char fromUByte8(byte b) {
        return (char) (b & 255);
    }

    public static char[] fromULong32(long l) {
        return new char[]{(char) ((int) (l & 255)), (char) ((int) ((l >> 8) & 255)), (char) ((int) ((l >> 16) & 255)), (char) ((int) ((l >> 24) & 255))};
    }
}
