package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.SpawnBossTable;
import com.lineage.server.datatables.storage.SpawnBossStorage;
import com.lineage.server.model.L1Spawn;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SpawnBossReading {
    private static SpawnBossReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final SpawnBossStorage _storage = new SpawnBossTable();

    private SpawnBossReading() {
    }

    public static SpawnBossReading get() {
        if (_instance == null) {
            _instance = new SpawnBossReading();
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

    public void upDateNextSpawnTime(int id, Calendar spawnTime) {
        this._lock.lock();
        try {
            this._storage.upDateNextSpawnTime(id, spawnTime);
        } finally {
            this._lock.unlock();
        }
    }

    public L1Spawn getTemplate(int key) {
        this._lock.lock();
        try {
            return this._storage.getTemplate(key);
        } finally {
            this._lock.unlock();
        }
    }

    public List<Integer> bossIds() {
        this._lock.lock();
        try {
            return this._storage.bossIds();
        } finally {
            this._lock.unlock();
        }
    }
}
