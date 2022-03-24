package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.LogChatTable;
import com.lineage.server.datatables.storage.LogChatStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LogChatReading {
    private static LogChatReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final LogChatStorage _storage = new LogChatTable();

    private LogChatReading() {
    }

    public static LogChatReading get() {
        if (_instance == null) {
            _instance = new LogChatReading();
        }
        return _instance;
    }

    public void isTarget(L1PcInstance pc, L1PcInstance target, String text, int type) {
        this._lock.lock();
        try {
            this._storage.isTarget(pc, target, text, type);
        } finally {
            this._lock.unlock();
        }
    }

    public void noTarget(L1PcInstance pc, String text, int type) {
        this._lock.lock();
        try {
            this._storage.noTarget(pc, text, type);
        } finally {
            this._lock.unlock();
        }
    }
}
