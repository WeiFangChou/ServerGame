package com.lineage.server.datatables.lock;

import com.lineage.server.datatables.sql.PetTable;
import com.lineage.server.datatables.storage.PetStorage;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.templates.L1Pet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PetReading {
    private static PetReading _instance;
    private final Lock _lock = new ReentrantLock(true);
    private final PetStorage _storage = new PetTable();

    private PetReading() {
    }

    public static PetReading get() {
        if (_instance == null) {
            _instance = new PetReading();
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

    public void storeNewPet(L1NpcInstance pet, int objid, int itemobjid) {
        this._lock.lock();
        try {
            this._storage.storeNewPet(pet, objid, itemobjid);
        } finally {
            this._lock.unlock();
        }
    }

    public void storePet(L1Pet pet) {
        this._lock.lock();
        try {
            this._storage.storePet(pet);
        } finally {
            this._lock.unlock();
        }
    }

    public void deletePet(int itemobjid) {
        this._lock.lock();
        try {
            this._storage.deletePet(itemobjid);
        } finally {
            this._lock.unlock();
        }
    }

    public boolean isNameExists(String nameCaseInsensitive) {
        this._lock.lock();
        try {
            return this._storage.isNameExists(nameCaseInsensitive);
        } finally {
            this._lock.unlock();
        }
    }

    public L1Pet getTemplate(int itemobjid) {
        this._lock.lock();
        try {
            return this._storage.getTemplate(itemobjid);
        } finally {
            this._lock.unlock();
        }
    }

    public L1Pet getTemplateX(int npcobjid) {
        this._lock.lock();
        try {
            return this._storage.getTemplateX(npcobjid);
        } finally {
            this._lock.unlock();
        }
    }

    public L1Pet[] getPetTableList() {
        this._lock.lock();
        try {
            return this._storage.getPetTableList();
        } finally {
            this._lock.unlock();
        }
    }
}
