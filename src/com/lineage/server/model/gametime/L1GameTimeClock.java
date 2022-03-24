package com.lineage.server.model.gametime;

import com.lineage.server.thread.GeneralThreadPool;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1GameTimeClock {
    private static L1GameTimeClock _instance;
    private static final Log _log = LogFactory.getLog(L1GameTimeClock.class);
    private volatile L1GameTime _currentTime = L1GameTime.fromSystemCurrentTime();
    private List<L1GameTimeListener> _listeners = new CopyOnWriteArrayList();
    private L1GameTime _previousTime = null;

    private class TimeUpdater implements Runnable {
        private TimeUpdater() {
        }

        /* synthetic */ TimeUpdater(L1GameTimeClock l1GameTimeClock, TimeUpdater timeUpdater) {
            this();
        }

        public void run() {
            while (true) {
                try {
                    L1GameTimeClock.this._previousTime = L1GameTimeClock.this._currentTime;
                    L1GameTimeClock.this._currentTime = L1GameTime.fromSystemCurrentTime();
                    L1GameTimeClock.this.notifyChanged();
                    Thread.sleep(500);
                } catch (Exception e) {
                    L1GameTimeClock._log.error(e.getLocalizedMessage(), e);
                    return;
                }
            }
        }
    }

    private boolean isFieldChanged(int field) {
        return this._previousTime.get(field) != this._currentTime.get(field);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void notifyChanged() {
        if (isFieldChanged(2)) {
            for (L1GameTimeListener listener : this._listeners) {
                listener.onMonthChanged(this._currentTime);
            }
        }
        if (isFieldChanged(5)) {
            for (L1GameTimeListener listener2 : this._listeners) {
                listener2.onDayChanged(this._currentTime);
            }
        }
        if (isFieldChanged(11)) {
            for (L1GameTimeListener listener3 : this._listeners) {
                listener3.onHourChanged(this._currentTime);
            }
        }
        if (isFieldChanged(12)) {
            for (L1GameTimeListener listener4 : this._listeners) {
                listener4.onMinuteChanged(this._currentTime);
            }
        }
    }

    private L1GameTimeClock() {
        GeneralThreadPool.get().execute(new TimeUpdater(this, null));
    }

    public static void init() {
        _instance = new L1GameTimeClock();
    }

    public static L1GameTimeClock getInstance() {
        return _instance;
    }

    public L1GameTime currentTime() {
        return this._currentTime;
    }

    public void addListener(L1GameTimeListener listener) {
        this._listeners.add(listener);
    }

    public void removeListener(L1GameTimeListener listener) {
        this._listeners.remove(listener);
    }
}
