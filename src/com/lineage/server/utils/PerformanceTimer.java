package com.lineage.server.utils;

import com.lineage.config.Config;
import java.util.Calendar;
import java.util.TimeZone;

public class PerformanceTimer {
    private long _begin = System.currentTimeMillis();

    public void reset() {
        this._begin = System.currentTimeMillis();
    }

    public long get() {
        return System.currentTimeMillis() - this._begin;
    }

    public static Calendar getRealTime() {
        return Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE));
    }
}
