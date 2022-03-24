package com.lineage.server.types;

public class UByte8 {
    public static byte[] fromArray(long[] buff) {
        byte[] byteBuff = new byte[(buff.length * 4)];
        for (int i = 0; i < buff.length; i++) {
            byteBuff[(i * 4) + 0] = (byte) ((int) (buff[i] & 255));
            byteBuff[(i * 4) + 1] = (byte) ((int) ((buff[i] >> 8) & 255));
            byteBuff[(i * 4) + 2] = (byte) ((int) ((buff[i] >> 16) & 255));
            byteBuff[(i * 4) + 3] = (byte) ((int) ((buff[i] >> 24) & 255));
        }
        return byteBuff;
    }

    public static byte[] fromArray(char[] buff) {
        byte[] byteBuff = new byte[buff.length];
        for (int i = 0; i < buff.length; i++) {
            byteBuff[i] = (byte) (buff[i] & 255);
        }
        return byteBuff;
    }

    public static byte fromUChar8(char c) {
        return (byte) (c & 255);
    }

    public static byte[] fromULong32(long l) {
        return new byte[]{(byte) ((int) (l & 255)), (byte) ((int) ((l >> 8) & 255)), (byte) ((int) ((l >> 16) & 255)), (byte) ((int) ((l >> 24) & 255))};
    }
}
