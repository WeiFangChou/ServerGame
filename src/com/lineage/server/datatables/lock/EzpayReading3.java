package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.EzpayTable3;
import com.lineage.server.datatables.storage.EzpayStorage3;
import com.lineage.server.model.Instance.L1ItemInstance;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EzpayReading3 {
    private static EzpayReading3 _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final EzpayStorage3 _storage = new EzpayTable3();

    private EzpayReading3() {
    }

    public static EzpayReading3 get() {
        if (_instance == null) {
            _instance = new EzpayReading3();
        }
        return _instance;
    }

    public Map<Integer, int[]> ezpayInfo(String loginName) {
        this._lock.lock();
        try {
            return this._storage.ezpayInfo(loginName);
        } finally {
            this._lock.unlock();
        }
    }

    public void insertItem(String account_name, L1ItemInstance item) {
        this._lock.lock();
        try {
            this._storage.insertItem(account_name, item);
        } finally {
            this._lock.unlock();
        }
    }

    public boolean update(String loginName, int id, String pcname, String ip) {
        this._lock.lock();
        try {
            return this._storage.update(loginName, id, pcname, ip);
        } finally {
            this._lock.unlock();
        }
    }
}
