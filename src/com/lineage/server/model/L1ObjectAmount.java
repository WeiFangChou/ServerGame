package com.lineage.server.model;

public class L1ObjectAmount<T> {
    private final long _amount;
    private final int _en;
    private final T _obj;

    public L1ObjectAmount(T obj, long amount, int en) {
        this._obj = obj;
        this._amount = amount;
        this._en = en;
    }

    public T getObject() {
        return this._obj;
    }

    public long getAmount() {
        return this._amount;
    }

    public int getEnchant() {
        return this._en;
    }
}
