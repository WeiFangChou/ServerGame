package com.lineage.server.templates;

public class L1ItemUpdate {
    public static final String _html_01 = "y_up_i0";
    public static final String _html_02 = "y_up_i1";
    public static final String _html_03 = "y_up_i2";
    private int _item_id;
    private int[] _needcounts = null;
    private int[] _needids = null;
    private int _toid;

    public int get_item_id() {
        return this._item_id;
    }

    public void set_item_id(int _item_id2) {
        this._item_id = _item_id2;
    }

    public int get_toid() {
        return this._toid;
    }

    public void set_toid(int _toid2) {
        this._toid = _toid2;
    }

    public int[] get_needids() {
        return this._needids;
    }

    public void set_needids(int[] _needids2) {
        this._needids = _needids2;
    }

    public int[] get_needcounts() {
        return this._needcounts;
    }

    public void set_needcounts(int[] _needcounts2) {
        this._needcounts = _needcounts2;
    }
}
