package com.lineage.server.templates;

public class L1ItemSetItem {
    private final int amount;
    private final int enchant;

    /* renamed from: id */
    private final int f18id;

    public L1ItemSetItem(int id, int amount2, int enchant2) {
        this.f18id = id;
        this.amount = amount2;
        this.enchant = enchant2;
    }

    public int getId() {
        return this.f18id;
    }

    public int getAmount() {
        return this.amount;
    }

    public int getEnchant() {
        return this.enchant;
    }
}
