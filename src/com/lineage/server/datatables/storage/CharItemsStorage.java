package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1ItemInstance;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public interface CharItemsStorage {
    void delUserItems(Integer num);

    void del_item(int i);

    void deleteItem(int i, L1ItemInstance l1ItemInstance) throws Exception;

    void getAdenaCount(int i, long j) throws Exception;

    int getItemCount(int i) throws Exception;

    boolean getUserItem(int i);

    Map<Integer, L1ItemInstance> getUserItems(int i);

    boolean getUserItems(Integer num, int i, long j);

    void load();

    CopyOnWriteArrayList<L1ItemInstance> loadItems(Integer num);

    void storeItem(int i, L1ItemInstance l1ItemInstance) throws Exception;

    void updateItemAttrEnchantKind(L1ItemInstance l1ItemInstance) throws Exception;

    void updateItemAttrEnchantLevel(L1ItemInstance l1ItemInstance) throws Exception;

    void updateItemBless(L1ItemInstance l1ItemInstance) throws Exception;

    void updateItemChargeCount(L1ItemInstance l1ItemInstance) throws Exception;

    void updateItemCount(L1ItemInstance l1ItemInstance) throws Exception;

    void updateItemDelayEffect(L1ItemInstance l1ItemInstance) throws Exception;

    void updateItemDurability(L1ItemInstance l1ItemInstance) throws Exception;

    void updateItemEnchantLevel(L1ItemInstance l1ItemInstance) throws Exception;

    void updateItemEquipped(L1ItemInstance l1ItemInstance) throws Exception;

    void updateItemId(L1ItemInstance l1ItemInstance) throws Exception;

    void updateItemId_Name(L1ItemInstance l1ItemInstance) throws Exception;

    void updateItemIdentified(L1ItemInstance l1ItemInstance) throws Exception;

    void updateItemRemainingTime(L1ItemInstance l1ItemInstance) throws Exception;
}
