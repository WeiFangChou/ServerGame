package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.CharBuffTable;
import com.lineage.server.datatables.storage.CharBuffStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CharBuffReading {
    private static CharBuffReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final CharBuffStorage _storage = new CharBuffTable();

    private CharBuffReading() {
    }

    public static CharBuffReading get() {
        if (_instance == null) {
            _instance = new CharBuffReading();
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

    public void saveBuff(L1PcInstance pc) {
        this._lock.lock();
        try {
            this._storage.saveBuff(pc);
        } finally {
            this._lock.unlock();
        }
    }

    public void buff(L1PcInstance pc) {
        this._lock.lock();
        try {
            this._storage.buff(pc);
        } finally {
            this._lock.unlock();
        }
    }

    public void deleteBuff(L1PcInstance pc) {
        this._lock.lock();
        try {
            this._storage.deleteBuff(pc);
        } finally {
            this._lock.unlock();
        }
    }

    public void deleteBuff(int objid) {
        this._lock.lock();
        try {
            this._storage.deleteBuff(objid);
        } finally {
            this._lock.unlock();
        }
    }
}
