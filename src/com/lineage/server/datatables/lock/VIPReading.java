package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.VIPTable;
import com.lineage.server.datatables.storage.VIPStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class VIPReading {
    private static VIPReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final VIPStorage _storage = new VIPTable();

    private VIPReading() {
    }

    public static VIPReading get() {
        if (_instance == null) {
            _instance = new VIPReading();
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

    public Map<Integer, Timestamp> map() {
        this._lock.lock();
        try {
            return this._storage.map();
        } finally {
            this._lock.unlock();
        }
    }

    public Timestamp getOther(L1PcInstance pc) {
        this._lock.lock();
        try {
            return this._storage.getOther(pc);
        } finally {
            this._lock.unlock();
        }
    }

    public void storeOther(int key, Timestamp value) {
        this._lock.lock();
        try {
            this._storage.storeOther(key, value);
        } finally {
            this._lock.unlock();
        }
    }

    public void delOther(int key) {
        this._lock.lock();
        try {
            this._storage.delOther(key);
        } finally {
            this._lock.unlock();
        }
    }
}
