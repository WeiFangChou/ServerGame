package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.CharacterConfigTable;
import com.lineage.server.datatables.storage.CharacterConfigStorage;
import com.lineage.server.templates.L1Config;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CharacterConfigReading {
    private static CharacterConfigReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final CharacterConfigStorage _storage = new CharacterConfigTable();

    private CharacterConfigReading() {
    }

    public static CharacterConfigReading get() {
        if (_instance == null) {
            _instance = new CharacterConfigReading();
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

    public L1Config get(int objectId) {
        this._lock.lock();
        try {
            return this._storage.get(objectId);
        } finally {
            this._lock.unlock();
        }
    }

    public void storeCharacterConfig(int objectId, int length, byte[] data) {
        this._lock.lock();
        try {
            this._storage.storeCharacterConfig(objectId, length, data);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateCharacterConfig(int objectId, int length, byte[] data) {
        this._lock.lock();
        try {
            this._storage.updateCharacterConfig(objectId, length, data);
        } finally {
            this._lock.unlock();
        }
    }
}
