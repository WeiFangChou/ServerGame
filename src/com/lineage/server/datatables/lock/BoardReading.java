package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.BoardTable;
import com.lineage.server.datatables.storage.BoardStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Board;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoardReading {
    private static BoardReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final BoardStorage _storage = new BoardTable();

    private BoardReading() {
    }

    public static BoardReading get() {
        if (_instance == null) {
            _instance = new BoardReading();
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

    public Map<Integer, L1Board> getBoardMap() {
        this._lock.lock();
        try {
            return this._storage.getBoardMap();
        } finally {
            this._lock.unlock();
        }
    }

    public L1Board[] getBoardTableList() {
        this._lock.lock();
        try {
            return this._storage.getBoardTableList();
        } finally {
            this._lock.unlock();
        }
    }

    public L1Board getBoardTable(int houseId) {
        this._lock.lock();
        try {
            return this._storage.getBoardTable(houseId);
        } finally {
            this._lock.unlock();
        }
    }

    public int getMaxId() {
        this._lock.lock();
        try {
            return this._storage.getMaxId();
        } finally {
            this._lock.unlock();
        }
    }

    public void writeTopic(L1PcInstance pc, String date, String title, String content) {
        this._lock.lock();
        try {
            this._storage.writeTopic(pc, date, title, content);
        } finally {
            this._lock.unlock();
        }
    }

    public void deleteTopic(int number) {
        this._lock.lock();
        try {
            this._storage.deleteTopic(number);
        } finally {
            this._lock.unlock();
        }
    }
}
