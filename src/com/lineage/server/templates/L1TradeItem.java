package com.lineage.server.templates;

import com.lineage.server.model.Instance.L1ItemInstance;

public class L1TradeItem {
    private long _count;
    private L1ItemInstance _item;
    private int _item_id;
    private int _objid;

    public L1ItemInstance get_item() {
        return this._item;
    }

    public void set_item(L1ItemInstance _item2) {
        this._item = _item2;
    }

    public long get_count() {
        return this._count;
    }

    public void set_count(long _count2) {
        this._count = _count2;
    }

    public int get_objid() {
        return this._objid;
    }

    public void set_objid(int _objid2) {
        this._objid = _objid2;
    }

    public int get_item_id() {
        return this._item_id;
    }

    public void set_item_id(int _item_id2) {
        this._item_id = _item_id2;
    }
}
