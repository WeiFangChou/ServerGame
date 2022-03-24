package com.lineage.server.templates;

import com.lineage.server.utils.TimePeriod;
import java.sql.Time;
import java.sql.Timestamp;

public class L1SpawnTime {
    private boolean _isDeleteAtEndTime;
    private final Timestamp _periodEnd;
    private final Timestamp _periodStart;
    private final int _spawnId;
    private final Time _timeEnd;
    private final TimePeriod _timePeriod;
    private final Time _timeStart;

    public boolean isDeleteAtEndTime() {
        return this._isDeleteAtEndTime;
    }

    private L1SpawnTime(L1SpawnTimeBuilder builder) {
        this._spawnId = builder._spawnId;
        this._timeStart = builder._timeStart;
        this._timeEnd = builder._timeEnd;
        this._timePeriod = new TimePeriod(this._timeStart, this._timeEnd);
        this._periodStart = builder._periodStart;
        this._periodEnd = builder._periodEnd;
        this._isDeleteAtEndTime = builder._isDeleteAtEndTime;
    }

    /* synthetic */ L1SpawnTime(L1SpawnTimeBuilder l1SpawnTimeBuilder, L1SpawnTime l1SpawnTime) {
        this(l1SpawnTimeBuilder);
    }

    public int getSpawnId() {
        return this._spawnId;
    }

    public Time getTimeStart() {
        return this._timeStart;
    }

    public Time getTimeEnd() {
        return this._timeEnd;
    }

    public Timestamp getPeriodStart() {
        return this._periodStart;
    }

    public Timestamp getPeriodEnd() {
        return this._periodEnd;
    }

    public static class L1SpawnTimeBuilder {
        private boolean _isDeleteAtEndTime;
        private Timestamp _periodEnd;
        private Timestamp _periodStart;
        private final int _spawnId;
        private Time _timeEnd;
        private Time _timeStart;

        public L1SpawnTimeBuilder(int spawnId) {
            this._spawnId = spawnId;
        }

        public L1SpawnTime build() {
            return new L1SpawnTime(this, null);
        }

        public void setTimeStart(Time timeStart) {
            this._timeStart = timeStart;
        }

        public void setTimeEnd(Time timeEnd) {
            this._timeEnd = timeEnd;
        }

        public void setPeriodStart(Timestamp periodStart) {
            this._periodStart = periodStart;
        }

        public void setPeriodEnd(Timestamp periodEnd) {
            this._periodEnd = periodEnd;
        }

        public void setDeleteAtEndTime(boolean f) {
            this._isDeleteAtEndTime = f;
        }
    }

    public TimePeriod getTimePeriod() {
        return this._timePeriod;
    }
}
