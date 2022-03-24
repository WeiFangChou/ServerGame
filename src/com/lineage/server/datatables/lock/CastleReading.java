package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.CastleTable;
import com.lineage.server.datatables.storage.CastleStorage;
import com.lineage.server.templates.L1Castle;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CastleReading {
    private static CastleReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final CastleStorage _storage = new CastleTable();

    private CastleReading() {
    }

    public static CastleReading get() {
        if (_instance == null) {
            _instance = new CastleReading();
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

    public Map<Integer, L1Castle> getCastleMap() {
        this._lock.lock();
        try {
            return this._storage.getCastleMap();
        } finally {
            this._lock.unlock();
        }
    }

    public L1Castle[] getCastleTableList() {
        this._lock.lock();
        try {
            return this._storage.getCastleTableList();
        } finally {
            this._lock.unlock();
        }
    }

    public L1Castle getCastleTable(int id) {
        this._lock.lock();
        try {
            return this._storage.getCastleTable(id);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateCastle(L1Castle castle) {
        this._lock.lock();
        try {
            this._storage.updateCastle(castle);
        } finally {
            this._lock.unlock();
        }
    }
}
