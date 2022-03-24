package com.lineage.server.utils;

import java.util.Random;

public class RangeInt {
    private static final Random _rnd = new Random();
    private int _high;
    private int _low;

    public RangeInt(int low, int high) {
        this._low = low;
        this._high = high;
    }

    public RangeInt(RangeInt range) {
        this(range._low, range._high);
    }

    public boolean includes(int i) {
        return this._low <= i && i <= this._high;
    }

    public static boolean includes(int i, int low, int high) {
        return low <= i && i <= high;
    }

    public int ensure(int i) {
        int r = i;
        if (this._low > r) {
            r = this._low;
        }
        if (r <= this._high) {
            return r;
        }
        return this._high;
    }

    public static int ensure(int n, int low, int high) {
        int r = n;
        if (low > r) {
            r = low;
        }
        return r <= high ? r : high;
    }

    public int randomValue() {
        return _rnd.nextInt(getWidth() + 1) + this._low;
    }

    public int getLow() {
        return this._low;
    }

    public int getHigh() {
        return this._high;
    }

    public int getWidth() {
        return this._high - this._low;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof RangeInt)) {
            return false;
        }
        RangeInt range = (RangeInt) obj;
        if (this._low == range._low && this._high == range._high) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "low=" + this._low + ", high=" + this._high;
    }
}
