package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1ItemInstance;
import java.util.concurrent.CopyOnWriteArrayList;

public interface DwarfForElfStorage {
    void delUserItems(String str);

    void deleteItem(String str, L1ItemInstance l1ItemInstance);

    boolean getUserItems(String str, int i, int i2);

    void insertItem(String str, L1ItemInstance l1ItemInstance);

    void load();

    CopyOnWriteArrayList<L1ItemInstance> loadItems(String str);

    void updateItem(L1ItemInstance l1ItemInstance);
}
