package com.lineage.server.model.shop;

/* access modifiers changed from: package-private */
/* compiled from: L1ShopSellOrderList */
public class L1ShopSellOrder {
    private final int _count;
    private final L1AssessedItem _item;

    public L1ShopSellOrder(L1AssessedItem item, int count) {
        this._item = item;
        this._count = Math.max(0, count);
    }

    public L1AssessedItem getItem() {
        return this._item;
    }

    public int getCount() {
        return this._count;
    }
}
