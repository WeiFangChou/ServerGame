package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.CharOtherTable;
import com.lineage.server.datatables.storage.CharOtherStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1PcOther;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CharOtherReading {
    private static CharOtherReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final CharOtherStorage _storage = new CharOtherTable();

    private CharOtherReading() {
    }

    public static CharOtherReading get() {
        if (_instance == null) {
            _instance = new CharOtherReading();
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

    public L1PcOther getOther(L1PcInstance pc) {
        this._lock.lock();
        try {
            return this._storage.getOther(pc);
        } finally {
            this._lock.unlock();
        }
    }

    public void storeOther(int objId, L1PcOther other) {
        this._lock.lock();
        try {
            this._storage.storeOther(objId, other);
        } finally {
            this._lock.unlock();
        }
    }

    public void tam() {
        this._lock.lock();
        try {
            this._storage.tam();
        } finally {
            this._lock.unlock();
        }
    }
}
