package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.CharItemsTimeTable;
import com.lineage.server.datatables.storage.CharItemsTimeStorage;
import java.sql.Timestamp;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CharItemsTimeReading {
    private static CharItemsTimeReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final CharItemsTimeStorage _storage = new CharItemsTimeTable();

    private CharItemsTimeReading() {
    }

    public static CharItemsTimeReading get() {
        if (_instance == null) {
            _instance = new CharItemsTimeReading();
        }
        return _instance;
    }

    public void load() {
        this._lock.lock();
        try {
            this._storage.load();
        } finally {
            this._lock.unlock();
        }
    }

    public void addTime(int itemr_obj_id, Timestamp usertime) {
        this._lock.lock();
        try {
            this._storage.addTime(itemr_obj_id, usertime);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateTime(int itemr_obj_id, Timestamp usertime) {
        this._lock.lock();
        try {
            this._storage.updateTime(itemr_obj_id, usertime);
        } finally {
            this._lock.unlock();
        }
    }
}
