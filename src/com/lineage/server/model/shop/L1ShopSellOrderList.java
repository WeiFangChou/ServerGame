package com.lineage.server.model.shop;

import com.lineage.server.model.Instance.L1PcInstance;
import java.util.ArrayList;
import java.util.List;

public class L1ShopSellOrderList {
    private final List<L1ShopSellOrder> _list = new ArrayList();
    private final L1PcInstance _pc;
    private final L1Shop _shop;

    L1ShopSellOrderList(L1Shop shop, L1PcInstance pc) {
        this._shop = shop;
        this._pc = pc;
    }

    public void add(int itemObjectId, int count) {
        L1AssessedItem assessedItem = this._shop.assessItem(this._pc.getInventory().getItem(itemObjectId));
        if (assessedItem == null) {
            throw new IllegalArgumentException();
        }
        this._list.add(new L1ShopSellOrder(assessedItem, count));
    }

    /* access modifiers changed from: package-private */
    public L1PcInstance getPc() {
        return this._pc;
    }

    /* access modifiers changed from: package-private */
    public List<L1ShopSellOrder> getList() {
        return this._list;
    }
}
