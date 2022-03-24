package com.lineage.server.templates;

import com.lineage.server.model.doll.L1DollExecutor;
import java.util.ArrayList;

public class L1Doll {
    private int[] _counts;
    private int _gfxid;
    private int _itemid;
    private String _nameid;
    private int[] _need;
    private int _time;
    private ArrayList<L1DollExecutor> powerList = null;

    public int get_itemid() {
        return this._itemid;
    }

    public void set_itemid(int _itemid2) {
        this._itemid = _itemid2;
    }

    public ArrayList<L1DollExecutor> getPowerList() {
        return this.powerList;
    }

    public void setPowerList(ArrayList<L1DollExecutor> powerList2) {
        this.powerList = powerList2;
    }

    public int[] get_need() {
        return this._need;
    }

    public void set_need(int[] _need2) {
        this._need = _need2;
    }

    public int[] get_counts() {
        return this._counts;
    }

    public void set_counts(int[] _counts2) {
        this._counts = _counts2;
    }

    public int get_gfxid() {
        return this._gfxid;
    }

    public void set_gfxid(int _gfxid2) {
        this._gfxid = _gfxid2;
    }

    public String get_nameid() {
        return this._nameid;
    }

    public void set_nameid(String _nameid2) {
        this._nameid = _nameid2;
    }

    public int get_time() {
        return this._time;
    }

    public void set_time(int _time2) {
        this._time = _time2;
    }
}
