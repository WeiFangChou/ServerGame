package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.DwarfTable;
import com.lineage.server.datatables.storage.DwarfStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DwarfReading {
    private static DwarfReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final DwarfStorage _storage = new DwarfTable();

    private DwarfReading() {
    }

    public static DwarfReading get() {
        if (_instance == null) {
            _instance = new DwarfReading();
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

    public Map<String, CopyOnWriteArrayList<L1ItemInstance>> allItems() {
        this._lock.lock();
        try {
            return this._storage.allItems();
        } finally {
            this._lock.unlock();
        }
    }

    public CopyOnWriteArrayList<L1ItemInstance> loadItems(String account_name) {
        this._lock.lock();
        try {
            return this._storage.loadItems(account_name);
        } finally {
            this._lock.unlock();
        }
    }

    public void delUserItems(String account_name) {
        this._lock.lock();
        try {
            this._storage.delUserItems(account_name);
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

    public void updateItem(L1ItemInstance item) {
        this._lock.lock();
        try {
            this._storage.updateItem(item);
        } finally {
            this._lock.unlock();
        }
    }

    public void deleteItem(String account_name, L1ItemInstance item) {
        this._lock.lock();
        try {
            this._storage.deleteItem(account_name, item);
        } finally {
            this._lock.unlock();
        }
    }

    public boolean getUserItems(String account_name, int objid, int count) {
        this._lock.lock();
        try {
            return this._storage.getUserItems(account_name, objid, count);
        } finally {
            this._lock.unlock();
        }
    }
}
