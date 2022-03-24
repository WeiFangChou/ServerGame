package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.AccountBankTable;
import com.lineage.server.datatables.storage.AccountBankStorage;
import com.lineage.server.templates.L1Bank;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AccountBankReading {
    private static AccountBankReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final AccountBankStorage _storage = new AccountBankTable();

    private AccountBankReading() {
    }

    public static AccountBankReading get() {
        if (_instance == null) {
            _instance = new AccountBankReading();
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

    public L1Bank get(String account_name) {
        this._lock.lock();
        try {
            return this._storage.get(account_name);
        } finally {
            this._lock.unlock();
        }
    }

    public Map<String, L1Bank> map() {
        this._lock.lock();
        try {
            return this._storage.map();
        } finally {
            this._lock.unlock();
        }
    }

    public void create(String loginName, L1Bank bank) {
        this._lock.lock();
        try {
            this._storage.create(loginName, bank);
        } finally {
            this._lock.unlock();
        }
    }

    public void updatePass(String loginName, String pwd) {
        this._lock.lock();
        try {
            this._storage.updatePass(loginName, pwd);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateAdena(String loginName, long adena) {
        this._lock.lock();
        try {
            this._storage.updateAdena(loginName, adena);
        } finally {
            this._lock.unlock();
        }
    }
}
