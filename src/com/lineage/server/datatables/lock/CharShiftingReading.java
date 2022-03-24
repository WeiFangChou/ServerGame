package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.CharShiftingTable;
import com.lineage.server.datatables.storage.CharShiftingStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CharShiftingReading {
    private static CharShiftingReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final CharShiftingStorage _storage = new CharShiftingTable();

    private CharShiftingReading() {
    }

    public static CharShiftingReading get() {
        if (_instance == null) {
            _instance = new CharShiftingReading();
        }
        return _instance;
    }

    public void newShifting(L1PcInstance pc, int tgId, String tgName, int srcObjid, L1Item srcItem, L1ItemInstance newItem, int mode) {
        this._lock.lock();
        try {
            this._storage.newShifting(pc, tgId, tgName, srcObjid, srcItem, newItem, mode);
        } finally {
            this._lock.unlock();
        }
    }
}
