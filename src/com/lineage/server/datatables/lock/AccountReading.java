package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.AccountTable;
import com.lineage.server.datatables.storage.AccountStorage;
import com.lineage.server.templates.L1Account;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AccountReading {
    private static AccountReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final AccountStorage _storage = new AccountTable();

    private AccountReading() {
    }

    public static AccountReading get() {
        if (_instance == null) {
            _instance = new AccountReading();
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

    public boolean isAccountUT(String loginName) {
        this._lock.lock();
        try {
            return this._storage.isAccountUT(loginName);
        } finally {
            this._lock.unlock();
        }
    }

    public L1Account create(String loginName, String pwd, String ip, String host, String spwd) {
        this._lock.lock();
        try {
            return this._storage.create(loginName, pwd, ip, host, spwd);
        } finally {
            this._lock.unlock();
        }
    }

    public boolean isAccount(String loginName) {
        this._lock.lock();
        try {
            return this._storage.isAccount(loginName);
        } finally {
            this._lock.unlock();
        }
    }

    public L1Account getAccount(String loginName) {
        this._lock.lock();
        try {
            return this._storage.getAccount(loginName);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateWarehouse(String loginName, int pwd) {
        this._lock.lock();
        try {
            this._storage.updateWarehouse(loginName, pwd);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateLastActive(L1Account account) {
        this._lock.lock();
        try {
            this._storage.updateLastActive(account);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateCharacterSlot(String loginName, int count) {
        this._lock.lock();
        try {
            this._storage.updateCharacterSlot(loginName, count);
        } finally {
            this._lock.unlock();
        }
    }

    public void updatePwd(String loginName, String newpwd) {
        this._lock.lock();
        try {
            this._storage.updatePwd(loginName, newpwd);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateLan(String loginName, boolean islan) {
        this._lock.lock();
        try {
            this._storage.updateLan(loginName, islan);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateLan() {
        this._lock.lock();
        try {
            this._storage.updateLan();
        } finally {
            this._lock.unlock();
        }
    }

    public int getPoint(String loginName) {
        this._lock.lock();
        try {
            return this._storage.getPoint(loginName);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            this._lock.unlock();
            return 0;
        }
    }

    public void setPoint(String loginName, int point) {
        this._lock.lock();
        try {
            this._storage.setPoint(loginName, point);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateFirstPay(String loginName, int count) {
        this._lock.lock();
        try {
            this._storage.updateFirstPay(loginName, count);
        } finally {
            this._lock.unlock();
        }
    }
}
