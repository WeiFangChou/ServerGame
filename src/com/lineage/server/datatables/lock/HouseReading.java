package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.HouseTable;
import com.lineage.server.datatables.storage.HouseStorage;
import com.lineage.server.templates.L1House;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HouseReading {
    private static HouseReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final HouseStorage _storage = new HouseTable();

    private HouseReading() {
    }

    public static HouseReading get() {
        if (_instance == null) {
            _instance = new HouseReading();
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

    public Map<Integer, L1House> getHouseTableList() {
        this._lock.lock();
        try {
            return this._storage.getHouseTableList();
        } finally {
            this._lock.unlock();
        }
    }

    public L1House getHouseTable(int houseId) {
        this._lock.lock();
        try {
            return this._storage.getHouseTable(houseId);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateHouse(L1House house) {
        this._lock.lock();
        try {
            this._storage.updateHouse(house);
        } finally {
            this._lock.unlock();
        }
    }
}
