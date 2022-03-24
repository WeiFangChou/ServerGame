package com.lineage.server.templates;

public class L1ItemPowerUpdate {
    private int _itemid;
    private int _mode;
    private int _nedid;
    private int _order_id;
    private int _out;
    private int _random;
    private int _type_id;

    public int get_itemid() {
        return this._itemid;
    }

    public void set_itemid(int itemid) {
        this._itemid = itemid;
    }

    public int get_type_id() {
        return this._type_id;
    }

    public void set_type_id(int type_id) {
        this._type_id = type_id;
    }

    public int get_order_id() {
        return this._order_id;
    }

    public void set_order_id(int order_id) {
        this._order_id = order_id;
    }

    public int get_mode() {
        return this._mode;
    }

    public void set_mode(int mode) {
        this._mode = mode;
    }

    public int get_random() {
        return this._random;
    }

    public void set_random(int random) {
        this._random = random;
    }

    public int get_nedid() {
        return this._nedid;
    }

    public void set_nedid(int _nedid2) {
        this._nedid = _nedid2;
    }

    public int is_out() {
        return this._out;
    }

    public void set_out(int out) {
        this._out = out;
    }
}
