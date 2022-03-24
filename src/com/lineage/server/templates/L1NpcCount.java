package com.lineage.server.templates;

public class L1NpcCount {
    private final int _count;
    private final int _id;

    public L1NpcCount(int id, int count) {
        this._id = id;
        this._count = count;
    }

    public int getId() {
        return this._id;
    }

    public int getCount() {
        return this._count;
    }

    public boolean isZero() {
        return this._id == 0 && this._count == 0;
    }
}
