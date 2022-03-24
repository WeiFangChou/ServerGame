package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.DwarfForClanTable;
import com.lineage.server.datatables.storage.DwarfForClanStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DwarfForClanReading {
    private static DwarfForClanReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final DwarfForClanStorage _storage = new DwarfForClanTable();

    private DwarfForClanReading() {
    }

    public static DwarfForClanReading get() {
        if (_instance == null) {
            _instance = new DwarfForClanReading();
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

    public CopyOnWriteArrayList<L1ItemInstance> loadItems(String clan_name) {
        this._lock.lock();
        try {
            return this._storage.loadItems(clan_name);
        } finally {
            this._lock.unlock();
        }
    }

    public void delUserItems(String clan_name) {
        this._lock.lock();
        try {
            this._storage.delUserItems(clan_name);
        } finally {
            this._lock.unlock();
        }
    }

    public void insertItem(String clan_name, L1ItemInstance item) {
        this._lock.lock();
        try {
            this._storage.insertItem(clan_name, item);
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

    public void deleteItem(String clan_name, L1ItemInstance item) {
        this._lock.lock();
        try {
            this._storage.deleteItem(clan_name, item);
        } finally {
            this._lock.unlock();
        }
    }

    public boolean getUserItems(String clan_name, int objid, int count) {
        this._lock.lock();
        try {
            return this._storage.getUserItems(clan_name, objid, count);
        } finally {
            this._lock.unlock();
        }
    }
}
