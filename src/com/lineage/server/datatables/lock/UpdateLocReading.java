package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.UpdateLocTable;
import com.lineage.server.datatables.storage.UpdateLocStorage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UpdateLocReading {
    private static UpdateLocReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final UpdateLocStorage _storage = new UpdateLocTable();

    private UpdateLocReading() {
    }

    public static UpdateLocReading get() {
        if (_instance == null) {
            _instance = new UpdateLocReading();
        }
        return _instance;
    }

    public void setPcLoc(String accName) {
        this._lock.lock();
        try {
            this._storage.setPcLoc(accName);
        } finally {
            this._lock.unlock();
        }
    }
}
