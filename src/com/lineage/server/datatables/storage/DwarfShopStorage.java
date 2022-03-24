package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.templates.L1ShopS;
import java.util.HashMap;
import java.util.Map;

public interface DwarfShopStorage {
    Map<Integer, L1ItemInstance> allItems();

    HashMap<Integer, L1ShopS> allShopS();

    void deleteItem(int i);

    L1ShopS getShopS(int i);

    HashMap<Integer, L1ShopS> getShopSMap(int i);

    int get_id();

    void insertItem(int i, L1ItemInstance l1ItemInstance, L1ShopS l1ShopS);

    void load();

    void set_id(int i);

    void updateShopS(L1ShopS l1ShopS);
}
