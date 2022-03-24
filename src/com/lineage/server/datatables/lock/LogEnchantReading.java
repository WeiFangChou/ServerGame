package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.LogEnchantTable;
import com.lineage.server.datatables.storage.LogEnchantStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LogEnchantReading {
    private static LogEnchantReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final LogEnchantStorage _storage = new LogEnchantTable();

    private LogEnchantReading() {
    }

    public static LogEnchantReading get() {
        if (_instance == null) {
            _instance = new LogEnchantReading();
        }
        return _instance;
    }

    public void failureEnchant(L1PcInstance pc, L1ItemInstance item) {
        this._lock.lock();
        try {
            this._storage.failureEnchant(pc, item);
        } finally {
            this._lock.unlock();
        }
    }
}
