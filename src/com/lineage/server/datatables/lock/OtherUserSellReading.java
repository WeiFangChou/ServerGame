package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.OtherUserSellTable;
import com.lineage.server.datatables.storage.OtherUserSellStorage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OtherUserSellReading {
    private static OtherUserSellReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final OtherUserSellStorage _storage = new OtherUserSellTable();

    private OtherUserSellReading() {
    }

    public static OtherUserSellReading get() {
        if (_instance == null) {
            _instance = new OtherUserSellReading();
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
