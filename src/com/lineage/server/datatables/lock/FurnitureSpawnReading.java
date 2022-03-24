package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.FurnitureSpawnTable;
import com.lineage.server.datatables.storage.FurnitureSpawnStorage;
import com.lineage.server.model.Instance.L1FurnitureInstance;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FurnitureSpawnReading {
    private static FurnitureSpawnReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final FurnitureSpawnStorage _storage = new FurnitureSpawnTable();

    private FurnitureSpawnReading() {
    }

    public static FurnitureSpawnReading get() {
        if (_instance == null) {
            _instance = new FurnitureSpawnReading();
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

    public void insertFurniture(L1FurnitureInstance furniture) {
        this._lock.lock();
        try {
            this._storage.insertFurniture(furniture);
        } finally {
            this._lock.unlock();
        }
    }

    public void deleteFurniture(L1FurnitureInstance furniture) {
        this._lock.lock();
        try {
            this._storage.deleteFurniture(furniture);
        } finally {
            this._lock.unlock();
        }
    }
}
