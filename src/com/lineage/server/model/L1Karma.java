package com.lineage.server.model;

import com.lineage.server.utils.RangeInt;

public class L1Karma {
    private static final int[] KARMA_POINT = {10000, 20000, 100000, 500000, 1500000, 3000000, 5000000, 10000000, 15500000};
    private static RangeInt KARMA_RANGE = new RangeInt(-15500000, 15500000);
    private int _karma = 0;

    public int get() {
        return this._karma;
    }

    public void set(int i) {
        this._karma = KARMA_RANGE.ensure(i);
    }

    public void add(int i) {
        set(this._karma + i);
    }

    public int getLevel() {
        boolean isMinus = false;
        int karmaLevel = 0;
        int karma = get();
        if (karma < 0) {
            isMinus = true;
            karma *= -1;
        }
        int[] iArr = KARMA_POINT;
        int length = iArr.length;
        int i = 0;
        while (i < length && karma >= iArr[i]) {
            karmaLevel++;
            if (karmaLevel >= 8) {
                break;
            }
            i++;
        }
        if (isMinus) {
            return karmaLevel * -1;
        }
        return karmaLevel;
    }

    public int getPercent() {
        int karma = get();
        int karmaLevel = getLevel();
        if (karmaLevel == 0) {
            return 0;
        }
        if (karma < 0) {
            karma *= -1;
            karmaLevel *= -1;
        }
        return ((karma - KARMA_POINT[karmaLevel - 1]) * 100) / (KARMA_POINT[karmaLevel] - KARMA_POINT[karmaLevel - 1]);
    }
}
