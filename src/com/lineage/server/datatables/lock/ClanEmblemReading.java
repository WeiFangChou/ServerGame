package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.ClanEmblemTable;
import com.lineage.server.datatables.storage.ClanEmblemStorage;
import com.lineage.server.templates.L1EmblemIcon;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClanEmblemReading {
    private static ClanEmblemReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final ClanEmblemStorage _storage = new ClanEmblemTable();

    private ClanEmblemReading() {
    }

    public static ClanEmblemReading get() {
        if (_instance == null) {
            _instance = new ClanEmblemReading();
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

    public L1EmblemIcon get(int clan_id) {
        this._lock.lock();
        try {
            return this._storage.get(clan_id);
        } finally {
            this._lock.unlock();
        }
    }

    public void add(int clan_id, byte[] icon) {
        this._lock.lock();
        try {
            this._storage.add(clan_id, icon);
        } finally {
            this._lock.unlock();
        }
    }

    public void deleteIcon(int clan_id) {
        this._lock.lock();
        try {
            this._storage.deleteIcon(clan_id);
        } finally {
            this._lock.unlock();
        }
    }

    public L1EmblemIcon storeClanIcon(int clan_id, byte[] emblemicon) {
        this._lock.lock();
        try {
            return this._storage.storeClanIcon(clan_id, emblemicon);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateClanIcon(L1EmblemIcon emblemIcon) {
        this._lock.lock();
        try {
            this._storage.updateClanIcon(emblemIcon);
        } finally {
            this._lock.unlock();
        }
    }
}
