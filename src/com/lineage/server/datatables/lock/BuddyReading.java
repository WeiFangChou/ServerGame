package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.BuddyTable;
import com.lineage.server.datatables.storage.BuddyStorage;
import com.lineage.server.templates.L1BuddyTmp;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BuddyReading {
    private static BuddyReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final BuddyStorage _storage = new BuddyTable();

    private BuddyReading() {
    }

    public static BuddyReading get() {
        if (_instance == null) {
            _instance = new BuddyReading();
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

    public ArrayList<L1BuddyTmp> userBuddy(int playerobjid) {
        this._lock.lock();
        try {
            return this._storage.userBuddy(playerobjid);
        } finally {
            this._lock.unlock();
        }
    }

    public void addBuddy(int charId, int objId, String name) {
        this._lock.lock();
        try {
            this._storage.addBuddy(charId, objId, name);
        } finally {
            this._lock.unlock();
        }
    }

    public void removeBuddy(int charId, String buddyName) {
        this._lock.lock();
        try {
            this._storage.removeBuddy(charId, buddyName);
        } finally {
            this._lock.unlock();
        }
    }
}
