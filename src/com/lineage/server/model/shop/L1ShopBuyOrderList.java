package com.lineage.server.model.shop;

import com.lineage.config.ConfigRate;
import com.lineage.server.model.L1TaxCalculator;
import com.lineage.server.templates.L1ShopItem;
import java.util.ArrayList;
import java.util.List;

public class L1ShopBuyOrderList {
    private final List<L1ShopBuyOrder> _list = new ArrayList();
    private final L1Shop _shop;
    private final L1TaxCalculator _taxCalc;
    private int _totalPrice = 0;
    private int _totalPriceTaxIncluded = 0;
    private int _totalWeight = 0;

    public L1ShopBuyOrderList(L1Shop shop) {
        this._shop = shop;
        this._taxCalc = new L1TaxCalculator(shop.getNpcId());
    }

    public boolean isEmpty() {
        return this._list.isEmpty();
    }

    public void add(int orderNumber, int count) {
        int ch_count;
        if (this._shop.getSellingItems().size() >= orderNumber && (ch_count = Math.max(0, count)) > 0) {
            L1ShopItem shopItem = this._shop.getSellingItems().get(orderNumber);
            int price = (int) (((double) shopItem.getPrice()) * ConfigRate.RATE_SHOP_SELLING_PRICE);
            for (int j = 0; j < ch_count; j++) {
                if (price * j < 0) {
                    return;
                }
            }
            if (this._totalPrice >= 0) {
                this._totalPrice += price * ch_count;
                this._totalPriceTaxIncluded += this._taxCalc.layTax(price) * ch_count;
                this._totalWeight += shopItem.getItem().getWeight() * ch_count * shopItem.getPackCount();
                if (shopItem.getItem().isStackable()) {
                    this._list.add(new L1ShopBuyOrder(shopItem, shopItem.getPackCount() * ch_count));
                    return;
                }
                for (int i = 0; i < shopItem.getPackCount() * ch_count; i++) {
                    this._list.add(new L1ShopBuyOrder(shopItem, 1));
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public List<L1ShopBuyOrder> getList() {
        return this._list;
    }

    public int getTotalWeight() {
        return this._totalWeight;
    }

    public int getTotalPrice() {
        return this._totalPrice;
    }

    public int getTotalPriceTaxIncluded() {
        return this._totalPriceTaxIncluded;
    }

    /* access modifiers changed from: package-private */
    public L1TaxCalculator getTaxCalculator() {
        return this._taxCalc;
    }
}
