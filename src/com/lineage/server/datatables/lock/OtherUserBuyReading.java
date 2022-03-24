package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.OtherUserBuyTable;
import com.lineage.server.datatables.storage.OtherUserBuyStorage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OtherUserBuyReading {
    private static OtherUserBuyReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final OtherUserBuyStorage _storage = new OtherUserBuyTable();

    private OtherUserBuyReading() {
    }

    public static OtherUserBuyReading get() {
        if (_instance == null) {
            _instance = new OtherUserBuyReading();
        }
        return _instance;
    }

    public void add(String itemname, int itemobjid, int itemadena, long itemcount, int pcobjid, String pcname, int srcpcobjid, String srcpcname) {
        this._lock.lock();
        try {
            this._storage.add(itemname, itemobjid, itemadena, itemcount, pcobjid, pcname, srcpcobjid, srcpcname);
        } finally {
            this._lock.unlock();
        }
    }
}
