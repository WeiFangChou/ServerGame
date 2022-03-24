package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.CharBookConfigTable;
import com.lineage.server.datatables.storage.CharBookConfigStorage;
import com.lineage.server.templates.L1BookConfig;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CharBookConfigReading {
    private static CharBookConfigReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final CharBookConfigStorage _storage = new CharBookConfigTable();

    private CharBookConfigReading() {
    }

    public static CharBookConfigReading get() {
        if (_instance == null) {
            _instance = new CharBookConfigReading();
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

    public L1BookConfig get(int objectId) {
        this._lock.lock();
        try {
            return this._storage.get(objectId);
        } finally {
            this._lock.unlock();
        }
    }

    public void storeCharacterBookConfig(int objectId, byte[] data) {
        this._lock.lock();
        try {
            this._storage.storeCharacterBookConfig(objectId, data);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateCharacterConfig(int objectId, byte[] data) {
        this._lock.lock();
        try {
            this._storage.updateCharacterConfig(objectId, data);
        } finally {
            this._lock.unlock();
        }
    }
}
