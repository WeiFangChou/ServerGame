package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.GamblingTable;
import com.lineage.server.datatables.storage.GamblingStorage;
import com.lineage.server.templates.L1Gambling;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GamblingReading {
    private static GamblingReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final GamblingStorage _storage = new GamblingTable();

    private GamblingReading() {
    }

    public static GamblingReading get() {
        if (_instance == null) {
            _instance = new GamblingReading();
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

    public L1Gambling getGambling(String key) {
        this._lock.lock();
        try {
            return this._storage.getGambling(key);
        } finally {
            this._lock.unlock();
        }
    }

    public L1Gambling getGambling(int key) {
        this._lock.lock();
        try {
            return this._storage.getGambling(key);
        } finally {
            this._lock.unlock();
        }
    }

    public void add(L1Gambling gambling) {
        this._lock.lock();
        try {
            this._storage.add(gambling);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateGambling(int id, int outcount) {
        this._lock.lock();
        try {
            this._storage.updateGambling(id, outcount);
        } finally {
            this._lock.unlock();
        }
    }

    public int[] winCount(int npcid) {
        this._lock.lock();
        try {
            return this._storage.winCount(npcid);
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
}
