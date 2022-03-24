package com.lineage.server.model.shop;

import com.lineage.server.templates.L1ShopItem;

/* access modifiers changed from: package-private */
/* compiled from: L1ShopBuyOrderList */
public class L1ShopBuyOrder {
    private final int _count;
    private final L1ShopItem _item;

    public L1ShopBuyOrder(L1ShopItem item, int count) {
        this._item = item;
        this._count = Math.max(0, count);
    }

    public L1ShopItem getItem() {
        return this._item;
    }

    public int getCount() {
        return this._count;
    }
}
