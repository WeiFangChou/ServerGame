package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.ServerCnInfoTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerCnInfoReading {
    private static ServerCnInfoReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final ServerCnInfoTable _storage = new ServerCnInfoTable();

    private ServerCnInfoReading() {
    }

    public static ServerCnInfoReading get() {
        if (_instance == null) {
            _instance = new ServerCnInfoReading();
        }
        return _instance;
    }

    public void create(L1PcInstance pc, L1Item itemtmp, int count) {
        this._lock.lock();
        try {
            this._storage.create(pc, itemtmp, count);
        } finally {
            this._lock.unlock();
        }
    }
}
