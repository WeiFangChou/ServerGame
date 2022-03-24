package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.OtherUserTradeTable;
import com.lineage.server.datatables.storage.OtherUserTradeStorage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OtherUserTitleReading {
    private static OtherUserTitleReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final OtherUserTradeStorage _storage = new OtherUserTradeTable();

    private OtherUserTitleReading() {
    }

    public static OtherUserTitleReading get() {
        if (_instance == null) {
            _instance = new OtherUserTitleReading();
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
