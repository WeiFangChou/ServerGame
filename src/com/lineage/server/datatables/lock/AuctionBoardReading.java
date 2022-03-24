package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.AuctionBoardTable;
import com.lineage.server.datatables.storage.AuctionBoardStorage;
import com.lineage.server.templates.L1AuctionBoardTmp;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AuctionBoardReading {
    private static AuctionBoardReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final AuctionBoardStorage _storage = new AuctionBoardTable();

    private AuctionBoardReading() {
    }

    public static AuctionBoardReading get() {
        if (_instance == null) {
            _instance = new AuctionBoardReading();
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

    public Map<Integer, L1AuctionBoardTmp> getAuctionBoardTableList() {
        this._lock.lock();
        try {
            return this._storage.getAuctionBoardTableList();
        } finally {
            this._lock.unlock();
        }
    }

    public L1AuctionBoardTmp getAuctionBoardTable(int houseId) {
        this._lock.lock();
        try {
            return this._storage.getAuctionBoardTable(houseId);
        } finally {
            this._lock.unlock();
        }
    }

    public void insertAuctionBoard(L1AuctionBoardTmp board) {
        this._lock.lock();
        try {
            this._storage.insertAuctionBoard(board);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateAuctionBoard(L1AuctionBoardTmp board) {
        this._lock.lock();
        try {
            this._storage.updateAuctionBoard(board);
        } finally {
            this._lock.unlock();
        }
    }

    public void deleteAuctionBoard(int houseId) {
        this._lock.lock();
        try {
            this._storage.deleteAuctionBoard(houseId);
        } finally {
            this._lock.unlock();
        }
    }
}
