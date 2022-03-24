package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.ClanTable;
import com.lineage.server.datatables.storage.ClanStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClanReading {
    private static ClanReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final ClanStorage _storage = new ClanTable();

    private ClanReading() {
    }

    public static ClanReading get() {
        if (_instance == null) {
            _instance = new ClanReading();
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

    public void addDeClan(Integer integer, L1Clan clan) {
        this._lock.lock();
        try {
            this._storage.addDeClan(integer, clan);
        } finally {
            this._lock.unlock();
        }
    }

    public L1Clan createClan(L1PcInstance player, String clan_name) {
        this._lock.lock();
        try {
            return this._storage.createClan(player, clan_name);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateClan(L1Clan clan) {
        this._lock.lock();
        try {
            this._storage.updateClan(clan);
        } finally {
            this._lock.unlock();
        }
    }

    public void deleteClan(String clan_name) {
        this._lock.lock();
        try {
            this._storage.deleteClan(clan_name);
        } finally {
            this._lock.unlock();
        }
    }

    public L1Clan getTemplate(int clan_id) {
        this._lock.lock();
        try {
            return this._storage.getTemplate(clan_id);
        } finally {
            this._lock.unlock();
        }
    }

    public Map<Integer, L1Clan> get_clans() {
        this._lock.lock();
        try {
            return this._storage.get_clans();
        } finally {
            this._lock.unlock();
        }
    }

    public void updateClanOnlineMaxUser(L1Clan clan) {
        this._lock.lock();
        try {
            this._storage.updateClanOnlineMaxUser(clan);
        } finally {
            this._lock.unlock();
        }
    }
}
