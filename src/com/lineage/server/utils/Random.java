package com.lineage.server.utils;

public class Random {
    private static int _idx = 0;
    private static final int _max = 32767;
    private static double[] _nArray = new double[32768];
    private static int _nIndex = 0;
    private static double[] _value = new double[32768];
    private static boolean haveNextGaussian = false;
    private static double nextGaussian;

    static {
        _idx = 0;
        _idx = 0;
        while (_idx < 32768) {
            _value[_idx] = ((((Math.random() + Math.random()) + Math.random()) + Math.random()) + Math.random()) % 1.0d;
            _idx++;
        }
        do {
            _nArray[_nIndex] = Math.random();
        } while (getIndex() != 0);
    }

    public static int nextInt(int n) {
        _idx &= _max;
        double[] dArr = _value;
        int i = _idx;
        _idx = i + 1;
        return (int) (dArr[i] * ((double) n));
    }

    public static int nextInt(int n, int offset) {
        _idx &= _max;
        double[] dArr = _value;
        int i = _idx;
        _idx = i + 1;
        return ((int) (dArr[i] * ((double) n))) + offset;
    }

    public static boolean nextBoolean() {
        return nextInt(2) == 1;
    }

    public static byte nextByte() {
        return (byte) nextInt(256);
    }

    public static long nextLong() {
        return (long) (nextInt(Integer.MAX_VALUE) << (nextInt(Integer.MAX_VALUE) + 32));
    }

    private static int getIndex() {
        int i = _nIndex + 1;
        _nIndex = i;
        int i2 = i & _max;
        _nIndex = i2;
        return i2;
    }

    public static void getByte(byte[] arr) {
        int _nLen_t = arr.length;
        for (int i = 0; i < _nLen_t; i++) {
            arr[i] = (byte) ((int) (getValue() * 128.0d));
        }
    }

    public static double getGaussian() {
        if (haveNextGaussian) {
            haveNextGaussian = false;
            return nextGaussian;
        }
        while (true) {
            double v1 = (_nArray[getIndex()] * 2.0d) - 1.0d;
            double v2 = (_nArray[getIndex()] * 2.0d) - 1.0d;
            double s = (v1 * v1) + (v2 * v2);
            if (s < 1.0d && s != 0.0d) {
                double multiplier = Math.sqrt((-2.0d * Math.log(s)) / s);
                nextGaussian = v2 * multiplier;
                haveNextGaussian = true;
                return v1 * multiplier;
            }
        }
    }

    private static double getValue() {
        return _nArray[getIndex()];
    }

    public static int getInt(int rang) {
        return (int) (getValue() * ((double) rang));
    }

    public static int getInt(double rang) {
        return (int) (getValue() * rang);
    }

    public static double getDouble() {
        return getValue();
    }

    public static double getDouble(double rang) {
        return getValue() * rang;
    }

    public static int getInc(int rang, int increase) {
        return getInt(rang) + increase;
    }

    public static int getInc(double rang, int increase) {
        return getInt(rang) + increase;
    }

    public static double getDc(int rang, int increase) {
        return getDouble((double) rang) + ((double) increase);
    }

    public static double getDc(double rang, int increase) {
        return getDouble(rang) + ((double) increase);
    }
}
