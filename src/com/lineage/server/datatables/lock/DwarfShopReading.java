package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.DwarfShopTable;
import com.lineage.server.datatables.storage.DwarfShopStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.templates.L1ShopS;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DwarfShopReading {
    private static DwarfShopReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final DwarfShopStorage _storage = new DwarfShopTable();

    private DwarfShopReading() {
    }

    public static DwarfShopReading get() {
        if (_instance == null) {
            _instance = new DwarfShopReading();
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

    public int nextId() {
        this._lock.lock();
        try {
            int tmp = 1 + this._storage.get_id();
            this._storage.set_id(tmp);
            return tmp;
        } finally {
            this._lock.unlock();
        }
    }

    public HashMap<Integer, L1ShopS> allShopS() {
        this._lock.lock();
        try {
            return this._storage.allShopS();
        } finally {
            this._lock.unlock();
        }
    }

    public Map<Integer, L1ItemInstance> allItems() {
        this._lock.lock();
        try {
            return this._storage.allItems();
        } finally {
            this._lock.unlock();
        }
    }

    public L1ShopS getShopS(int objid) {
        this._lock.lock();
        try {
            return this._storage.getShopS(objid);
        } finally {
            this._lock.unlock();
        }
    }

    public HashMap<Integer, L1ShopS> getShopSMap(int pcobjid) {
        this._lock.lock();
        try {
            return this._storage.getShopSMap(pcobjid);
        } finally {
            this._lock.unlock();
        }
    }

    public void insertItem(int key, L1ItemInstance item, L1ShopS shopS) {
        this._lock.lock();
        try {
            this._storage.insertItem(key, item, shopS);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateShopS(L1ShopS shopS) {
        this._lock.lock();
        try {
            this._storage.updateShopS(shopS);
        } finally {
            this._lock.unlock();
        }
    }

    public void deleteItem(int key) {
        this._lock.lock();
        try {
            this._storage.deleteItem(key);
        } finally {
            this._lock.unlock();
        }
    }
}
