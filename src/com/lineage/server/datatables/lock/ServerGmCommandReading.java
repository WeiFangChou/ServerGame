package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.ServerGmCommandTable;
import com.lineage.server.datatables.storage.ServerGmCommandStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerGmCommandReading {
    private static ServerGmCommandReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final ServerGmCommandStorage _storage = new ServerGmCommandTable();

    private ServerGmCommandReading() {
    }

    public static ServerGmCommandReading get() {
        if (_instance == null) {
            _instance = new ServerGmCommandReading();
        }
        return _instance;
    }

    public void create(L1PcInstance pc, String cmd) {
        this._lock.lock();
        try {
            this._storage.create(pc, cmd);
        } finally {
            this._lock.unlock();
        }
    }
}
