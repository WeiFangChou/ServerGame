package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.CharacterQuestTable;
import com.lineage.server.datatables.storage.CharacterQuestStorage;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CharacterQuestReading {
    private static CharacterQuestReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final CharacterQuestStorage _storage = new CharacterQuestTable();

    private CharacterQuestReading() {
    }

    public static CharacterQuestReading get() {
        if (_instance == null) {
            _instance = new CharacterQuestReading();
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

    public Map<Integer, Integer> get(int char_id) {
        this._lock.lock();
        try {
            return this._storage.get(char_id);
        } finally {
            this._lock.unlock();
        }
    }

    public void storeQuest(int char_id, int key, int value) {
        this._lock.lock();
        try {
            this._storage.storeQuest(char_id, key, value);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateQuest(int char_id, int key, int value) {
        this._lock.lock();
        try {
            this._storage.updateQuest(char_id, key, value);
        } finally {
            this._lock.unlock();
        }
    }

    public void delQuest(int char_id, int key) {
        this._lock.lock();
        try {
            this._storage.delQuest(char_id, key);
        } finally {
            this._lock.unlock();
        }
    }
}
