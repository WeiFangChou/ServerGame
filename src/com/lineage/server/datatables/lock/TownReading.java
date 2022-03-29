package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.TownTable;
import com.lineage.server.datatables.storage.TownStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Town;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TownReading {
    private static TownReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final TownStorage _storage = new TownTable();

    private TownReading() {
    }

    public static TownReading get() {
        if (_instance == null) {
            _instance = new TownReading();
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

    public L1Town[] getTownTableList() {
        this._lock.lock();
        try {
            return this._storage.getTownTableList();
        } finally {
            this._lock.unlock();
        }
    }

    public L1Town getTownTable(int id) {
        this._lock.lock();
        try {
            return this._storage.getTownTable(id);
        } finally {
            this._lock.unlock();
        }
    }

    public boolean isLeader(L1PcInstance pc, int town_id) {
        this._lock.lock();
        try {
            return this._storage.isLeader(pc, town_id);
        } finally {
            this._lock.unlock();
        }
    }

    public void addSalesMoney(int town_id, int salesMoney) {
        this._lock.lock();
        try {
            this._storage.addSalesMoney(town_id, salesMoney);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateTaxRate() {
        this._lock.lock();
        try {
            this._storage.updateTaxRate();
        } finally {
            this._lock.unlock();
        }
    }

    public void updateSalesMoneyYesterday() {
        this._lock.lock();
        try {
            this._storage.updateSalesMoneyYesterday();
        } finally {
            this._lock.unlock();
        }
    }

    public String totalContribution(int townId) {
        this._lock.lock();
        try {
            return this._storage.totalContribution(townId);
        } finally {
            this._lock.unlock();
        }
    }

    public void clearHomeTownID() {
        this._lock.lock();
        try {
            this._storage.clearHomeTownID();
        } finally {
            this._lock.unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    public int getPay(int objid) {
        this._lock.lock();
        final int tmp = 0;
        try {
            this._storage.getPay(objid);
            this._lock.unlock();
        } catch (Throwable th) {
            this._lock.unlock();
        }
        return tmp;
    }
}
