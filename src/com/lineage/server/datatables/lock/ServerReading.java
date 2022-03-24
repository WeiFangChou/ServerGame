package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.ServerTable;
import com.lineage.server.datatables.storage.ServerStorage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerReading {
    private static ServerReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final ServerStorage _storage = new ServerTable();

    private ServerReading() {
    }

    public static ServerReading get() {
        if (_instance == null) {
            _instance = new ServerReading();
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

    public int minId() {
        this._lock.lock();
        try {
            return this._storage.minId();
        } finally {
            this._lock.unlock();
        }
    }

    public int maxId() {
        this._lock.lock();
        try {
            return this._storage.maxId();
        } finally {
            this._lock.unlock();
        }
    }

    public void isStop() {
        this._lock.lock();
        try {
            this._storage.isStop();
        } finally {
            this._lock.unlock();
        }
    }
}
