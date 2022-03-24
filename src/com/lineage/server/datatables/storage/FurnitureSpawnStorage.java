package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1FurnitureInstance;

public interface FurnitureSpawnStorage {
    void deleteFurniture(L1FurnitureInstance l1FurnitureInstance);

    void insertFurniture(L1FurnitureInstance l1FurnitureInstance);

    void load();
}
