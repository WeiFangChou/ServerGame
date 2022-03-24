package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.CharBookTable;
import com.lineage.server.datatables.storage.CharBookStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1BookMark;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CharBookReading {
    private static CharBookReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final CharBookStorage _storage = new CharBookTable();

    private CharBookReading() {
    }

    public static CharBookReading get() {
        if (_instance == null) {
            _instance = new CharBookReading();
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

    public ArrayList<L1BookMark> getBookMarks(L1PcInstance pc) {
        this._lock.lock();
        try {
            return this._storage.getBookMarks(pc);
        } finally {
            this._lock.unlock();
        }
    }

    public L1BookMark getBookMark(L1PcInstance pc, int i) {
        this._lock.lock();
        try {
            return this._storage.getBookMark(pc, i);
        } finally {
            this._lock.unlock();
        }
    }

    public void deleteBookmark(L1PcInstance pc, String s) {
        this._lock.lock();
        try {
            this._storage.deleteBookmark(pc, s);
        } finally {
            this._lock.unlock();
        }
    }

    public void addBookmark(L1PcInstance pc, String s) {
        this._lock.lock();
        try {
            this._storage.addBookmark(pc, s);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateBookmarkName(L1BookMark bookmark) {
        this._lock.lock();
        try {
            this._storage.updateBookmarkName(bookmark);
        } finally {
            this._lock.unlock();
        }
    }
}
