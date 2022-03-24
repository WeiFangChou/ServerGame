package com.lineage.server.model.gametime;

import com.lineage.server.utils.RangeInt;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class L1GameTime {
    private static final long BASE_TIME_IN_MILLIS_REAL = 1278680880000L;
    private final Calendar _calendar;
    private final int _time;

    private Calendar makeCalendar(int time) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(0);
        cal.add(13, time);
        return cal;
    }

    private L1GameTime(int time) {
        this._time = time;
        this._calendar = makeCalendar(time);
    }

    public static L1GameTime valueOf(long timeMillis) {
        long t1 = timeMillis - BASE_TIME_IN_MILLIS_REAL;
        if (t1 < 0) {
            throw new IllegalArgumentException();
        }
        int t2 = (int) ((6 * t1) / 1000);
        return new L1GameTime(t2 - (t2 % 3));
    }

    public static L1GameTime fromSystemCurrentTime() {
        return valueOf(System.currentTimeMillis());
    }

    public static L1GameTime valueOfGameTime(Time time) {
        return new L1GameTime((int) ((time.getTime() + ((long) TimeZone.getDefault().getRawOffset())) / 1000));
    }

    public Time toTime() {
        return new Time((((long) (this._time % 86400)) * 1000) - ((long) TimeZone.getDefault().getRawOffset()));
    }

    public int get(int field) {
        return this._calendar.get(field);
    }

    public int getSeconds() {
        return this._time;
    }

    public Calendar getCalendar() {
        return (Calendar) this._calendar.clone();
    }

    public boolean isNight() {
        return !RangeInt.includes(this._calendar.get(11), 6, 17);
    }

    public String toString() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        f.setTimeZone(this._calendar.getTimeZone());
        return String.valueOf(f.format(this._calendar.getTime())) + "(" + getSeconds() + ")";
    }
}
