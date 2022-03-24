package com.lineage.server.utils;

public class RandomArrayList {
    private static double[] ArrayDouble = new double[32767];
    private static boolean haveNextGaussian = false;
    private static int listint;
    private static double nextGaussian;

    static {
        listint = 0;
        listint = 0;
        while (listint < 32767) {
            ArrayDouble[listint] = Math.random();
            listint++;
        }
    }

    public static void setArrayList() {
        listint = 0;
        while (listint < 32767) {
            ArrayDouble[listint] = Math.random();
            listint++;
        }
    }

    private static int getlistint() {
        if (listint < 32766) {
            int i = listint + 1;
            listint = i;
            return i;
        }
        listint = 0;
        return 0;
    }

    public static void getByte(byte[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (byte) ((int) getValue(128));
        }
    }

    public static double getGaussian() {
        if (haveNextGaussian) {
            haveNextGaussian = false;
            return nextGaussian;
        }
        while (true) {
            double v1 = (ArrayDouble[getlistint()] * 2.0d) - 1.0d;
            double v2 = (ArrayDouble[getlistint()] * 2.0d) - 1.0d;
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
        return ArrayDouble[getlistint()];
    }

    private static double getValue(int rang) {
        return getValue() * ((double) rang);
    }

    private static double getValue(double rang) {
        return getValue() * rang;
    }

    public static int getInt(int rang) {
        return (int) getValue(rang);
    }

    public static int getInt(double rang) {
        return (int) getValue(rang);
    }

    public static double getDouble() {
        return getValue();
    }

    public static double getDouble(double rang) {
        return getValue(rang);
    }

    public static int getInc(int rang, int increase) {
        return getInt(rang) + increase;
    }

    public static int getInc(double rang, int increase) {
        return getInt(rang) + increase;
    }

    public static double getDc(int rang, int increase) {
        return getValue(rang) + ((double) increase);
    }

    public static double getDc(double rang, int increase) {
        return getValue(rang) + ((double) increase);
    }
}
