package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.IpTable;
import com.lineage.server.datatables.storage.IpStorage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IpReading {
    private static IpReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final IpStorage _storage = new IpTable();

    private IpReading() {
    }

    public static IpReading get() {
        if (_instance == null) {
            _instance = new IpReading();
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

    public void add(String ip, String info) {
        this._lock.lock();
        try {
            this._storage.add(ip, info);
        } finally {
            this._lock.unlock();
        }
    }

    public void remove(String ip) {
        this._lock.lock();
        try {
            this._storage.remove(ip);
        } finally {
            this._lock.unlock();
        }
    }
}
