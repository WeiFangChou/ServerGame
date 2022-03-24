package com.lineage.server.utils;

import com.lineage.server.model.gametime.L1GameTime;
import java.sql.Time;

public class TimePeriod {
    private final Time _timeEnd;
    private final Time _timeStart;

    public TimePeriod(Time timeStart, Time timeEnd) {
        if (timeStart.equals(timeEnd)) {
            throw new IllegalArgumentException("timeBegin must not equals timeEnd");
        }
        this._timeStart = timeStart;
        this._timeEnd = timeEnd;
    }

    private boolean includes(L1GameTime time, Time timeStart, Time timeEnd) {
        Time when = time.toTime();
        return timeStart.compareTo(when) <= 0 && timeEnd.compareTo(when) > 0;
    }

    public boolean includes(L1GameTime time) {
        if (this._timeStart.after(this._timeEnd)) {
            return !includes(time, this._timeEnd, this._timeStart);
        }
        return includes(time, this._timeStart, this._timeEnd);
    }
}
