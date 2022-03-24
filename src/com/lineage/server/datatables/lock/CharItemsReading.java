package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.CharItemsTable;
import com.lineage.server.datatables.storage.CharItemsStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CharItemsReading {
    private static CharItemsReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final CharItemsStorage _storage = new CharItemsTable();

    private CharItemsReading() {
    }

    public static CharItemsReading get() {
        if (_instance == null) {
            _instance = new CharItemsReading();
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

    public CopyOnWriteArrayList<L1ItemInstance> loadItems(Integer objid) {
        this._lock.lock();
        try {
            return this._storage.loadItems(objid);
        } finally {
            this._lock.unlock();
        }
    }

    public void delUserItems(Integer objid) {
        this._lock.lock();
        try {
            this._storage.delUserItems(objid);
        } finally {
            this._lock.unlock();
        }
    }

    public void del_item(int itemid) {
        this._lock.lock();
        try {
            this._storage.del_item(itemid);
        } finally {
            this._lock.unlock();
        }
    }

    public void storeItem(int objId, L1ItemInstance item) throws Exception {
        this._lock.lock();
        try {
            this._storage.storeItem(objId, item);
        } finally {
            this._lock.unlock();
        }
    }

    public void deleteItem(int objid, L1ItemInstance item) throws Exception {
        this._lock.lock();
        try {
            this._storage.deleteItem(objid, item);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateItemId_Name(L1ItemInstance item) throws Exception {
        this._lock.lock();
        try {
            this._storage.updateItemId_Name(item);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateItemId(L1ItemInstance item) throws Exception {
        this._lock.lock();
        try {
            this._storage.updateItemId(item);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateItemCount(L1ItemInstance item) throws Exception {
        this._lock.lock();
        try {
            this._storage.updateItemCount(item);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateItemDurability(L1ItemInstance item) throws Exception {
        this._lock.lock();
        try {
            this._storage.updateItemDurability(item);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateItemChargeCount(L1ItemInstance item) throws Exception {
        this._lock.lock();
        try {
            this._storage.updateItemChargeCount(item);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateItemRemainingTime(L1ItemInstance item) throws Exception {
        this._lock.lock();
        try {
            this._storage.updateItemRemainingTime(item);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateItemEnchantLevel(L1ItemInstance item) throws Exception {
        this._lock.lock();
        try {
            this._storage.updateItemEnchantLevel(item);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateItemEquipped(L1ItemInstance item) throws Exception {
        this._lock.lock();
        try {
            this._storage.updateItemEquipped(item);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateItemIdentified(L1ItemInstance item) throws Exception {
        this._lock.lock();
        try {
            this._storage.updateItemIdentified(item);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateItemBless(L1ItemInstance item) throws Exception {
        this._lock.lock();
        try {
            this._storage.updateItemBless(item);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateItemAttrEnchantKind(L1ItemInstance item) throws Exception {
        this._lock.lock();
        try {
            this._storage.updateItemAttrEnchantKind(item);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateItemAttrEnchantLevel(L1ItemInstance item) throws Exception {
        this._lock.lock();
        try {
            this._storage.updateItemAttrEnchantLevel(item);
        } finally {
            this._lock.unlock();
        }
    }

    public void updateItemDelayEffect(L1ItemInstance item) throws Exception {
        this._lock.lock();
        try {
            this._storage.updateItemDelayEffect(item);
        } finally {
            this._lock.unlock();
        }
    }

    public int getItemCount(int objId) throws Exception {
        this._lock.lock();
        try {
            return this._storage.getItemCount(objId);
        } finally {
            this._lock.unlock();
        }
    }

    public void getAdenaCount(int objId, long count) throws Exception {
        this._lock.lock();
        try {
            this._storage.getAdenaCount(objId, count);
        } finally {
            this._lock.unlock();
        }
    }

    public boolean getUserItems(int pcObjId, int objid, long count) {
        this._lock.lock();
        try {
            return this._storage.getUserItems(Integer.valueOf(pcObjId), objid, count);
        } finally {
            this._lock.unlock();
        }
    }

    public boolean getUserItem(int objid) {
        this._lock.lock();
        try {
            return this._storage.getUserItem(objid);
        } finally {
            this._lock.unlock();
        }
    }

    public Map<Integer, L1ItemInstance> getUserItems(int itemid) {
        this._lock.lock();
        try {
            return this._storage.getUserItems(itemid);
        } finally {
            this._lock.unlock();
        }
    }
}
